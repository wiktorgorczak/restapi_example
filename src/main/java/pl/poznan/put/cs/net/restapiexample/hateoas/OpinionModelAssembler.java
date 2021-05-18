package pl.poznan.put.cs.net.restapiexample.hateoas;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import pl.poznan.put.cs.net.restapiexample.controller.OpinionController;
import pl.poznan.put.cs.net.restapiexample.model.Opinion;

@Component
public class OpinionModelAssembler implements RepresentationModelAssembler<Opinion, EntityModel<Opinion>> {

	@Override
	public EntityModel<Opinion> toModel(Opinion entity) {
		return EntityModel.of(entity,
				linkTo(methodOn(OpinionController.class)
						.getOpinion(entity.getProduct().getId(), entity.getId()))
						.withSelfRel(),
				linkTo(methodOn(OpinionController.class)
						.getAllOpinions(entity.getProduct().getId()))
						.withRel("opinions")
		);
	}

}
