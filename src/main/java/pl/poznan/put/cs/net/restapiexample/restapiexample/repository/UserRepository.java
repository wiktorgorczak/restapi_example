package pl.poznan.put.cs.net.restapiexample.restapiexample.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import pl.poznan.put.cs.net.restapiexample.restapiexample.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, String> {

}
