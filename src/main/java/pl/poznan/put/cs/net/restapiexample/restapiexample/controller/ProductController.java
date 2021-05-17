package pl.poznan.put.cs.net.restapiexample.restapiexample.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

import pl.poznan.put.cs.net.restapiexample.restapiexample.exception.NotFoundException;
import pl.poznan.put.cs.net.restapiexample.restapiexample.hateoas.ProductModelAssembler;
import pl.poznan.put.cs.net.restapiexample.restapiexample.model.Product;
import pl.poznan.put.cs.net.restapiexample.restapiexample.service.ProductService;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
	
	private final ProductService productService;
	private final ProductModelAssembler productModelAssembler;
	
	@Autowired
	public ProductController(ProductService productService, ProductModelAssembler productModelAssembler) {
		this.productService = productService;
		this.productModelAssembler = productModelAssembler;
	}
	
	@GetMapping
	public CollectionModel<EntityModel<Product>> getAllProducts() {
		List<EntityModel<Product>> products = productService.findAll()
				.stream()
				.map(productModelAssembler::toModel)
				.collect(Collectors.toList());
		
		return CollectionModel.of(products,
				linkTo(methodOn(ProductController.class).getAllProducts()).withSelfRel());
	}
	
	@GetMapping("/{id}")
	public EntityModel<Product> getProduct(@PathVariable String id) {
		Product product = productService.findById(id)
				.orElseThrow(NotFoundException::new);
		
		return productModelAssembler.toModel(product);
	}
	
	@PostMapping
	public ResponseEntity<EntityModel<Product>> addProduct(@RequestBody Product product) {
		Product created = productService.create(product);
		EntityModel<Product> model = productModelAssembler.toModel(created);
		
		return ResponseEntity
				.created(model.getRequiredLink(IanaLinkRelations.SELF).toUri())
				.body(model);
	}
	
	@PatchMapping("/{id}")
	public ResponseEntity<?> updateProduct(@PathVariable String id, @RequestBody JsonPatch patch) {
		Product currentProduct = productService.findById(id)
				.orElseThrow(NotFoundException::new);
		
		try {
			productService.applyPatchAndUpdate(currentProduct, patch);
		} catch (JsonProcessingException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		} catch (JsonPatchException e) {
			return ResponseEntity.unprocessableEntity().build();
		}
		
		return ResponseEntity.noContent().build();
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteProduct(@PathVariable String id) {
		Product product = productService.findById(id)
				.orElseThrow(NotFoundException::new);
		
		productService.delete(product);
		
		return ResponseEntity.noContent().build();
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> replaceProduct(@PathVariable String id, @RequestBody Product product) {
		Optional<Product> currentProduct = productService.findById(id);
		
		if(currentProduct.isEmpty()) {
			return addProduct(product);
		} else {
			productService.replace(currentProduct.get(), product);
		}
		
		return ResponseEntity.noContent().build();
	}
}
