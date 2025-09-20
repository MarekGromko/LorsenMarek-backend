FROM jenkins/jenkins:lts

USER root

RUN apt-get update && \
    apt-get install -y wget gnupg2 && \
    rm -rf /var/lib/apt/lists/*

RUN wget https://download.oracle.com/java/24/archive/jdk-24.0.2_linux-aarch64_bin.tar.gz && \
    tar xzf jdk-24.0.2_linux-aarch64_bin.tar.gz -C /opt && \
    rm jdk-24.0.2_linux-aarch64_bin.tar.gz

USER jenkins