#!/bin/bash

set -o allexport
source ../.env
set +o allexport

pm2 start pm2-run.sh