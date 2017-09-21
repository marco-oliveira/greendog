package com.greendog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.greendog.domain.Item;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long>{

}
