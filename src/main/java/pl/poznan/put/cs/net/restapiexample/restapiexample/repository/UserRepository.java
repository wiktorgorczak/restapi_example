package pl.poznan.put.cs.net.restapiexample.restapiexample.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import pl.poznan.put.cs.net.restapiexample.restapiexample.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, String> {

	@Override
	public List<User> findAll();
	public Optional<User> findByLogin(String login);
}
