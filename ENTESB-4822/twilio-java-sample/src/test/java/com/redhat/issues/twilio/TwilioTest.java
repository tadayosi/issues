package com.redhat.issues.twilio;

import com.twilio.Twilio;
import com.twilio.base.ResourceSet;
import com.twilio.rest.api.v2010.Account;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.eclipse.jetty.server.Server;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TwilioTest extends TwilioTestBase {

    private static final String ACCOUNT_SID = System.getProperty("twilio.account.sid");
    private static final String AUTH_TOKEN = System.getProperty("twilio.auth.token");

    private static final String FROM = System.getProperty("twilio.from");
    private static final String TO = System.getProperty("twilio.to");

    @BeforeClass
    public static void initTwilio() {
        assertThat(ACCOUNT_SID).isNotEmpty();
        assertThat(AUTH_TOKEN).isNotEmpty();
        assertThat(FROM).isNotEmpty();
        assertThat(TO).isNotEmpty();
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    }

    // *** Account *****************************************

    @Test
    public void fetchAccount() {
        Account account = Account.fetcher(ACCOUNT_SID).fetch();
        assertThat(account).isNotNull();
        assertThat(account.getSid()).isEqualTo(ACCOUNT_SID);
        assertThat(account.getAuthToken()).isEqualTo(AUTH_TOKEN);
        System.out.println(account);
    }

    @Test
    public void readAccounts() {
        ResourceSet<Account> accounts = Account.reader().read();
        assertThat(accounts).isNotEmpty();
        for (Account account : accounts) {
            System.out.println(account);
        }
    }

    // *** Message *****************************************

    @Test
    @Ignore("Charged")
    public void sendSMS() {
        Message message = Message
            .creator(
                new PhoneNumber(TO), new PhoneNumber(FROM),
                String.format("Hello from %s", getClass().getSimpleName()))
            .create();
        assertThat(message).isNotNull();
        System.out.println(message);
    }

    @Test
    @Ignore("Charged")
    public void sendMMS() {
        Message message = Message
            .creator(
                new PhoneNumber(TO), new PhoneNumber(FROM),
                String.format("Hello from %s", getClass().getSimpleName()))
            .setMediaUrl("https://static0.twilio.com/resources/logos/twilio-logo-circle-50x50.png")
            .create();
        assertThat(message).isNotNull();
        System.out.println(message);
    }

    @Test
    //@Ignore("Charged")
    public void receiveSMS() throws Exception {
        Server server = new Server(SERVER_PORT);
        try {
            start(server);
            Thread.sleep(30 * 1000);
            /*
            Message message = Message
                .creator(
                    new PhoneNumber(TO), new PhoneNumber(FROM),
                    String.format("Hello from %s", getClass().getSimpleName()))
                .create();
            assertThat(message).isNotNull();
            System.out.println(message);
            */
        } finally {
            stop(server);
        }
    }

    // * Call a user and text-2-speech them a notification
    // * Handle a phone call and transcribe a recorded message to a message
    // * Call 2 different users and join them in a conference room

    @Test
    public void voice() {
        assertThat(1 + 1).isEqualTo(2);
    }

    @Test
    public void voip() {
        assertThat(1 + 1).isEqualTo(2);
    }

    @Test
    public void taskrouter() {
        assertThat(1 + 1).isEqualTo(2);
    }

}
