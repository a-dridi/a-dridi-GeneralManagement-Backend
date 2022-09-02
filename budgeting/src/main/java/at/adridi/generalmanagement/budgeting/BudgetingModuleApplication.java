package at.adridi.generalmanagement.budgeting;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.TimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Configuration;

/**
 * Budgeting (Expenses and Earnings service) module part of generalmanagement
 * microservices app architecture.
 *
 * @author A.Dridi
 */
@SpringBootApplication
@Configuration
@EnableEurekaClient
public class BudgetingModuleApplication extends SpringBootServletInitializer {
    
    public static void main(String[] args) {
        SpringApplication.run(BudgetingModuleApplication.class, args);
    }
    
    @Autowired
    public void configureJackson(ObjectMapper objectMapper) {
        objectMapper.setTimeZone(TimeZone.getTimeZone("Etc/UTC"));
    }


}
