package pl.poznan.put.cs.net.restapiexample.restapiexample.repository;

import org.springframework.data.repository.CrudRepository;

import pl.poznan.put.cs.net.restapiexample.restapiexample.model.Opinion;

public interface OpinionRepository extends CrudRepository<Opinion, String> {

}
