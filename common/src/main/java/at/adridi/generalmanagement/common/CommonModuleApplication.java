package at.adridi.generalmanagement.common;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Common features for the whole application. E.g.: application settings, database
 * notes.
 *
 * @author A.Dridi
 */
@SpringBootApplication
public class CommonModuleApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(CommonModuleApplication.class, args);
    }

}
