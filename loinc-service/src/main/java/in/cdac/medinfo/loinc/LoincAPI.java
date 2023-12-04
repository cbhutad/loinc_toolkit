package in.cdac.medinfo.loinc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Cheenmaya Bhutad
 * This class is start of the application
 */

@SpringBootApplication(scanBasePackages = {"in.cdac.medinfo.loinc"})
public class LoincAPI extends SpringBootServletInitializer {
    
    public static void main(String[] args) {
        SpringApplication.run(LoincAPI.class, args);
    }

    //This method provides a way to deploy the spring boot application via traditional WAR deployment
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(LoincAPI.class);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("*");
            }
        };
    }
}
