/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.appserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * General Management App Server (Eureka Server) used for modules of this
 * microservices architecture.
 *
 * @author A.Dridi
 */
@SpringBootApplication
@EnableEurekaServer
public class GeneralManagementAppServer {

    public static void main(String[] args) {
        SpringApplication.run(GeneralManagementAppServer.class, args);

    }

}
