#FROM maven:3.5-jdk-8-alpine


FROM alpine
MAINTAINER Zulu Enterprise Container Images <azul-zulu-images@microsoft.com>

ENV LANG en_US.UTF-8
ENV LANGUAGE en_US:en
ENV LC_ALL en_US.UTF-8




RUN mkdir /usr/app

WORKDIR /usr/app

RUN apk update
RUN apk add bash


ARG BUILD_FILE=build.sh

# make JDK Available
ADD ${BUILD_FILE} build.sh

RUN chmod +x ${BUILD_FILE}

RUN ./build.sh

ENV JAVA_HOME=/usr/lib/jvm/${ZULU_DIR}


ARG JAR_FILE=target/api-services-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} api-services-0.0.1-SNAPSHOT.jar

#ARG SHELL_FILE=setup.sh
#ADD ${SHELL_FILE} setup.sh
#RUN chmod +x ${SHELL_FILE}

#CMD  /bin/sh ./setup.sh -
# The setup sh keeps looping to keep the env alive for testing . Do not remove

#CMD  java -Dspring.profiles.active=stage -Djava.security.egd=file:/dev/./urandom -jar api-services-0.0.1-SNAPSHOT.jar
CMD  java -Dspring.profiles.active=${SPRING_PROFILES_ACTIVE} -Djava.security.egd=file:/dev/./urandom -jar api-services-0.0.1-SNAPSHOT.jar


EXPOSE 8088
