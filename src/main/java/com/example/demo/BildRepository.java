package com.example.demo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

//hejhej
@Repository
public interface BildRepository extends CrudRepository<Bilder, Integer> {
}
