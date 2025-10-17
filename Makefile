export ENV ?= dev

# load dotenv
ifneq (,$(wildcard ./.env))
    include .env
    export
endif
ifneq (,$(wildcard ./.$(ENV).env))
	include .$(ENV).env
	export
endif

# these can all be overwritten
export git_branch	?= $(shell git branch --show-current)
export db_port_in 	?= 3306
export db_port_ex 	?= $(if $(DB_PORT),$(DB_PORT),3306)
export app_port_in 	?= $(if $(SERVER_PORT),$(SERVER_PORT),8080)
export app_port_ex 	?= $(if $(SERVER_PORT),$(SERVER_PORT),8080)
export docker_sock_filepath ?= /var/run/docker.sock


# these can not/should not be overwritten
ifneq (,$(BUILD_TAG))
	export IMG_TAG = :$(BUILD_TAG)
	export CTN_TAG = -$(subst .,_,$(BUILD_TAG))
endif
docker_compose_cmd :=
override dca := app 								# docker compose main service name
override dcf := ./docker/${ENV}/docker-compose.yaml # docker compose filepath
override docker_compose = $(docker_compose_cmd) -f ${dcf}

--validate-docker:
	@$(eval docker_compose_cmd = $(shell \
		command -v docker-compose >/dev/null 2>&1 && \
		 	echo "docker-compose" || \
	 	(command -v docker >/dev/null 2>&1 && docker compose version >/dev/null 2>&1 && \
	 		echo "docker compose" || echo "")))
	@if [ -z "$(docker_compose_cmd)" ]; then \
		echo 'could not find the required "docker-compose" or "docker compose" command'; \
		exit 1; \
	fi;

--validate-env:
	@if [ ! -d "./docker/$(ENV)" ]; then \
  		echo "env \"$(ENV)\" is not valid"; \
  		exit 1; \
  	fi;

--validate-all: --validate-env --validate-docker

# REQUIRED TARGETS
build: --validate-all
	$(docker_compose) build

run: --validate-all
	$(docker_compose) up -d

exec: --validate-all
	$(docker_compose) exec ${dca} sh

stop: --validate-all
	$(docker_compose) down

restart: --validate-all
	$(docker_compose) restart

test: --validate-all
	$(docker_compose) exec -T=false --interactive=false ${dca} mvn -ntp clean test

# ADDED TARGETS
prune: --validate-all
	$(docker_compose) rm -s -f -v

status: --validate-all
	$(docker_compose) ps

logs: --validate-all
	$(docker_compose) logs -f ${dca}

once: --validate-all
	$(docker_compose) exec -T=false --interactive=false ${dca} ${exec}

copy: --validate-all
	$(docker_compose) cp ${dca}:${src} ${dest}

# CONTINUED INTEGRATION SETUP (JENKINS)
ci-build: --validate-docker
	docker build \
			--platform=linux/amd64 \
			-f ./ci/Dockerfile \
			-t ci-lorsenmarek-backend \
			./ci

ci-mount: --validate-docker
	docker run -d \
		--privileged \
		-p 8080:8080 \
		-p 50000:50000 \
		-v lorsemarek_backend_jenkins_home:/var/jenkins_home \
		-v ${docker_sock_filepath}:/var/run/docker.sock \
		--name ci-lorsenmarek-backend \
		ci-lorsenmarek-backend
	docker exec -u root ci-lorsenmarek-backend /bin/chown root:docker /var/run/docker.sock

ci-unmount: --validate-docker
	docker stop ci-lorsenmarek-backend && \
    docker rm ci-lorsenmarek-backend