package pl.poznan.put.cs.net.restapiexample.repository;

import org.springframework.data.repository.CrudRepository;

import pl.poznan.put.cs.net.restapiexample.model.Opinion;

public interface OpinionRepository extends CrudRepository<Opinion, String> {

}
