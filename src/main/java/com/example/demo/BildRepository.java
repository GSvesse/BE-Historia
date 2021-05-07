package com.example.demo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

//hejhej
@Repository
public interface BildRepository extends CrudRepository<Bilder, Integer> {

    List<Bilder> findAllByAddressesEquals(Address address);

    Optional<Bilder> findBilderByDocumentIDEquals (String documentID);

    List<Bilder> findAllByTagsEquals (Tag tag);

    List<Bilder> findAllByDistrictEquals (String district);

    List<Bilder> findAllByYearBetween (int start, int end);

}