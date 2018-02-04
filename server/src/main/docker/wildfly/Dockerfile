FROM jboss/wildfly:10.1.0.Final
#FROM jboss/wildfly:latest
MAINTAINER John Reynolds <john.reynolds.irl@acm.org>

ADD /maven/server*.war /opt/jboss/wildfly/standalone/deployments/

USER root
RUN mkdir -p /opt/www/tiatus/config/images
RUN mkdir -p /opt/www/tiatus/config/css
RUN chown jboss:jboss -R /opt/www/tiatus

USER jboss

RUN mkdir -p /opt/jboss/wildfly/modules/org/postgres/main
COPY postgresql-9.4.1208.jar /opt/jboss/wildfly/modules/org/postgres/main/
COPY module.xml /opt/jboss/wildfly/modules/org/postgres/main/
COPY standalone.xml /opt/jboss/wildfly/standalone/configuration

