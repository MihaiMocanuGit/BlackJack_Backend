package blackjack.backend.assemblers;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import blackjack.backend.domain.Player;
import blackjack.backend.service.PlayerController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class PlayerModelAssembler implements RepresentationModelAssembler<Player, EntityModel<Player>> {

    @Override
    public EntityModel<Player> toModel(Player employee) {

        return EntityModel.of(employee, //
                linkTo(methodOn(PlayerController.class).one(employee.getId())).withSelfRel(),
                linkTo(methodOn(PlayerController.class).all()).withRel("employees"));
    }
}