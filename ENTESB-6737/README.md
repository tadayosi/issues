# Jolokia Performance Test

<https://issues.jboss.org/browse/ENTESB-6737>

## How to run

Start a Karaf container and run the following command:

    JBossFuse:karaf@root> shell:source create-queues.script

Then run the test script:

    $ npm install
    $ node index.js

This will send requests to broker named `amq` at `1000` ms intervals and `1000` times. If you want to change the test settings, run the script as follows:

    $ node index.js <intervals (ms)> <times> <broker name>
