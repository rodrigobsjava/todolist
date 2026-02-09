#!/usr/bin/env bash
set -euo pipefail

docker compose -f docker-compose.yml -f docker-compose.prod.yml up -d --build

echo "\nContainers:" 
docker compose -f docker-compose.yml -f docker-compose.prod.yml ps

echo "\nLogs (app):" 
docker compose -f docker-compose.yml -f docker-compose.prod.yml logs -f --tail=200 app


