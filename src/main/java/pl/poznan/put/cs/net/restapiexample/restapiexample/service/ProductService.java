package pl.poznan.put.cs.net.restapiexample.restapiexample.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;

import pl.poznan.put.cs.net.restapiexample.restapiexample.model.Product;
import pl.poznan.put.cs.net.restapiexample.restapiexample.repository.ProductRepository;

@Service
public class ProductService {

	private final ProductRepository productRepository;
	private final ObjectMapper objectMapper = new ObjectMapper();
	
	@Autowired
	public ProductService(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	public List<Product> findAll() {
		return productRepository.findAll();
	}
	
	public Optional<Product> findById(String id) {
		return productRepository.findById(id);
	}
	
	public void delete(Product product) {
		productRepository.delete(product);
	}
	
	public Product create(Product product) {
		return productRepository.save(product);
	}
	
	public void update(Product currentProduct, Product newProduct) {
		if(newProduct.getName() != null) {
			currentProduct.setName(newProduct.getName());
		}
		
		if(newProduct.getDescription() != null) {
			currentProduct.setDescription(newProduct.getDescription());
		}
	}
	
	public void applyPatchAndUpdate(Product product, JsonPatch patch) throws JsonPatchException, JsonProcessingException {
		JsonNode patchedJson = patch.apply(objectMapper.convertValue(product, JsonNode.class));
		Product patched = objectMapper.treeToValue(patchedJson, Product.class);
		
		productRepository.save(patched);
	}
	
	public void replace(Product currentProduct, Product newProduct) {
		productRepository.save(newProduct);
	}
}
