package pl.poznan.put.cs.net.restapiexample.hateoas;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import pl.poznan.put.cs.net.restapiexample.controller.UserController;
import pl.poznan.put.cs.net.restapiexample.model.User;

@Component
public class UserModelAssembler implements RepresentationModelAssembler<User, EntityModel<User>>{

	@Override
	public EntityModel<User> toModel(User entity) {
		return EntityModel.of(entity,
				linkTo(methodOn(UserController.class)
						.getUser(entity.getId())).withSelfRel(),
				linkTo(methodOn(UserController.class)
						.getAllUsers()).withRel("users"));
	}

}
