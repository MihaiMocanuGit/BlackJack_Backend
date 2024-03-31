package blackjack.backend.configuration;

import blackjack.backend.domain.Player;
import blackjack.backend.repository.PlayerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class LoadDatabase {
    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(PlayerRepository repository) {

        return args -> {
            log.info("Preloading " + repository.save(new Player("Bilbo Baggins", "burglar")));
            log.info("Preloading " + repository.save(new Player("Frodo Baggins", "thief")));
        };
    }
}
