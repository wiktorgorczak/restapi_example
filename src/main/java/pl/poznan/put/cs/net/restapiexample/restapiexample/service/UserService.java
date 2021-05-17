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

import pl.poznan.put.cs.net.restapiexample.restapiexample.model.User;
import pl.poznan.put.cs.net.restapiexample.restapiexample.repository.UserRepository;

@Service
public class UserService {

	private final UserRepository userRepository;
	private final ObjectMapper objectMapper = new ObjectMapper();
	
	@Autowired
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	public List<User> findAll() {
		return userRepository.findAll();
	}
	
	public Optional<User> findById(String id) {
		return userRepository.findById(id);
	}
	
	public User create(User user) {
		return userRepository.save(user);
	}
	
	public void delete(User user) {
		userRepository.delete(user);
	}
	
	public void replace(User user, User newUser) {
		userRepository.save(newUser);
	}
	
	public void applyPatchAndUpdate(User currentUser, JsonPatch patch) throws JsonPatchException, JsonProcessingException {
		JsonNode patchedJson = patch.apply(objectMapper.convertValue(currentUser, JsonNode.class));
		User patched = objectMapper.treeToValue(patchedJson, User.class);
		
		userRepository.save(patched);
	}
}
