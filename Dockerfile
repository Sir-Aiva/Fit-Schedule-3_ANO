FROM jboss/wildfly:18.0.1.Final
ADD aas-gym/target/tasklists.war /opt/jboss/wildfly/standalone/deployments
