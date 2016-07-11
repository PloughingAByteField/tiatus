#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" <<-EOSQL
    CREATE USER tiatus WITH PASSWORD 'tiatus';
    CREATE DATABASE tiatus;
    GRANT ALL PRIVILEGES ON DATABASE tiatus TO tiatus;
EOSQL