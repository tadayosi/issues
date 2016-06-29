package com.redhat.issues.hawtio.rbac;

import org.junit.Test;

import javax.management.MBeanInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;
import javax.management.ObjectName;
import javax.management.openmbean.CompositeData;
import javax.management.openmbean.TabularData;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertTrue;

public class RbacActiveMQTest {

    private static final String JMX_SERVICE_URL = "service:jmx:rmi://localhost:44444/jndi/rmi://localhost:1099/karaf-root";
    private static final String BROKER_OBJECT_NAME = "org.apache.activemq:type=Broker,brokerName=amq-broker,destinationType=Queue,destinationName=TEST";
    //private static final String BROKER_OBJECT_NAME = "org.apache.activemq:brokerName=amq-broker,destinationName=TEST,destinationType=Queue,type=Broker";

    @Test
    public void browse_admin() throws Exception {
        String[] creds = new String[] { "admin", "admin" };
        assertTrue((boolean) invoke(creds,
                "org.apache.karaf:type=security,area=jmx,name=root", "canInvoke",
                new Object[] { BROKER_OBJECT_NAME, "browse" },
                new String[] { String.class.getName(), String.class.getName() }));
    }

    @Test
    public void browse_admin_bulkQuery() throws Exception {
        String[] creds = new String[] { "admin", "admin" };
        TabularData result = bulkCanInvoke(creds);
        for (Object value : result.values()) {
            CompositeData compData = (CompositeData) value;
            if (compData.get("Method").toString().startsWith("browse"))
                System.out.println(compData.values());
            assertTrue((Boolean) compData.get("CanInvoke"));
        }
    }

    @Test
    public void browse_viewer() throws Exception {
        String[] creds = new String[] { "viewer", "viewer" };
        assertTrue((boolean) invoke(creds,
                "org.apache.karaf:type=security,area=jmx,name=root", "canInvoke",
                new Object[] { BROKER_OBJECT_NAME, "browse" },
                new String[] { String.class.getName(), String.class.getName() }));
    }

    @Test
    public void browse_viewer_bulkQuery() throws Exception {
        String[] creds = new String[] { "viewer", "viewer" };
        TabularData result = bulkCanInvoke(creds);
        for (Object value : result.values()) {
            CompositeData compData = (CompositeData) value;
            if (compData.get("Method").toString().startsWith("browse")) {
                System.out.println(compData.values());
                assertTrue((Boolean) compData.get("CanInvoke"));
            }
        }
    }

    private TabularData bulkCanInvoke(String[] creds) throws Exception {
        Map<String, List<String>> bulkQuery = bulkQuery(creds, BROKER_OBJECT_NAME);
        return (TabularData) invoke(creds,
                "org.apache.karaf:type=security,area=jmx,name=root", "canInvoke",
                new Object[] { bulkQuery },
                new String[] { Map.class.getName() });
    }

    private Map<String, List<String>> bulkQuery(String[] creds, String objectName) throws Exception {
        Map<String, List<String>> bulkQuery = new HashMap<>();
        bulkQuery.put(objectName, new ArrayList<String>());
        MBeanInfo mBeanInfo = mBeanInfo(creds, objectName);
        for (MBeanOperationInfo opInfo : mBeanInfo.getOperations()) {
            String args = "";
            for (MBeanParameterInfo paramInfo : opInfo.getSignature()) {
                if (!"".equals(args)) args += ",";
                args += paramInfo.getType();
            }
            String op = String.format("%s(%s)", opInfo.getName(), args);
            bulkQuery.get(objectName).add(op);

            if (op.startsWith("browse"))
                System.out.println(op);
        }
        return bulkQuery;
    }

    private MBeanInfo mBeanInfo(String[] creds, String objectName) throws Exception {
        Map<String, Object> env = new HashMap<>();
        env.put(JMXConnector.CREDENTIALS, creds);
        JMXConnector connector = null;
        try {
            connector = JMXConnectorFactory.newJMXConnector(new JMXServiceURL(JMX_SERVICE_URL), env);
            connector.connect();

            ObjectName name = new ObjectName(objectName);
            return connector.getMBeanServerConnection().getMBeanInfo(new ObjectName(objectName));
        } finally {
            if (connector != null) connector.close();
        }
    }

    private Object invoke(String[] creds, String objectName, String method, Object[] args, String[] types) throws Exception {
        Map<String, Object> env = new HashMap<>();
        env.put(JMXConnector.CREDENTIALS, creds);
        JMXConnector connector = null;
        try {
            connector = JMXConnectorFactory.newJMXConnector(new JMXServiceURL(JMX_SERVICE_URL), env);
            connector.connect();

            ObjectName name = new ObjectName(objectName);
            return connector.getMBeanServerConnection().invoke(name, method, args, types);
        } finally {
            if (connector != null) connector.close();
        }
    }

}
