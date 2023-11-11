package com.commerce.backend.dao;

import com.commerce.backend.model.entity.Cart;
import com.commerce.backend.model.entity.CartItem;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CartItemRepository extends CrudRepository<CartItem, Long> {
    @Query(value="select * from cart_item", nativeQuery = true)
    List<CartItem> findByCartItem();
}
