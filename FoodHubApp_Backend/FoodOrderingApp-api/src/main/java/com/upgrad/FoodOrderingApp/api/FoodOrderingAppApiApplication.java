package com.swaraj.FoodHubApp.api;  

import com.swaraj.FoodHubApp.service.ServiceConfiguration;  
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

/**
 * A Configuration class that can declare one or more @Bean methods and trigger auto-configuration and component scanning.
 * This class launches a Spring Application from Java main method.
 * 
 * Author: Swaraj Das
 * Email: swarajdas540@gmail.com
 * Project: FoodHubApp Backend
 * Description: This is the main entry point for the FoodHubApp backend application. It initializes the Spring Boot application
 * and imports necessary configurations for the service layer.
 */
@SpringBootApplication
@Import(ServiceConfiguration.class)
public class FoodHubAppApiApplication {  
    public static void main(String[] args) {
        SpringApplication.run(FoodHubAppApiApplication.class, args);  // Running your project
    }
}
