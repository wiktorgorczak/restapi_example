package pl.poznan.put.cs.net.restapiexample.restapiexample.hateoas;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;

import pl.poznan.put.cs.net.restapiexample.restapiexample.controller.UserController;
import pl.poznan.put.cs.net.restapiexample.restapiexample.model.User;

public class UserModelAssembler implements RepresentationModelAssembler<User, EntityModel<User>>{

	@Override
	public EntityModel<User> toModel(User entity) {
		return EntityModel.of(entity,
				linkTo(methodOn(UserController.class)
						.getUser(entity.getLogin())).withSelfRel(),
				linkTo(methodOn(UserController.class)
						.getAllUsers()).withRel("users"));
	}

}
