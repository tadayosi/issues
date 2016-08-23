# ENTESB-4850 - No bean could be found in the registry for: ConnectionFactory of type: javax.jms.ConnectionFactory

<https://issues.jboss.org/browse/ENTESB-4850>

The issue is not reproduced with this sample.

## Steps to reproduce

1. Package projects:

        $ mvn clean package

2. Deploy artifacts:

        $ EAP_HOME=/path/to/eap/home ./deploy.sh

3. Start the EAP server:

        $ ./bin/standalone.sh

4. Grep `server.log` for the error messages:

        $ grep "No bean could be found" -r standalone/log/
