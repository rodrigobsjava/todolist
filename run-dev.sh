#!/usr/bin/env bash
set -euo pipefail

if [[ -f .env.dev ]]; then
  set -a
  source .env.dev
  set +a
fi

./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
