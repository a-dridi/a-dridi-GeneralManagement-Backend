package at.adridi.generalmanagement.media;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * Media service. Music, Video, Video clips and software, etc. All types of media except books. 
 * 
 * @author A.Dridi
 */
@SpringBootApplication
public class MediaModuleApplication extends SpringBootServletInitializer{
        public static void main(String[] args) {
        SpringApplication.run(MediaModuleApplication.class, args);
    }
}
