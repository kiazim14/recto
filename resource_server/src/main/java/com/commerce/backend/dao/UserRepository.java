package com.commerce.backend.dao;

import com.commerce.backend.model.entity.Cart;
import com.commerce.backend.model.entity.User;
import com.commerce.backend.service.CartService;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);

    @Query(value="select * from user", nativeQuery = true)
    List<User> getUserCart();
}
