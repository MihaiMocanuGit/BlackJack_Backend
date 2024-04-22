package blackjack.backend;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import blackjack.backend.configuration.Faker;
import blackjack.backend.controller.PrimaryController;
import blackjack.backend.domain.Player;
import org.junit.jupiter.api.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.*;


import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;


@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BlackJackBackendApplicationTests {

    @LocalServerPort
    int randomServerPort;
    long actualSize;
    ArrayList<String> oldUids = new ArrayList<>();

    @Autowired
    PrimaryController controller;

    @Autowired
    private TestRestTemplate restTemplate;

    Faker faker = new Faker("data/usernames.txt");

//    @Test
//    void runTests() throws Exception {
//        contextLoads();
//        allBasic();
//        controller.all().forEach(model -> oldUids.add(Objects.requireNonNull(model.getContent()).getUid()));
//        newPlayer();
//        status();
//        all();
//        page();
//        one();
//        sortRepo();
//        replacePlayers();
//        deletePlayer();
//
//
//
//    }
    @Test
    @Order(1)
    void contextLoads() throws URISyntaxException {
        faker.init();
        assertThat(controller).isNotNull();

        List<Player> players =  controller.all().getContent().stream().map(EntityModel::getContent).toList();
        players.forEach(x -> {
            final String baseUrl = "http://localhost:" + randomServerPort + "/players/" + x.getUid();


            ResponseEntity<String> result = this.restTemplate.exchange(baseUrl, HttpMethod.DELETE, new HttpEntity<>(x.getUid()), String.class);
        });

        assertEquals(0, controller.size());
        actualSize = 0;


        for (int i = 0; i < 10; i++) {

            Player player = faker.getFakePlayer();
            final String baseUrl = "http://localhost:" + randomServerPort + "/players";
            URI uri = new URI(baseUrl);

            HttpHeaders headers = new HttpHeaders();
            // headers.set("Accept-Post", "true");

            HttpEntity<Player> request = new HttpEntity<>(player, headers);
            ResponseEntity<String> result = this.restTemplate.postForEntity(uri, request, String.class);
            actualSize++;
        }



    }

    @Test
    @Order(3)
    void newPlayer() throws Exception {
        controller.all().forEach(model -> oldUids.add(Objects.requireNonNull(model.getContent()).getUid()));

        //ArrayList<Player> players = new ArrayList<>();
        long oldSize = controller.size();
        this.actualSize = oldSize;
        for(int i = 0; i < 100; i++) {
            Player player = new Player("Johnny" + Integer.toString(i), i * 10, i * 1.125f);

            final String baseUrl = "http://localhost:" + randomServerPort + "/players";
            URI uri = new URI(baseUrl);

            HttpHeaders headers = new HttpHeaders();
            // headers.set("Accept-Post", "true");

            HttpEntity<Player> request = new HttpEntity<>(player, headers);

            ResponseEntity<String> result = this.restTemplate.postForEntity(uri, request, String.class);
            String id = result.getBody().substring("{\"uid\":\"".length(), result.getBody().indexOf( "\",\"username\":\"Johnny"));
            //Verify request succeed
            assertEquals(201, result.getStatusCodeValue());
            String body = "{\"uid\":\"" + id +
                    "\",\"username\":\"Johnny" + Integer.toString(i) +
                    "\",\"bank\":" + i * 10.0f +
                    ",\"level\":" + i * 1.125f +
                    ",\"games\":[]" +
                    ",\"_links\":{\"self\":{\"href\":\"http://localhost:8080/players/" + id +
                    "\"},\"player\":{\"href\":\"http://localhost:8080/players\"}}}";
            assertEquals( body, result.getBody());
        }
        assertEquals(100, controller.size() - oldSize);
        this.actualSize = 100 + oldSize;
    }
    @Test
    @Order(4)
    void status() {
        assertEquals(controller.status(), 1);
    }

    @Test
    @Order(2)
    void allBasic()
    {
        assertEquals(controller.all().getContent().size(), controller.size());
    }

    @Test
    @Order(5)
    void all() {
        CollectionModel<EntityModel<Player>> response = controller.all();
        Collection<EntityModel<Player>> list = response.getContent();

        for (int i = 0; i < 100; i++)
        {
            Player player = new Player("Johnny" + Integer.toString(i), i * 10, i * 1.125f);
            assertTrue(list.stream().map(EntityModel::getContent).anyMatch(x -> x.getLevel() == player.getLevel() &&
                                                    Objects.equals(x.getUsername(), player.getUsername()) &&
                                                     x.getBank() == player.getBank()));
        }
        assertEquals(actualSize, list.size());
    }

    @Test
    @Order(6)
    void page() {
        for (long i = 1; i <= actualSize; i+= 3)
        {
            CollectionModel<EntityModel<Player>> response = controller.page(0L, i);
            Collection<EntityModel<Player>> list = response.getContent();
            assertEquals(i, list.size());

            for (long j = 0; j < actualSize / i; j+=3)
            {
                CollectionModel<EntityModel<Player>> responseInner = controller.page(j, i);
                Collection<EntityModel<Player>> listInner = response.getContent();
                assertEquals(i, listInner.size());
            }
        }



    }

    @Test
    @Order(7)
    void one() {
        for (int i = 0; i < oldUids.size(); i++) {
           String uid = Objects.requireNonNull(controller.one(oldUids.get(i)).getContent()).getUid();
           assertEquals(oldUids.get(i), uid);
        }
    }


    @Test
    @Order(8)
    void sortRepo() {
        CollectionModel<EntityModel<Player>> response = controller.sortRepo(Boolean.toString(false));
        Collection<EntityModel<Player>> list = response.getContent();
        Object[] players = list.stream().map(EntityModel::getContent).toArray();
        for (int i = 0; i < players.length - 1; i++)
        {
            Player player1 = (Player) players[i];
            Player player2 = (Player) players[i + 1];

            assertTrue(player1.getLevel() <= player2.getLevel());
        }

    }

    @Test
    @Order(9)
    void replacePlayers() throws URISyntaxException {
        for (int i = 0; i < oldUids.size(); i++) {
            String uid = Objects.requireNonNull(controller.one(oldUids.get(i)).getContent()).getUid();
            Player player = new Player("Johnny" + Integer.toString(-i), i * 10, i * 1.125f);

            final String baseUrl = "http://localhost:" + randomServerPort + "/players/" + uid;


            ResponseEntity<String> result = this.restTemplate.exchange(baseUrl, HttpMethod.PUT, new HttpEntity<>(player), String.class);
            String id = result.getBody().substring("{\"uid\":\"".length(), result.getBody().indexOf( "\",\"username\":"));
            assertEquals(201, result.getStatusCodeValue());
            String body = "{\"uid\":\"" + id +
                    "\",\"username\":\"Johnny" + -i +
                    "\",\"bank\":" + i * 10.0f +
                    ",\"level\":" + i * 1.125f +
                    ",\"games\":[]" +
                    ",\"_links\":{\"self\":{\"href\":\"http://localhost:8080/players/" + id +
                    "\"},\"player\":{\"href\":\"http://localhost:8080/players\"}}}";
            assertEquals( body, result.getBody());
        }
    }

    @Test
    @Order(10)
    void deletePlayer() {
         List<Player> players =  controller.all().getContent().stream().map(EntityModel::getContent).toList();
         players.forEach(x -> {
             final String baseUrl = "http://localhost:" + randomServerPort + "/players/" + x.getUid();


             ResponseEntity<String> result = this.restTemplate.exchange(baseUrl, HttpMethod.DELETE, new HttpEntity<>(x.getUid()), String.class);

             assertEquals(actualSize - 1, controller.size());
             actualSize--;
         });

         assertEquals(0, controller.size());
    }


}
