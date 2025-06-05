FROM ubuntu:24.04

RUN apt-get update
RUN apt-get install -y curl openjdk-21-jre-headless
RUN curl -sSL https://ngrok-agent.s3.amazonaws.com/ngrok.asc \
      | tee /etc/apt/trusted.gpg.d/ngrok.asc >/dev/null \
      && echo "deb https://ngrok-agent.s3.amazonaws.com buster main" \
      | tee /etc/apt/sources.list.d/ngrok.list \
      && apt update \
      && apt install ngrok

RUN mkdir -p /root/.config/ngrok
RUN echo "version: 2\nweb_addr: 0.0.0.0:4040" >> /root/.config/ngrok/ngrok.yml

# Provision your Java application
ENV NGROK_BINARY_PATH="/usr/local/bin/ngrok"
VOLUME /tmp
COPY build/libs/java-ngrok-example-spring-1.0.0-SNAPSHOT.jar /root/java-ngrok-example-spring-1.0.0-SNAPSHOT.jar
CMD ["java", "-jar", "/root/java-ngrok-example-spring-1.0.0-SNAPSHOT.jar"]

EXPOSE 8080
