package com.springinventory.demo.repository;

import com.springinventory.demo.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    boolean existsItemByInventoryCode(String code);
}
