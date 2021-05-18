package pl.poznan.put.cs.net.restapiexample.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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
import pl.poznan.put.cs.net.restapiexample.hateoas.OpinionModelAssembler;
import pl.poznan.put.cs.net.restapiexample.model.Opinion;
import pl.poznan.put.cs.net.restapiexample.model.Product;
import pl.poznan.put.cs.net.restapiexample.service.OpinionService;
import pl.poznan.put.cs.net.restapiexample.service.ProductService;

@RestController
@RequestMapping("/api/v1/products/{productId}/opinions")
public class OpinionController {
	
	private final OpinionService opinionService;
	private final ProductService productService;
	private final OpinionModelAssembler opinionModelAssembler;
	private final Logger logger = LoggerFactory.getLogger(OpinionController.class);
	
	@Autowired
	public OpinionController(OpinionService opinionService, 
			ProductService productService,
			OpinionModelAssembler opinionModelAssembler) {
		this.opinionService = opinionService;
		this.productService = productService;
		this.opinionModelAssembler = opinionModelAssembler;
	}
	
	@GetMapping
	public CollectionModel<EntityModel<Opinion>> getAllOpinions(@PathVariable String productId) {
		Product product = productService.findById(productId)
				.orElseThrow(NotFoundException::new);
		List<EntityModel<Opinion>> opinions = 
				opinionService.findByProduct(product).stream()
				.map(opinionModelAssembler::toModel)
				.collect(Collectors.toList());
		
		return CollectionModel.of(opinions, 
				linkTo(methodOn(OpinionController.class)
						.getAllOpinions(productId)).withSelfRel());
	}
	
	@GetMapping("/{opinionId}")
	public EntityModel<Opinion> getOpinion(@PathVariable String productId, 
			@PathVariable String opinionId) {
		Product product = productService.findById(productId)
				.orElseThrow(NotFoundException::new);
		
		Opinion opinion = opinionService.findByProductAndId(product, opinionId)
				.orElseThrow(NotFoundException::new);
		
		return opinionModelAssembler.toModel(opinion);
	}
	
	@PostMapping
	public ResponseEntity<EntityModel<Opinion>> addOpinion(
			@RequestBody Opinion opinion, @PathVariable String productId) {
		Product product = productService.findById(productId)
				.orElseThrow(NotFoundException::new);
		
		Opinion created = opinionService.create(opinion, product);
		EntityModel<Opinion> model = opinionModelAssembler.toModel(created);
		
		return ResponseEntity.created(model.getRequiredLink(IanaLinkRelations.SELF).toUri())
				.body(model);
	}
	
	@DeleteMapping("/{opinionId}")
	public ResponseEntity<?> deleteOpinion(@PathVariable String productId, 
			@PathVariable String opinionId) {
		Product product = productService.findById(productId)
				.orElseThrow(NotFoundException::new);
		
		Opinion opinion = opinionService.findByProductAndId(product, opinionId)
				.orElseThrow(NotFoundException::new);
		
		opinionService.delete(opinion);
		
		return ResponseEntity.noContent().build();
	}
	
	@PutMapping("/{opinionId}")
	public ResponseEntity<?> replaceOpinion(@RequestBody Opinion opinion, 
			@PathVariable String productId, @PathVariable String opinionId) {
		Product product = productService.findById(productId)
				.orElseThrow(NotFoundException::new);
		
		Optional<Opinion> currentOpinion = opinionService.findByProductAndId(product, opinionId);
		
		if(currentOpinion.isEmpty()) {
			return addOpinion(opinion, productId);
		} else {
			opinionService.replace(currentOpinion.get(), opinion, product);
		}
		
		return ResponseEntity.noContent().build();
	}
	
	@PatchMapping("/{opinionId}")
	public ResponseEntity<?> updateOpinion(@PathVariable String productId, 
			@PathVariable String opinionId, @RequestBody JsonPatch patch) {
		Product product = productService.findById(productId)
				.orElseThrow(NotFoundException::new);
		Opinion opinion = opinionService.findByProductAndId(product, opinionId)
				.orElseThrow(NotFoundException::new);
		
		try {
			opinionService.applyPatchAndUpdate(opinion, patch, product);
		} catch (JsonProcessingException e) {
			logger.error(e.getMessage(), e);
			
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		} catch (JsonPatchException e) {
			return ResponseEntity.unprocessableEntity().build();
		}
		
		return ResponseEntity.noContent().build();
	}
}
