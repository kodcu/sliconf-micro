# 1 First intermediate image
FROM maven:3-jdk-8-alpine as pom

COPY pom.xml /sliconf-micro/

# 2 Second intermediate image
FROM pom as source

COPY src/ /sliconf-micro/src/

# run in non-interactive mode
RUN mvn clean install -B -ff -f /sliconf-micro/pom.xml

# 3 final (production) image
FROM openjdk:8-jre-alpine as final

ENV SLICONF_VERSION=0.0.1

COPY --from=source /sliconf-micro/target/sliconf-micro-$SLICONF_VERSION.jar /sliconf-micro/sliconf-micro-$SLICONF_VERSION.jar

EXPOSE 8090

VOLUME /sliconf-micro/log
VOLUME /sliconf-micro/upload

ENTRYPOINT [ "sh", "-c", "java -jar /sliconf-micro/sliconf-micro-${SLICONF_VERSION}.jar"]