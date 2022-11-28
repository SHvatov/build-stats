FROM curlimages/curl:latest AS downloader
WORKDIR /tmp
RUN cd /tmp
RUN curl -s https://api.github.com/repos/SHvatov/build-stats/releases/latest \
    | grep "browser_download_url.*.jar" \
    | cut -d : -f 2,3 \
    | tr -d \" \
    | grep "telegram-bot-module" \
    | xargs wget

FROM eclipse-temurin:17 AS builder
COPY --from=downloader /tmp/telegram-bot-module.jar /usr/local/lib/app.jar
EXPOSE 8082
ENTRYPOINT ["java","-jar","/usr/local/lib/app.jar"]