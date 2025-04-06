FROM gradle:8.12.1-jdk23-corretto

WORKDIR /app
COPY /app .
RUN ./gradlew installDist

ENV JAVA_OPTS="-Xmx512M -Xms512M"
EXPOSE 7000

CMD ["./build/install/app/bin/app"]
