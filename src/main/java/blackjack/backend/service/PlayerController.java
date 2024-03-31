package blackjack.backend.service;


import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import blackjack.backend.assemblers.PlayerModelAssembler;
import blackjack.backend.domain.Player;
import blackjack.backend.exceptions.PlayerNotFoundException;
import blackjack.backend.repository.PlayerRepository;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@RestController
public class PlayerController {
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

    @PostMapping("/players")
    ResponseEntity<?> newPlayer(@RequestBody Player newPlayer) {

        EntityModel<Player> entityModel = assembler.toModel(repository.save(newPlayer));

        return ResponseEntity //
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()) //
                .body(entityModel);
    }


    @GetMapping("/players")
    public CollectionModel<EntityModel<Player>> all() {
        List<EntityModel<Player>> players = repoSortToList().stream() //
                .map(assembler::toModel) //
                .collect(Collectors.toList());

        return CollectionModel.of(players, linkTo(methodOn(PlayerController.class).all()).withSelfRel());
    }

    @GetMapping("/players/{pageNo}/{pageSize}")
    public CollectionModel<EntityModel<Player>> page(@PathVariable("pageNo") Long pageNo, @PathVariable("pageSize") Long pageSize) {

        long startPosition = pageNo * pageSize;
        long endPosition = startPosition + pageSize;

        Object[] playersIds =   repoSortToList().stream() //
                                .map(Player::getUid)
                                .toArray();
        List<EntityModel<Player>> players = IntStream
                .range((int) startPosition, (int) endPosition)
                .mapToObj(i -> assembler.toModel(repository.findById((Long) playersIds[i]).orElseThrow(()
                                                        -> new PlayerNotFoundException((Long) playersIds[i]))))
                .toList();
        return CollectionModel.of(players, linkTo(methodOn(PlayerController.class).all()).withSelfRel());

    }

    // Single item
    @GetMapping("/players/{id}")
    public EntityModel<Player> one(@PathVariable Long id) {

        Player player = repository.findById(id) //
                .orElseThrow(() -> new PlayerNotFoundException(id));

        return assembler.toModel(player);
    }


    ///TODO: Make it a PutMapping instead
    @GetMapping("/players/sort/{reverse}")
    public CollectionModel<EntityModel<Player>> sortRepo(@PathVariable("reverse") Boolean reverse)
    {
        this.reversed = reverse;

        EntityModel<Boolean> entityModel = EntityModel.of(reversed);

        return this.all();
    }

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

    @DeleteMapping("/players/{id}")
    ResponseEntity<?> deletePlayer(@PathVariable Long id) {

        repository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
