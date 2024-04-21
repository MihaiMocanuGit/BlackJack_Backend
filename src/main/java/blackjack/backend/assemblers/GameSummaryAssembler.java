package blackjack.backend.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import blackjack.backend.domain.GameSummary;
import blackjack.backend.controller.PrimaryController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class GameSummaryAssembler implements RepresentationModelAssembler<GameSummary, EntityModel<GameSummary>> {

    @Override
    public EntityModel<GameSummary> toModel(GameSummary gameSummary) {

        return EntityModel.of(gameSummary, //
                linkTo(methodOn(PrimaryController.class).one(gameSummary.getUid())).withSelfRel(),
                linkTo(methodOn(PrimaryController.class).all()).withRel("gameSummary"));
    }

}

