package pl.poznan.put.cs.net.restapiexample.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import pl.poznan.put.cs.net.restapiexample.model.User;

public interface UserRepository extends CrudRepository<User, String> {

	@Override
	public List<User> findAll();
	public Optional<User> findByLogin(String login);
}
