package com.redhat.issues.camel.cxf;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService
public interface GreetingService {
    @WebMethod
    String hello(@WebParam(name = "name") String name);
}
