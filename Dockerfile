FROM ubuntu:16.04

RUN apt-get update; \
    apt-get install -y --fix-missing python2.7 net-tools python-pip wget unzip maven mysql-client openjdk-8-jdk; \
    wget http://download.java.net/glassfish/4.1/release/glassfish-4.1.zip; \
    unzip glassfish-4.1.zip; \
    pip install sh; \
    wget http://dev.mysql.com/get/Downloads/Connector-J/mysql-connector-java-5.1.39.tar.gz; \
    tar -xvf mysql-connector-java-5.1.39.tar.gz; \
    cp ./mysql-connector-java-5.1.39/mysql-connector-java-5.1.39-bin.jar glassfish4/glassfish/domains/domain1/lib; \
    mkdir /apis; \
    mkdir -p /etc/default/tmf/

WORKDIR /apis

RUN mkdir wars;

WORKDIR DSPRODUCTCATALOG2

COPY .settings .settings
COPY lib lib
COPY src src
COPY web web
COPY .classpath .
COPY .project .
COPY nb-configuration.xml .
COPY pom.xml .

RUN mvn install; \
    mv ./target/DSProductCatalog.war ../wars/;

WORKDIR /apis

RUN mkdir wars-ext
VOLUME ["/apis/wars-ext", "/etc/default/tmf/"]

COPY ./entrypoint.sh /
COPY ./apis-entrypoint.py /

EXPOSE 4848
EXPOSE 8080

ENTRYPOINT ["/entrypoint.sh"]
