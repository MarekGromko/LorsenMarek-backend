FROM jenkins/jenkins:lts

USER root

# update/install base command
RUN apt-get update -y && \
    apt-get install build-essential -y && \
    apt-get install git -y && \
    apt-get install wget -y && \
    apt-get install -y curl

# install docker
RUN curl -fsSL https://get.docker.com | sh && \
    mkdir -p ~/.docker/cli-plugins/ && \
    curl -SL https://github.com/docker/compose/releases/latest/download/docker-compose-linux-x86_64 \
    -o ~/.docker/cli-plugins/docker-compose && \
    chmod +x ~/.docker/cli-plugins/docker-compose

# install JDK 25
RUN wget https://download.oracle.com/java/25/latest/jdk-25_linux-x64_bin.tar.gz && \
    tar -xvzf jdk-25_linux-x64_bin.tar.gz -C /opt/java && \
    rm jdk-25_linux-x64_bin.tar.gz

# install Maven 3.9.11
RUN wget https://dlcdn.apache.org/maven/maven-3/3.9.11/binaries/apache-maven-3.9.11-bin.tar.gz && \
    mkdir -p /opt/maven/maven3.9.11 && \
    tar -xvzf apache-maven-3.9.11-bin.tar.gz -C /opt/maven/ && \
    rm apache-maven-3.9.11-bin.tar.gz

ENV JAVA_HOME=/opt/java/jdk-25
ENV MAVEN_HOME=/opt/maven/apache-maven-3.9.11
#ENV PATH="$JAVA_HOME/bin:$MAVEN_HOME/bin:$PATH"

# install plugins
RUN jenkins-plugin-cli --plugins \
    workflow-job:latest \
    configuration-as-code:latest \
    job-dsl:latest \
    git:latest

COPY ./casc /var/jenkins_home/casc
ENV CASC_JENKINS_CONFIG=/var/jenkins_home/casc/jenkins-casc.yaml

RUN usermod -aG docker jenkins

USER jenkins


