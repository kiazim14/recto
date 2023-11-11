package com.commerce.backend.dao;

import com.commerce.backend.model.entity.Cart;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends CrudRepository<Cart, Long> {

    @Query(value="select * from cart", nativeQuery = true)
    List<Cart> findByCart();
}
