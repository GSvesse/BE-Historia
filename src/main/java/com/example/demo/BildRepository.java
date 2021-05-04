package com.example.demo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

//hejhej
@Repository
public interface BildRepository extends CrudRepository<Bilder, Integer> {

    Optional<Bilder> findAllByAddressesEquals(Address address);

}