FROM jboss/wildfly:latest
MAINTAINER John Reynolds <john.reynolds.irl@acm.org>

ADD /maven/server*.war /opt/jboss/wildfly/standalone/deployments/

RUN mkdir -p /opt/jboss/wildfly/modules/org/postgres/main
COPY postgresql-9.4.1208.jar /opt/jboss/wildfly/modules/org/postgres/main/
COPY module.xml /opt/jboss/wildfly/modules/org/postgres/main/
COPY standalone.xml /opt/jboss/wildfly/standalone/configuration

