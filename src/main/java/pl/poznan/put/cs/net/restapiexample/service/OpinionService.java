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
import pl.poznan.put.cs.net.restapiexample.model.Opinion;
import pl.poznan.put.cs.net.restapiexample.model.Product;
import pl.poznan.put.cs.net.restapiexample.model.User;
import pl.poznan.put.cs.net.restapiexample.repository.OpinionRepository;

@Service
@Transactional(readOnly = true)
public class OpinionService {
	
	private final OpinionRepository opinionRepository;
	private final UserService userService;
	private final ObjectMapper objectMapper = new ObjectMapper();
	
	@Autowired
	public OpinionService(OpinionRepository opinionRepository, 
			UserService userService) {
		this.opinionRepository = opinionRepository;
		this.userService = userService;
	}
	
	public Optional<Opinion> findById(String id) {
		return opinionRepository.findById(id);
	}
	
	public List<Opinion> findByUser(User user) {
		return opinionRepository.findByUser(user);
	}
	
	public Optional<Opinion> findByProductAndId(Product product, String id) {
		return opinionRepository.findByProductAndId(product, id);
	}
	
	public List<Opinion> findByProduct(Product product) {
		return opinionRepository.findByProduct(product);
	}
	
	@Transactional(readOnly = false)
	public Opinion create(Opinion opinion, Product product) {
		User user = userService.findById(opinion.getUser().getId())
				.orElseThrow(NotFoundException::new);
		
		opinion.setProduct(product);
		opinion.setUser(user);
		
		return opinionRepository.save(opinion);
	}
	
	@Transactional(readOnly = false)
	public void delete(Opinion opinion) {
		User user = opinion.getUser();
		Product product = opinion.getProduct();
		
		user.getOpinions().remove(opinion);
		product.getOpinions().remove(opinion);
		
		opinionRepository.delete(opinion);
	}
	
	@Transactional(readOnly = false)
	public void replace(Opinion currentOpinion, Opinion newOpinion, Product product) {
		delete(currentOpinion);
		create(newOpinion, product);
	}
	
	@Transactional(readOnly = false)
	public void applyPatchAndUpdate(Opinion opinion, JsonPatch patch, Product product) 
			throws JsonPatchException, JsonProcessingException {
		JsonNode patchedJson = patch.apply(objectMapper.convertValue(opinion, JsonNode.class));
		Opinion patched = objectMapper.treeToValue(patchedJson, Opinion.class);
		
		replace(opinion, patched, product);
	}
}
