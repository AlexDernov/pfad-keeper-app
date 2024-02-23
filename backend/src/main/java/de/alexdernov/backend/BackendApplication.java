package de.alexdernov.backend;

import com.cloudinary.Cloudinary;
import de.alexdernov.backend.security.SecurityConfig;
import de.alexdernov.backend.services.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }
    @Bean
    public Cloudinary cloudinaryBean(){
        return new Cloudinary();
    }

}
