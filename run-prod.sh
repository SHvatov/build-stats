curl -LJO https://raw.githubusercontent.com/SHvatov/build-stats/main/docker-compose.yml
docker-compose --env-file prod.env up -d --build