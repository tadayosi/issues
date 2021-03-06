/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package demo.jaxrs.server;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

import java.util.HashMap;
import java.util.Map;

import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.lifecycle.SingletonResourceProvider;
import org.apache.cxf.jaxrs.provider.json.JSONProvider;

public class Server {

    protected Server() throws Exception {
        JAXRSServerFactoryBean sf = new JAXRSServerFactoryBean();
        sf.setResourceClasses(CustomerService.class);
        sf.setResourceProvider(CustomerService.class, 
            new SingletonResourceProvider(new CustomerService()));
        sf.setAddress("http://localhost:9000/");
        JSONProvider<Customer> p = new JSONProvider<>();
        //p.setSupportUnwrapped(true);
        //p.setIgnoreNamespaces(true);
        //p.setUnmarshallAsJaxbElement(true);
        Map<String, String> ns = new HashMap<>();
        ns.put("http://rest.fabric.quickstarts.fabric8.io/", "ns1");
        p.setNamespaceMap(ns);
        sf.setProvider(p);
        sf.setProvider(new JacksonJsonProvider());

        sf.create();
    }

    public static void main(String args[]) throws Exception {
        new Server();
        System.out.println("Server ready...");

        Thread.sleep(5 * 6000 * 1000);
        System.out.println("Server exiting");
        System.exit(0);
    }
}
