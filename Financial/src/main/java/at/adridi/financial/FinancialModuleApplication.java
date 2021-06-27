package at.adridi.financial;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * Budgeting (Expenses and Earnings service) module part of generalmanagement
 * microservices app architecture.
 *
 * @author A.Dridi
 */
@SpringBootApplication
@EnableEurekaClient
public class FinancialModuleApplication extends SpringBootServletInitializer {
    
    public static void main(String[] args) {
        SpringApplication.run(FinancialModuleApplication.class, args);
    }


}
