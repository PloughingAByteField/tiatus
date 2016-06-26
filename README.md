# tiatus

A work in progress, for now testing gerrit/github integration

Configuring postgres for wildfly

create db and users
psql

CREATE USER tiatus WITH PASSWORD 'tiatus';
CREATE DATABASE tiatus;
GRANT ALL PRIVILEGES ON DATABASE tiatus to tiatus;
CREATE SEQUENCE race_id_sequence;

load postgres driver into wildfly 10 and add data source for the app
./jboss-cli.sh

module add --name=org.postgres --resources=Downloads/postgresql-9.4.1208.jar --dependencies=javax.api,javax.transaction.api

/subsystem=datasources/jdbc-driver=postgres:add(driver-name="postgres",driver-module-name="org.postgres",driver-class-name=org.postgresql.Driver)

data-source add --jndi-name=java:/datasources/TiatusDS --name=TiatusPool --connection-url=jdbc:postgresql://localhost/tiatus --driver-name=postgres --user-name=tiatus --password=tiatus
