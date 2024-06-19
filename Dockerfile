FROM openjdk:17-jdk-slim
RUN apt-get update && apt-get install -y postgresql-client && rm -rf /var/lib/apt/lists/*
VOLUME /tmp
COPY target/account-0.0.1-SNAPSHOT.jar app.jar
COPY wait-for-it.sh /wait-for-it.sh
RUN chmod +x /wait-for-it.sh
ENTRYPOINT ["/wait-for-it.sh", "db:5432", "--", "java", "-jar", "/app.jar"]