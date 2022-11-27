FROM curlimages/curl:latest AS downloader
WORKDIR /tmp
RUN cd /tmp
RUN curl -s https://api.github.com/repos/SHvatov/build-stats/releases/latest \
    | grep "browser_download_url.*.jar" \
    | cut -d : -f 2,3 \
    | tr -d \" \
    | grep "build-stats-module" \
    | xargs wget

FROM eclipse-temurin:17 AS builder
COPY --from=downloader /tmp/build-stats-module.jar /usr/local/lib/app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/local/lib/app.jar"]