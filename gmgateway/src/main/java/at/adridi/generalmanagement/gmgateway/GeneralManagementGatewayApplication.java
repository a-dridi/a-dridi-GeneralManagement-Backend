/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.gmgateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * API entry point, authentication and user management for general management microservices app. The gateway that
 * connects with the authentication service and other modules (budgeting, etc.).
 * This is used to access all REST API end points of all different modules. 
 * The settings for the REST API mapping is located in the resources file "application.properties".
 * Authentication und management (add, delete or update user) of users is done here as well. 
 *
 * @author A.Dridi
 */
@SpringBootApplication
@EnableEurekaClient
@EnableZuulProxy		
public class GeneralManagementGatewayApplication {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(GeneralManagementGatewayApplication.class, args);
    }

}
