package com.redhat.issues.hawtio;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MemoryTest {

    private static final Logger LOG = LoggerFactory.getLogger(MemoryTest.class);

    private static final String HAWTIO_URL = "http://localhost:8181/hawtio";
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "admin";

    private RemoteWebDriver driver;

    @Before
    public void setUp() throws Exception {
        System.setProperty("webdriver.chrome.driver", "/usr/local/bin/chromedriver");
        driver = new ChromeDriver();
        //driver = new FirefoxDriver();
        driver.get(HAWTIO_URL + "/login");
        sleep(5);
        driver.findElement(By.id("username")).sendKeys(USERNAME);
        driver.findElement(By.id("password")).sendKeys(PASSWORD);
        driver.findElement(By.tagName("button")).click();

        LOG.info("Test starts in 10 secs");
        for (int i = 0; i < 10; i++) {
            System.out.print(".");
            sleep(1);
        }
        System.out.println();
    }

    //@After
    public void tearDown() {
        LOG.info("Test ends in 5 mins");
        sleep(5 * 60);
        driver.quit();
    }

    @Test
    public void run() throws Exception {
        int round = 4 * 60;
        //int round = 1;
        for (int i = 1; i <= round; i++) {
            LOG.info("Round {} / {}", i, round);
            driver.get(HAWTIO_URL + "/#/jmx/attributes");
            sleep(5);
            driver.get(HAWTIO_URL + "/#/logs");
            sleep(5);
            driver.get(HAWTIO_URL + "/#/osgi/bundles");
            sleep(5);
        }
    }

    private void sleep(int sec) {
        try {
            Thread.sleep(sec * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
