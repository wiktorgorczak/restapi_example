package pl.poznan.put.cs.net.restapiexample.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import pl.poznan.put.cs.net.restapiexample.model.Product;

@Repository
public interface ProductRepository extends CrudRepository<Product, String> {
	
	@Override
	List<Product> findAll(); 
}
