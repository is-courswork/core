#!/bin/sh
set -eu
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname postgres \
  --set=core_password="$CORE_DB_PASSWORD" <<'SQL'
CREATE ROLE airlines_core LOGIN PASSWORD :'core_password';
CREATE DATABASE airlines_core OWNER airlines_core;
SQL
