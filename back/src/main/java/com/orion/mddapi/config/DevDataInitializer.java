package com.orion.mddapi.config;

import com.orion.mddapi.entities.User;
import com.orion.mddapi.entities.Article;
import com.orion.mddapi.entities.Comment;
import com.orion.mddapi.entities.Subscription;
import com.orion.mddapi.entities.Theme;
import com.orion.mddapi.repositories.UserRepository;
import com.orion.mddapi.repositories.ArticleRepository;
import com.orion.mddapi.repositories.CommentRepository;
import com.orion.mddapi.repositories.SubscriptionRepository;
import com.orion.mddapi.repositories.ThemeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
@Order(2)
public class DevDataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final ThemeRepository themeRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public DevDataInitializer(UserRepository userRepository,
            ArticleRepository articleRepository,
            CommentRepository commentRepository,
            SubscriptionRepository subscriptionRepository,
            ThemeRepository themeRepository) {
        this.userRepository = userRepository;
        this.articleRepository = articleRepository;
        this.commentRepository = commentRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.themeRepository = themeRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {

            // --- Utilisateurs ---
            User alice = new User();
            alice.setUsername("alice");
            alice.setEmail("alice@mail.com");
            alice.setPassword(passwordEncoder.encode("Password1!"));
            userRepository.save(alice);

            User bob = new User();
            bob.setUsername("bob");
            bob.setEmail("bob@mail.com");
            bob.setPassword(passwordEncoder.encode("Password1!"));
            userRepository.save(bob);

            // --- Thèmes existants ---
            Theme javascript = themeRepository.findAll().get(0);
            Theme java = themeRepository.findAll().get(1);

            // --- Articles ---
            Article article1 = new Article();
            article1.setTitle("Découvrir les closures en JavaScript");
            article1.setContent("Une closure permet à une fonction d'accéder à des variables de son contexte...");
            article1.setAuthor(alice);
            article1.setTheme(javascript);
            articleRepository.save(article1);

            Article article2 = new Article();
            article2.setTitle("Introduction aux streams en Java");
            article2.setContent("Les streams permettent de traiter des collections de manière déclarative...");
            article2.setAuthor(bob);
            article2.setTheme(java);
            articleRepository.save(article2);

            // --- Commentaires ---
            Comment comment1 = new Comment();
            comment1.setContent("Super clair, merci !");
            comment1.setAuthor(bob);
            comment1.setArticle(article1);
            commentRepository.save(comment1);

            Comment comment2 = new Comment();
            comment2.setContent("Très bon rappel sur les streams.");
            comment2.setAuthor(alice);
            comment2.setArticle(article2);
            commentRepository.save(comment2);

            // --- Abonnement ---
            Subscription subscription1 = new Subscription();
            subscription1.setUser(alice);
            subscription1.setTheme(javascript);
            subscriptionRepository.save(subscription1);

            Subscription subscription2 = new Subscription();
            subscription2.setUser(alice);
            subscription2.setTheme(java);
            subscriptionRepository.save(subscription2);
        }
    }
}