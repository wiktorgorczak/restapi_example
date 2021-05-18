package pl.poznan.put.cs.net.restapiexample.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;

import pl.poznan.put.cs.net.restapiexample.exception.NotFoundException;
import pl.poznan.put.cs.net.restapiexample.hateoas.UserModelAssembler;
import pl.poznan.put.cs.net.restapiexample.model.User;
import pl.poznan.put.cs.net.restapiexample.service.UserService;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
	
	private final UserService userService;
	private final UserModelAssembler userModelAssembler;
	private final Logger logger = LoggerFactory.getLogger(ProductController.class);
	
	@Autowired
	public UserController(UserService userService, UserModelAssembler userModelAssembler) {
		this.userService = userService;
		this.userModelAssembler = userModelAssembler;
	}
	
	@GetMapping
	public CollectionModel<EntityModel<User>> getAllUsers() {
		List<EntityModel<User>> users = userService.findAll()
				.stream()
				.map(userModelAssembler::toModel)
				.collect(Collectors.toList());
		return CollectionModel.of(users, 
				linkTo(methodOn(UserController.class).getAllUsers()).withSelfRel());
	}
	
	@GetMapping("/{id}")
	public EntityModel<User> getUser(@PathVariable String id) {
		User user = userService.findById(id)
				.orElseThrow(NotFoundException::new);
		
		return userModelAssembler.toModel(user);
	}
	
	@PostMapping
	public ResponseEntity<EntityModel<User>> addUser(@RequestBody User user) {
		User created = userService.create(user);
		EntityModel<User> model = userModelAssembler.toModel(created);
		
		return ResponseEntity
				.created(model.getRequiredLink(IanaLinkRelations.SELF).toUri())
				.body(model);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteUser(@PathVariable String id) {
		User user = userService.findById(id)
				.orElseThrow(NotFoundException::new);
		
		userService.delete(user);
		
		return ResponseEntity.noContent().build();
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> replaceUser(@PathVariable String id, @RequestBody User user) {
		Optional<User> currentUser = userService.findById(id);
		
		if(currentUser.isEmpty()) {
			return addUser(user);
		} else {
			userService.replace(currentUser.get(), user);
		}
		
		return ResponseEntity.noContent().build();
	}
	
	@PatchMapping("/{id}")
	public ResponseEntity<?> updateUser(@PathVariable String id, @RequestBody JsonPatch patch) {
		User currentUser = userService.findById(id)
				.orElseThrow(NotFoundException::new);
		
		try {
			userService.applyPatchAndUpdate(currentUser, patch);
		} catch (JsonProcessingException e) {
			logger.error(e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		} catch (JsonPatchException e) {
			return ResponseEntity.unprocessableEntity().build();
		}
		
		return ResponseEntity.noContent().build();
	}
}
