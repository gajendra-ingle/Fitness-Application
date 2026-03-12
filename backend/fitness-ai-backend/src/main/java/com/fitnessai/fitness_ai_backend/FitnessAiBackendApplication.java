package com.fitnessai.fitness_ai_backend;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(
                title = "FitnessAI API",
                version = "1.0",
                description = "AI-Powered Fitness Application REST API",
                contact = @Contact(name = "FitnessAI Team", email = "support@fitnessai.com")
        )
)
public class FitnessAiBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(FitnessAiBackendApplication.class, args);
    }

}
