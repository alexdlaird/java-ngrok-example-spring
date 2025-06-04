FROM ubuntu:24.04

ARG NGROK_INSTALLER_PATH=ngrok-v3-stable-linux-arm64.tgz

RUN apt-get update
RUN apt-get install -y wget openjdk-21-jre-headless

RUN mkdir -p /root/.config/ngrok
RUN echo "version: 2\nweb_addr: 0.0.0.0:4040" >> /root/.config/ngrok/ngrok.yml

RUN wget https://bin.equinox.io/c/bNyj1mQVY4c/$NGROK_INSTALLER_PATH
RUN tar xvzf ./$NGROK_INSTALLER_PATH -C /usr/local/bin
RUN rm ./$NGROK_INSTALLER_PATH

# Provision your Java application
COPY build/libs/java-ngrok-example-spring-1.0.0-SNAPSHOT.jar /root/java-ngrok-example-spring-1.0.0-SNAPSHOT.jar
CMD ["java", "-jar", "/root/java-ngrok-example-spring-1.0.0-SNAPSHOT.jar"]

EXPOSE 8080
