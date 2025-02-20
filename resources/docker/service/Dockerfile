FROM ubuntu:20.04


ENV DEBIAN_FRONTEND=noninteractive

RUN apt-get update && apt-get install -y \
    wget \
    curl \
    gnupg2 \
    software-properties-common \
    && rm -rf /var/lib/apt/lists/*
RUN wget -O- https://apt.corretto.aws/corretto.key | apt-key add - \
    && add-apt-repository 'deb https://apt.corretto.aws stable main'

RUN apt-get update && apt-get install -y \
    java-17-amazon-corretto-jdk \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /app
COPY deploy/gateway-1.0-boot.jar gateway-app.jar

CMD java -jar /app/gateway-app.jar &

EXPOSE 8080