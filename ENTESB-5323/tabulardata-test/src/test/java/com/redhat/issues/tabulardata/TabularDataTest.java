package com.redhat.issues.tabulardata;

import static org.apache.karaf.management.JMXSecurityMBean.*;

import javax.management.openmbean.CompositeDataSupport;
import javax.management.openmbean.KeyAlreadyExistsException;
import javax.management.openmbean.TabularData;
import javax.management.openmbean.TabularDataSupport;

import org.junit.Test;

public class TabularDataTest {

    @Test(expected = KeyAlreadyExistsException.class)
    public void put() throws Exception {
        String objectName = "foo.bar.testing:type=SomeMBean";
        String method1 = "testMethod1(java.lang.String)";
        String method2 = "testMethod2(java.lang.String)";
        TabularData table = new TabularDataSupport(CAN_INVOKE_TABULAR_TYPE);
        table.put(new CompositeDataSupport(CAN_INVOKE_RESULT_ROW_TYPE, CAN_INVOKE_RESULT_COLUMNS,
                new Object[] { objectName, method1, true }));
        table.put(new CompositeDataSupport(CAN_INVOKE_RESULT_ROW_TYPE, CAN_INVOKE_RESULT_COLUMNS,
                new Object[] { objectName, method2, false }));
        table.put(new CompositeDataSupport(CAN_INVOKE_RESULT_ROW_TYPE, CAN_INVOKE_RESULT_COLUMNS,
                new Object[] { objectName, method1, false }));
    }

}
