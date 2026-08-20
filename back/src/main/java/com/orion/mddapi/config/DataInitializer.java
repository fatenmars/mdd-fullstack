package com.orion.mddapi.config;

import com.orion.mddapi.entities.Theme;
import com.orion.mddapi.repositories.ThemeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class DataInitializer implements CommandLineRunner {

    private final ThemeRepository themeRepository;

    public DataInitializer(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (themeRepository.count() == 0) {

            Theme javascript = new Theme();
            javascript.setTitle("JavaScript");
            javascript.setDescription("Le langage du web, côté navigateur et serveur.");
            themeRepository.save(javascript);

            Theme java = new Theme();
            java.setTitle("Java");
            java.setDescription("Langage orienté objet robuste, très utilisé côté back-end.");
            themeRepository.save(java);

            Theme python = new Theme();
            python.setTitle("Python");
            python.setDescription("Langage polyvalent, populaire en data science et scripting.");
            themeRepository.save(python);

            Theme angular = new Theme();
            angular.setTitle("Angular");
            angular.setDescription("Framework front-end TypeScript pour applications web.");
            themeRepository.save(angular);

            Theme spring = new Theme();
            spring.setTitle("Spring");
            spring.setDescription("Framework Java de référence pour le développement back-end.");
            themeRepository.save(spring);
        }
    }
}