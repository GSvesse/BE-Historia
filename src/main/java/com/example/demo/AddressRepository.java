package com.example.demo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends CrudRepository<Address, Integer> {

    List<Address> findByAddress(String address);

    List<Address> findAllByAddressContains (String address);

    Address findAddressByAddress (String address);
}