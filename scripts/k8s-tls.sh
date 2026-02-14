#!/usr/bin/env bash
set -euo pipefail

# Generate a self-signed cert for local Minikube and create the Kubernetes TLS secret.
# Host used: projet-indiv.local

NAMESPACE=${1:-projet-indiv}
HOST=${2:-projet-indiv.local}
SECRET_NAME=${3:-projet-indiv-tls}

mkdir -p k8s/tls

echo "[1/3] Generating self-signed certificate for ${HOST}..."
openssl req -x509 -nodes -newkey rsa:2048 -sha256 -days 365 \
  -keyout k8s/tls/tls.key \
  -out k8s/tls/tls.crt \
  -subj "/CN=${HOST}" \
  -addext "subjectAltName=DNS:${HOST}"

echo "[2/3] Creating/refreshing TLS secret ${SECRET_NAME} in namespace ${NAMESPACE}..."
kubectl -n "${NAMESPACE}" delete secret "${SECRET_NAME}" --ignore-not-found
kubectl -n "${NAMESPACE}" create secret tls "${SECRET_NAME}" \
  --cert=k8s/tls/tls.crt \
  --key=k8s/tls/tls.key

echo "[3/3] Done. You can now apply the Ingress: kubectl apply -f k8s/ingress.yaml"