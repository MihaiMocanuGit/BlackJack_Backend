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
        if (repository.count() == 0)
            return args -> {
                log.info("Preloading " + repository.save(new Player("CrawWakefield", 0, 0.0f)));
                log.info("Preloading " + repository.save(new Player("VizgigKitty", 50, 0.5f)));
                log.info("Preloading " + repository.save(new Player("WobblerBetroth", 100, 1.0f)));
                log.info("Preloading " + repository.save(new Player("HoddypeakMargaretta", 150, 1.5f)));
                log.info("Preloading " + repository.save(new Player("SkitterwaysJoe", 200, 2.0f)));
                log.info("Preloading " + repository.save(new Player("BelleBanshee", 250, 2.5f)));
                log.info("Preloading " + repository.save(new Player("VorerightGowan", 300, 3.0f)));
                log.info("Preloading " + repository.save(new Player("StandvurderMacklin", 350, 3.5f)));
                log.info("Preloading " + repository.save(new Player("ErstwhileDodson", 400, 4.0f)));
                log.info("Preloading " + repository.save(new Player("BrishMeg", 450, 4.5f)));
                log.info("Preloading " + repository.save(new Player("BonneyChuckaboo", 500, 5.0f)));
                log.info("Preloading " + repository.save(new Player("BanghamWhistersniff", 550, 5.5f)));
                log.info("Preloading " + repository.save(new Player("GargalesisMagwitch", 600, 6.0f)));
                log.info("Preloading " + repository.save(new Player("SeptimusCankerd", 650, 6.5f)));
                log.info("Preloading " + repository.save(new Player("SnoozlePirrip", 700, 7.0f)));
                log.info("Preloading " + repository.save(new Player("PeepyCrambo", 750, 7.5f)));
                log.info("Preloading " + repository.save(new Player("SkidaddleArtful", 800, 8.0f)));
                log.info("Preloading " + repository.save(new Player("SlackbridgeShroke", 850, 8.5f)));
                log.info("Preloading " + repository.save(new Player("DeliciateTox", 900, 9.0f)));
                log.info("Preloading " + repository.save(new Player("PeltirogusThither", 950, 9.5f)));
                log.info("Preloading " + repository.save(new Player("TootleNammet", 1000, 10.0f)));
            };
        else
            return args -> { log.info("Database is not empty, skipping initialization.");};
    }
}
