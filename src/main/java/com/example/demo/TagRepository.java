package com.example.demo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends CrudRepository<Tag, Integer>{

    List<Tag> findByTag(String tag);

    Tag findTagByTag (String tag);
}