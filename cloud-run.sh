#!/bin/bash

set -o allexport
source ../.env
set +o allexport

pm2 start java --name "cash-manager-backend" --interpreter none --watch false -- cash-manager-backend-${IMAGE_VERSION}.jar