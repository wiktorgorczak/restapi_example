package pl.poznan.put.cs.net.restapiexample.hateoas;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import pl.poznan.put.cs.net.restapiexample.controller.ProductController;
import pl.poznan.put.cs.net.restapiexample.model.Product;

@Component
public class ProductModelAssembler implements RepresentationModelAssembler<Product, EntityModel<Product>> {

	@Override
	public EntityModel<Product> toModel(Product product) {
		return EntityModel.of(product, 
				linkTo(methodOn(ProductController.class)
						.getProduct(product.getId())).withSelfRel(),
				linkTo(methodOn(ProductController.class)
						.getAllProducts()).withRel("products"));
	}
	
}
