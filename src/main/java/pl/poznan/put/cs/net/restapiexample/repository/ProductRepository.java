package pl.poznan.put.cs.net.restapiexample.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import pl.poznan.put.cs.net.restapiexample.model.Product;

public interface ProductRepository extends CrudRepository<Product, String> {
	
	@Override
	List<Product> findAll(); 
}
