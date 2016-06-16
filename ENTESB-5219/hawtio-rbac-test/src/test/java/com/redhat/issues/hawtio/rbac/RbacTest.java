package com.redhat.issues.hawtio.rbac;

import java.util.HashMap;
import java.util.Map;

import javax.management.ObjectName;
import javax.management.openmbean.CompositeDataSupport;
import javax.management.openmbean.CompositeType;
import javax.management.openmbean.OpenType;
import javax.management.openmbean.SimpleType;
import javax.management.openmbean.TabularData;
import javax.management.openmbean.TabularDataSupport;
import javax.management.openmbean.TabularType;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.junit.Test;

public class RbacTest {

    private static final String JMX_SERVICE_URL = "service:jmx:rmi://localhost:44444/jndi/rmi://localhost:1099/karaf-root";

    @Test
    public void configAdmin_admin() throws Exception {
        configAdminUpdate(new String[] { "admin", "admin" });
    }

    @Test(expected = SecurityException.class)
    public void configAdmin_viewer() throws Exception {
        configAdminUpdate(new String[] { "viewer", "viewer" });
    }

    private void configAdminUpdate(String[] creds) throws Exception {
        String[] keys = new String[] { "key", "value" };
        CompositeType compType = new CompositeType("testType", "Type for testing", keys, keys, new OpenType[] { SimpleType.STRING, SimpleType.STRING });
        TabularType type = new TabularType("test", "test", compType, keys);
        TabularDataSupport sample = new TabularDataSupport(type);
        sample.put(new CompositeDataSupport(type.getRowType(), keys, new String[] { "size", "888" }));

        invoke(creds, "hawtio:type=ConfigAdmin", "configAdminUpdate",
                new Object[] { "org.apache.karaf.log", sample },
                new String[] { String.class.getName(), TabularData.class.getName() });
    }

    @Test
    public void gc_admin() throws Exception {
        gc(new String[] { "admin", "admin" });
    }

    @Test(expected = SecurityException.class)
    public void gc_viewer() throws Exception {
        gc(new String[] { "viewer", "viewer" });
    }

    private void gc(String[] creds) throws Exception {
        invoke(creds, "java.lang:type=Memory", "gc", null, null);
    }

    private void invoke(String[] creds, String objectName, String method, Object[] args, String[] types) throws Exception {
        Map<String, Object> env = new HashMap<>();
        env.put(JMXConnector.CREDENTIALS, creds);
        JMXConnector connector = null;
        try {
            connector = JMXConnectorFactory.newJMXConnector(new JMXServiceURL(JMX_SERVICE_URL), env);
            connector.connect();

            ObjectName name = new ObjectName(objectName);
            connector.getMBeanServerConnection().invoke(name, method, args, types);
        } finally {
            if (connector != null) connector.close();
        }
    }

}
