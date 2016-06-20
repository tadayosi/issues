package com.redhat.issues.jetty;

import org.eclipse.jetty.jmx.MBeanContainer;
import org.eclipse.jetty.server.Server;
import org.junit.Test;

import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ServerJMXTest {

    private MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();

    @Test
    public void jettyJMX() throws Exception {
        Server server = new Server(18080);
        for (Object bean : server.getBeans()) {
            System.out.println(bean);
        }
        MBeanContainer mBeanContainer = new MBeanContainer(mBeanServer);
        server.addEventListener(mBeanContainer);
        server.addBean(mBeanContainer);
        try {
            server.start();
            List<ObjectInstance> mbeans = queryMBeans();
            assertThat(mbeans.isEmpty()).isFalse();
        } finally {
            server.stop();
            server.removeBean(mBeanContainer);
            //server.destroy();
        }
        List<ObjectInstance> mbeans = queryMBeans();
        assertThat(mbeans.isEmpty()).isTrue();
    }

    private List<ObjectInstance> queryMBeans() throws MalformedObjectNameException {
        List<ObjectInstance> mbeans = new ArrayList<>(
                mBeanServer.queryMBeans(new ObjectName("org.eclipse.jetty.*:*"), null));
        Collections.sort(mbeans, new Comparator<ObjectInstance>() {
            public int compare(ObjectInstance o1, ObjectInstance o2) {
                return o1.getObjectName().getCanonicalName().compareTo(
                        o2.getObjectName().getCanonicalName());
            }
        });
        System.out.println("---");
        for (ObjectInstance mbean : mbeans) {
            System.out.println(mbean.getObjectName());
        }
        System.out.println("---");
        return mbeans;
    }

}
