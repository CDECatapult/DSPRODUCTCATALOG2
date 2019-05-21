FROM ubuntu:16.04

RUN apt-get update; \
    apt-get install -y --fix-missing python2.7 net-tools python-pip git wget unzip maven mysql-client openjdk-8-jdk; \
    wget http://download.java.net/glassfish/4.1/release/glassfish-4.1.zip; \
    unzip glassfish-4.1.zip; \
    pip install sh; \
    wget http://dev.mysql.com/get/Downloads/Connector-J/mysql-connector-java-5.1.39.tar.gz; \
    tar -xvf mysql-connector-java-5.1.39.tar.gz; \
    cp ./mysql-connector-java-5.1.39/mysql-connector-java-5.1.39-bin.jar glassfish4/glassfish/domains/domain1/lib; \
    mkdir /apis; \
    mkdir -p /etc/default/tmf/

WORKDIR /apis

RUN mkdir wars; \
  git clone https://github.com/CDECatapult/DSPRODUCTCATALOG2.git

WORKDIR DSPRODUCTCATALOG2

RUN git checkout 59b2c360bc264b3e893548df739b59348a09720d; \
    sed -i 's/jdbc\/sample/jdbc\/pcatv2/g' ./src/main/resources/META-INF/persistence.xml; \
    sed -i 's/<provider>org\.eclipse\.persistence\.jpa\.PersistenceProvider<\/provider>/ /g' ./src/main/resources/META-INF/persistence.xml; \
    sed -i 's/<property name="eclipselink\.ddl-generation" value="drop-and-create-tables"\/>/ /g' ./src/main/resources/META-INF/persistence.xml; \
    sed -i 's/<property name="eclipselink\.logging\.level" value="FINE"\/>/ /g' ./src/main/resources/META-INF/persistence.xml; \
    if [ -f "./DSPRODUCTORDERING/src/main/java/org/tmf/dsmapi/settings.properties" ]; then mv ./DSPRODUCTORDERING/src/main/java/org/tmf/dsmapi/settings.properties ./DSPRODUCTORDERING/src/main/resources/settings.properties; fi; \
    grep -F "<property name=\"javax.persistence.schema-generation.database.action\" value=\"create\"/>" ./src/main/resources/META-INF/persistence.xml || sed -i 's/<\/properties>/\t<property name=\"javax.persistence.schema-generation.database.action\" value=\"create\"\/>\n\t\t<\/properties>/g' ./src/main/resources/META-INF/persistence.xml; \
    mvn install; \
    mv ./target/DSProductCatalog.war ../wars/;

WORKDIR /apis

RUN mkdir wars-ext
VOLUME ["/apis/wars-ext", "/etc/default/tmf/"]

COPY ./entrypoint.sh /
COPY ./apis-entrypoint.py /

EXPOSE 4848
EXPOSE 8080

ENTRYPOINT ["/entrypoint.sh"]
