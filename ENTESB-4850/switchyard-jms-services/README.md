# ENTESB-4850 - No bean could be found in the registry for: ConnectionFactory of type: javax.jms.ConnectionFactory

<https://issues.jboss.org/browse/ENTESB-4850>

## Steps to reproduce

1. Add `<jms-queue>` definitions in `jms-queues.xml` to `standalone-full.xml`.
2. Package projects:

        $ mvn clean package

3. Deploy artifacts:

        $ EAP_HOME=/path/to/eap/home ./deploy.sh

4. Start the EAP server:

        $ ./bin/standalone.sh -c standalone-full.xml

5. Grep `server.log` for the error messages:

        $ grep "No bean could be found" -r standalone/log/
