FROM ubuntu:20.04

ARG SCRIPTS_DIR

# basic setupe
RUN apt-get update -y && \
    apt-get install software-properties-common -y && \
    apt-get install wget -y

# install JDK 25
RUN wget https://download.oracle.com/java/25/latest/jdk-25_linux-x64_bin.tar.gz && \
    mkdir -p /opt/java/ && \
    tar -xvzf jdk-25_linux-x64_bin.tar.gz -C /opt/java && \
    rm jdk-25_linux-x64_bin.tar.gz

# install Maven 3.9.11
RUN wget https://dlcdn.apache.org/maven/maven-3/3.9.11/binaries/apache-maven-3.9.11-bin.tar.gz && \
    mkdir -p /opt/maven/ && \
    tar -xvzf apache-maven-3.9.11-bin.tar.gz -C /opt/maven/ && \
    rm apache-maven-3.9.11-bin.tar.gz

ENV JAVA_HOME=/opt/java/jdk-25
ENV MAVEN_HOME=/opt/maven/apache-maven-3.9.11
ENV PATH="$JAVA_HOME/bin:$MAVEN_HOME/bin:$PATH"

# install Git
RUN apt-add-repository ppa:git-core/ppa -y && \
    apt-get update -y && \
    apt-get install git -y

# setup project
COPY ./scripts /scripts
RUN chmod +x /scripts/*.sh && \
    git clone https://github.com/MarekGromko/LorsenMarek-backend.git /home/app

WORKDIR /home/app

ENTRYPOINT ["/scripts/entrypoint.sh"]
CMD ["mvn spring-boot:run"]