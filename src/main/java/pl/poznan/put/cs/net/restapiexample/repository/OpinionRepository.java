package pl.poznan.put.cs.net.restapiexample.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import pl.poznan.put.cs.net.restapiexample.model.Opinion;
import pl.poznan.put.cs.net.restapiexample.model.Product;
import pl.poznan.put.cs.net.restapiexample.model.User;

public interface OpinionRepository extends CrudRepository<Opinion, String> {

	List<Opinion> findByUser(User user);
	List<Opinion> findByProduct(Product product);
	Optional<Opinion> findByProductAndId(Product product, String id);
}
