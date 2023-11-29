package in.cdac.medinfo.loinc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class LoincAPI extends SpringBootServletInitializer {
    
    public static void main(String[] args) {
        SpringApplication.run(LoincAPI.class, args);
    }

    //This method provides a way to deploy the spring boot application via traditional WAR deployment
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(LoincAPI.class);
    }

}
