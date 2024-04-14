package blackjack.backend.controller;


import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import blackjack.backend.assemblers.PlayerModelAssembler;
import blackjack.backend.configuration.Faker;
import blackjack.backend.domain.Player;
import blackjack.backend.exceptions.PlayerNotFoundException;
import blackjack.backend.repository.PlayerRepository;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@RestController
public class PlayerController {
    private final String origin = "http://localhost:3000";
    private PlayerRepository repository;
    private final PlayerModelAssembler assembler;

    private boolean reversed;



    private int sortRepoByLevelCondition(Player player1, Player player2)
    {
        if (player1.getLevel() == player2.getLevel())
            return 0;
        else if (player1.getLevel() < player2.getLevel() ^ reversed)
            return -1;
        else
            return 1;

    }
    private List<Player> repoSortToList()
    {
        return repository.findAll().stream().sorted((p1, p2) ->
                {
                    if (p1.getLevel() == p2.getLevel())
                        return 0;
                    else if (p1.getLevel() < p2.getLevel() ^ reversed)
                        return -1;
                    else
                        return 1;
                }).toList();
    }


    PlayerController(PlayerRepository repository, PlayerModelAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;

        reversed = false;
    }

    @CrossOrigin(origins = origin)
    @PostMapping("/players")
    ResponseEntity<?> newPlayer(@RequestBody Player newPlayer) {

        EntityModel<Player> entityModel = assembler.toModel(repository.save(newPlayer));

        return ResponseEntity //
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()) //
                .body(entityModel);
    }

    @CrossOrigin(origins = origin)
    @PostMapping("/fakers")
    void newPlayer() {
        Faker faker = new Faker("data/usernames.txt");
        faker.init();

        for (long i = 0; i < 100; i++)
        {
            repository.save(faker.getFakePlayer());
        }
    }

    @CrossOrigin(origins = origin)
    @GetMapping("/status")
    public int status() {

        return 1;
    }
    @CrossOrigin(origins = origin)
    @GetMapping("/players")
    public CollectionModel<EntityModel<Player>> all() {
        List<EntityModel<Player>> players = repoSortToList().stream() //
                .map(assembler::toModel) //
                .collect(Collectors.toList());

        return CollectionModel.of(players, linkTo(methodOn(PlayerController.class).all()).withSelfRel());
    }

    @CrossOrigin(origins = origin)
    @GetMapping("/players/{pageNo}/{pageSize}")
    public CollectionModel<EntityModel<Player>> page(@PathVariable("pageNo") Long pageNo, @PathVariable("pageSize") Long pageSize) {

        if (pageNo < 0)
            pageNo = 0L;
        long startPosition = pageNo * pageSize;
        long endPosition = startPosition + pageSize;


        Object[] playersIds =   repoSortToList().stream() //
                                .map(Player::getUid)
                                .toArray();

        if (endPosition > playersIds.length)
        {

            startPosition = (playersIds.length / pageSize) * pageSize;
            endPosition = Math.min(playersIds.length, startPosition + pageSize);

        }

        List<EntityModel<Player>> players = IntStream
                .range((int) startPosition, (int) endPosition)
                .mapToObj(i -> assembler.toModel(repository.findById((Long) playersIds[i]).orElseThrow(()
                                                        -> new PlayerNotFoundException((Long) playersIds[i]))))
                .toList();
        return CollectionModel.of(players, linkTo(methodOn(PlayerController.class).all()).withSelfRel());

    }

    // Single item
    @CrossOrigin(origins = origin)
    @GetMapping("/players/{id}")
    public EntityModel<Player> one(@PathVariable Long id) {

        Player player = repository.findById(id) //
                .orElseThrow(() -> new PlayerNotFoundException(id));

        return assembler.toModel(player);
    }

    @CrossOrigin(origins = origin)
    @GetMapping("/players/size")
    public long size() {
        return repository.findAll().size();
    }

    ///TODO: Make it a PutMapping instead
    @CrossOrigin(origins = origin)
    @GetMapping("/players/sort/{reverse}")
    public CollectionModel<EntityModel<Player>> sortRepo(@PathVariable("reverse") String reverse)
    {
        this.reversed = reverse.equalsIgnoreCase("true") || reverse.equals("1");

        EntityModel<Boolean> entityModel = EntityModel.of(reversed);

        return this.all();
    }
    @CrossOrigin(origins = origin)
    @PutMapping("/players/{id}")
    ResponseEntity<?> replacePlayers(@RequestBody Player newPlayer, @PathVariable Long id) {

        Player updatedPlayer = repository.findById(id) //
                .map(player -> {
                    player.setUsername(newPlayer.getUsername());
                    player.setBank(newPlayer.getBank());
                    player.setLevel(newPlayer.getLevel());
                    return repository.save(player);
                }) //
                .orElseGet(() -> {
                    newPlayer.setUid(id);
                    return repository.save(newPlayer);
                });

        EntityModel<Player> entityModel = assembler.toModel(updatedPlayer);

        return ResponseEntity //
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()) //
                .body(entityModel);
    }

    @CrossOrigin(origins = origin)
    @DeleteMapping("/players/{id}")
    ResponseEntity<?> deletePlayer(@PathVariable Long id) {

        repository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
