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
        Player player = new Player(Integer.toString(1), 2, 3);
        GameSummary summary = new GameSummary(player, 12, 13, 14);

        service.addPlayer(player);
        summary = service.addSummary(summary);

        Player player1 = new Player(Integer.toString(11), 12, 13);
        player1 = service.addPlayer(player1);
        GameSummary summary1 = new GameSummary(player1, 12, 13, 14);
        service.updateSummary(summary.getUid(), summary1);

        assertTrue(service.getPlayer(player.getUid()).isPresent());
        assertEquals(service.getPlayer(player.getUid()).get().getGames().size(), 0);

        assertTrue(service.getPlayer(player1.getUid()).isPresent());


        service.removeAllPlayers();
        service.removeAllPSummaries();


    }

}
