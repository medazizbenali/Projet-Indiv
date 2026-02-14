#!/usr/bin/env bash
set -euo pipefail

HOST="${1:-localhost}"

mkdir -p certs

# Certificat auto-signé (démo locale)
openssl req -x509 -nodes -days 365   -newkey rsa:2048   -keyout "certs/local.key"   -out "certs/local.crt"   -subj "/CN=${HOST}"

echo "OK: certs/local.crt et certs/local.key générés pour CN=${HOST}"
echo "Accès: https://localhost:8443"
