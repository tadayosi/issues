package com.redhat.issues.cxf.jms;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService
public interface GreetingService {
    @WebMethod
    String hello(@WebParam(name = "name") String name);
}
