package blackjack.backend;

import blackjack.backend.domain.GameSummary;
import blackjack.backend.domain.Player;

import blackjack.backend.service.CascadingService;
import org.junit.jupiter.api.*;


import static org.junit.jupiter.api.Assertions.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest

//@DataMongoTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//@AutoConfigureDataMongo
public class GameSummaryCrudTest {

    @Autowired
    CascadingService service;

//    @Autowired
//    GameSummaryRepository summaries;
//    @Autowired
//    PlayerRepository players;



    @BeforeAll
    public void Init()
    {
        service.removeAllPlayers();
        service.removeAllPSummaries();
    }

    @Test
    @Order(1)
    public void crud() {

        for (int i = 0; i < 50; i++) {
            Player player = new Player(Integer.toString(i), i, i);

            player = service.addPlayer(player);
            GameSummary summary;
            for (int j = 0; j < 20; j++) {
                summary = new GameSummary(player, i * 20 + j, 1000 + i * 20 + j, 2000 + i * 20 + j);

                summary = service.addSummary(summary);

                assertEquals(service.countGameSummaries(),  j + 1);
                assertTrue(service.summaryExistsById(summary.getUid()));
            }

            service.deletePlayer(player.getUid());
            assertEquals(0, service.countGameSummaries());
        }
    }

}
