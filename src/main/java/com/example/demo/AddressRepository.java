package com.example.demo;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends CrudRepository<Address, Integer> {

    List<Address> findByAddress(String address);

    List<Address> findAllByAddressContains (String address);

    Address findAddressByAddress (String address);

    // ny, används av getByAddressLike:
    @Query(value = "SELECT a FROM address a WHERE a.address like :addressName")
    List<Address> includeAllAddressesOnStreet(
            @Param("addressName") String addressName
    );
}