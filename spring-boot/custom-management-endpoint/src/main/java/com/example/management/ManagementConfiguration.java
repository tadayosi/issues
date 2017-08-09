package com.example.management;

import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.actuate.autoconfigure.ManagementContextConfiguration;
import org.springframework.boot.actuate.endpoint.mvc.AbstractMvcEndpoint;
import org.springframework.boot.actuate.endpoint.mvc.MvcEndpoint;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;

@ManagementContextConfiguration
public class ManagementConfiguration {

    @Bean
    public MvcEndpoint endpoint() {
        return new AbstractMvcEndpoint("/console", true) {
            @Override
            public void addResourceHandlers(ResourceHandlerRegistry registry) {
                registry
                    .addResourceHandler("/console/**")
                    .addResourceLocations("classpath:/console/");
            }

            @Override
            public void addViewControllers(ViewControllerRegistry registry) {
                registry
                    .addViewController("/console/")
                    .setViewName("forward:/console/index.html");
            }
        };
    }

    @Bean
    public ServletRegistrationBean servlet() {
        return new ServletRegistrationBean(new HttpServlet() {
            @Override
            protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
                resp.getWriter().println("This is a console servlet.");
            }
        }, "/console/servlet");
    }

}
