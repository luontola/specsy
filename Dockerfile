FROM java:8-jdk

ENV JAVA7_HOME=/usr/lib/jvm/java-7-openjdk-amd64 \
    JAVA8_HOME=/usr/lib/jvm/java-8-openjdk-amd64 \
    MAVEN_VERSION=3.3.9 \
    EDITOR=nano

RUN set -x \
        && apt-get update \
        && apt-get install -y nano openjdk-7-jdk ruby ruby-dev gcc make \
        && rm -rf /var/lib/apt/lists/* \
        && gem install jekyll --no-document \
        && cd /usr/local/lib \
        && curl http://www.nic.funet.fi/pub/mirrors/apache.org/maven/maven-3/$MAVEN_VERSION/binaries/apache-maven-$MAVEN_VERSION-bin.tar.gz \
            | tar -xz \
        && ln -s /usr/local/lib/apache-maven-$MAVEN_VERSION/bin/mvn /usr/local/bin/mvn

WORKDIR /specsy

CMD ["bash"]
