package blackjack.backend.DatabaseConfiguration;

import blackjack.backend.repository.GameSummaryRepository;
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
    CommandLineRunner initDatabase(PlayerRepository playerRepository, GameSummaryRepository gameSummaryRepository) {
        if (playerRepository.count() == 0)
            return args -> {
//                log.info("Preloading " + playerRepository.save(new Player("CrawWakefield", 0, 0.0f)));
//                log.info("Preloading " + playerRepository.save(new Player("VizgigKitty", 50, 0.5f)));
//                log.info("Preloading " + playerRepository.save(new Player("WobblerBetroth", 100, 1.0f)));
//                log.info("Preloading " + playerRepository.save(new Player("HoddypeakMargaretta", 150, 1.5f)));
//                log.info("Preloading " + playerRepository.save(new Player("SkitterwaysJoe", 200, 2.0f)));
//                log.info("Preloading " + playerRepository.save(new Player("BelleBanshee", 250, 2.5f)));
//                log.info("Preloading " + playerRepository.save(new Player("VorerightGowan", 300, 3.0f)));
//                log.info("Preloading " + playerRepository.save(new Player("StandvurderMacklin", 350, 3.5f)));
//                log.info("Preloading " + playerRepository.save(new Player("ErstwhileDodson", 400, 4.0f)));
//                log.info("Preloading " + playerRepository.save(new Player("BrishMeg", 450, 4.5f)));
//                log.info("Preloading " + playerRepository.save(new Player("BonneyChuckaboo", 500, 5.0f)));
//                log.info("Preloading " + playerRepository.save(new Player("BanghamWhistersniff", 550, 5.5f)));
//                log.info("Preloading " + playerRepository.save(new Player("GargalesisMagwitch", 600, 6.0f)));
//                log.info("Preloading " + playerRepository.save(new Player("SeptimusCankerd", 650, 6.5f)));
//                log.info("Preloading " + playerRepository.save(new Player("SnoozlePirrip", 700, 7.0f)));
//                log.info("Preloading " + playerRepository.save(new Player("PeepyCrambo", 750, 7.5f)));
//                log.info("Preloading " + playerRepository.save(new Player("SkidaddleArtful", 800, 8.0f)));
//                log.info("Preloading " + playerRepository.save(new Player("SlackbridgeShroke", 850, 8.5f)));
//                log.info("Preloading " + playerRepository.save(new Player("DeliciateTox", 900, 9.0f)));
//                log.info("Preloading " + playerRepository.save(new Player("PeltirogusThither", 950, 9.5f)));
//                log.info("Preloading " + playerRepository.save(new Player("TootleNammet", 1000, 10.0f)));

                Faker faker = new Faker("data/usernames.txt");
                faker.init();
                for (int i = 0; i < 50; i++)
                {
                    Faker.FakerTuple result = faker.getFakePlayerWithSummaries();
                    log.info("Preloading " + playerRepository.save(result.player));

                    log.info("Preloading for previous player" + gameSummaryRepository.saveAll(result.summaries));
                }
            };
        else
            return args -> { log.info("Database is not empty, skipping initialization.");};
    }
}
