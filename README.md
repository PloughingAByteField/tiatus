# tiatus

A work in progress, for now testing gerrit/github integration

Configuring postgres for wildfly

create db and users

./jboss-cli.sh

module add --name=org.postgres --resources=Downloads/postgresql-9.4.1208.jar --dependencies=javax.api,javax.transaction.api

/subsystem=datasources/jdbc-driver=postgres:add(driver-name="postgres",driver-module-name="org.postgres",driver-class-name=org.postgresql.Driver)