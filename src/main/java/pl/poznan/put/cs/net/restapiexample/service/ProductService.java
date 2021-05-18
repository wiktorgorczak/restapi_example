package pl.poznan.put.cs.net.restapiexample.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;

import pl.poznan.put.cs.net.restapiexample.exception.NotFoundException;
import pl.poznan.put.cs.net.restapiexample.model.Product;
import pl.poznan.put.cs.net.restapiexample.model.User;
import pl.poznan.put.cs.net.restapiexample.repository.ProductRepository;

@Service
@Transactional(readOnly = true)
public class ProductService {

	private final ProductRepository productRepository;
	private final UserService userService;
	private final ObjectMapper objectMapper = new ObjectMapper();
	
	@Autowired
	public ProductService(ProductRepository productRepository, UserService userService) {
		this.productRepository = productRepository;
		this.userService = userService;
	}

	public List<Product> findAll() {
		return productRepository.findAll();
	}
	
	public Optional<Product> findById(String id) {
		return productRepository.findById(id);
	}
	
	@Transactional(readOnly = false)
	public void delete(Product product) {
		User user = product.getUser();
		user.getProducts().remove(product);
		
		productRepository.delete(product);
	}
	
	@Transactional(readOnly = false)
	public Product create(Product product) {
		User user = userService.findById(
				product.getUser().getId()).orElseThrow(NotFoundException::new);
		
		product.setUser(user);
		
		return productRepository.save(product);
	}
	
	@Transactional(readOnly = false)
	public void applyPatchAndUpdate(Product product, JsonPatch patch) throws JsonPatchException, JsonProcessingException {
		JsonNode patchedJson = patch.apply(objectMapper.convertValue(product, JsonNode.class));
		Product patched = objectMapper.treeToValue(patchedJson, Product.class);
		
		productRepository.save(patched);
	}
	
	@Transactional(readOnly = false)
	public void replace(Product currentProduct, Product newProduct) {
		productRepository.save(newProduct);
	}
}
