package com.commerce.backend.service;

import ch.qos.logback.core.BasicStatusManager;
import ch.qos.logback.core.joran.spi.ElementPath;
import com.commerce.backend.converter.user.UserResponseConverter;
import com.commerce.backend.dao.CartItemRepository;
import com.commerce.backend.dao.CartRepository;
import com.commerce.backend.dao.UserRepository;
import com.commerce.backend.error.exception.InvalidArgumentException;
import com.commerce.backend.error.exception.ResourceNotFoundException;
import com.commerce.backend.model.dto.UserDTO;
import com.commerce.backend.model.entity.Cart;
import com.commerce.backend.model.entity.CartItem;
import com.commerce.backend.model.entity.User;
import com.commerce.backend.model.request.user.PasswordResetRequest;
import com.commerce.backend.model.request.user.RegisterUserRequest;
import com.commerce.backend.model.request.user.UpdateUserAddressRequest;
import com.commerce.backend.model.request.user.UpdateUserRequest;
import com.commerce.backend.model.response.user.UserResponse;
import com.fasterxml.jackson.annotation.JacksonInject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserResponseConverter userResponseConverter;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           UserResponseConverter userResponseConverter, CartRepository cartRepository, CartItemRepository cartItemRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userResponseConverter = userResponseConverter;
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
    }

    @Override
    public User register(RegisterUserRequest registerUserRequest) {
        if (userExists(registerUserRequest.getEmail())) {
            throw new InvalidArgumentException("An account already exists with this email");
        }

        User user = new User();
        user.setEmail(registerUserRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerUserRequest.getPassword()));
        user.setEmailVerified(0);

        return userRepository.save(user);
    }

    @Override
    public UserResponse fetchUser() {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        if (Objects.isNull(userName)) {
            throw new AccessDeniedException("Invalid access");
        }

        Optional<User> user = userRepository.findByEmail(userName);
        if (user.isEmpty()) {
            throw new ResourceNotFoundException("User not found");
        }
        return userResponseConverter.apply(user.get());
    }


    @Override
    public User getUser() {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        if (Objects.isNull(userName)) {
            throw new AccessDeniedException("Invalid access");
        }

        Optional<User> user = userRepository.findByEmail(userName);
        if (user.isEmpty()) {
            throw new ResourceNotFoundException("User not found");
        }
        return user.get();
    }

    @Override
    public User saveUser(User user) {
        if (Objects.isNull(user)) {
            throw new InvalidArgumentException("Null user");
        }

        return userRepository.save(user);
    }

    @Override
    public User findByEmail(String email) {
        if (Objects.isNull(email)) {
            throw new InvalidArgumentException("Null email");
        }

        return userRepository.findByEmail(email).orElse(null);
    }

    @Override
    public boolean userExists(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public UserResponse updateUser(UpdateUserRequest updateUserRequest) {
        User user = getUser();
        user.setFirstName(updateUserRequest.getFirstName());
        user.setLastName(updateUserRequest.getLastName());
        user.setPhone(updateUserRequest.getPhone());

        user = userRepository.save(user);
        return userResponseConverter.apply(user);
    }

    @Override
    public UserResponse updateUserAddress(UpdateUserAddressRequest updateUserAddressRequest) {
        User user = getUser();
        user.setAddress(updateUserAddressRequest.getAddress());
        user.setCity(updateUserAddressRequest.getCity());
        user.setState(updateUserAddressRequest.getState());
        user.setZip(updateUserAddressRequest.getZip());
        user.setCountry(updateUserAddressRequest.getCountry());

        user = userRepository.save(user);
        return userResponseConverter.apply(user);
    }

    @Override
    public void resetPassword(PasswordResetRequest passwordResetRequest) {
        User user = getUser();
        if (!passwordEncoder.matches(passwordResetRequest.getOldPassword(), user.getPassword())) {
            throw new InvalidArgumentException("Invalid password");
        }

        if (passwordEncoder.matches(passwordResetRequest.getNewPassword(), user.getPassword())) {
            return;
        }

        user.setPassword(passwordEncoder.encode(passwordResetRequest.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    public Boolean getVerificationStatus() {
        User user = getUser();
        return user.getEmailVerified() == 1;
    }

    @Override
    public User addSubscibe(UserDTO userDTO) {
        User user = new User();
        user.setAddress(userDTO.getAddress());
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setState(userDTO.getState());
        user.setRegistrationDate(userDTO.getRegistrationDate());
        user.setZip(userDTO.getZip());
        user.setPhone(userDTO.getPhone());
        user.setCity(userDTO.getCity());
        user.setEmailVerified(0);
        user.setLastName(userDTO.getLastName());
        user.setFirstName(userDTO.getFirstName());
        user.setCountry(userDTO.getCountry());
        return userRepository.save(user);

    }

    @Override
    public List<User> findAll() {

        List<User> user = (List<User>) userRepository.findAll();
        if (user == null) {
            throw new ResourceNotFoundException("Objet null");

        }
        // user.parallelStream().map(User::getCart).forEach(System.out::println);
        return user;
    }

    @Override
    public List<User> getUserCart() {
        List<User> user = userRepository.getUserCart();
        Cart cart = cartRepository.findAll().iterator().next();
        List<User> result = new ArrayList<>();

                for (User users: user) {
                    User user1 = new User();
                    user1.setId(users.getId());
                    user1.setCountry(users.getCountry());
                    user1.setCart(cart);
                    user1.setFirstName(users.getFirstName());
                    user1.setLastName(users.getLastName());
                    user1.setEmail(users.getEmail());
                    user1.setLastName(users.getLastName());
                    user1.setCity(users.getCity());
                    user1.setPhone(users.getPhone());
                    user1.setZip(users.getZip());
                    user1.setPassword(users.getPassword());
                    user1.setEmailVerified(users.getEmailVerified());
                    user1.setAddress(users.getAddress());
                    user1.setRegistrationDate(users.getRegistrationDate());
                    result.add(user1);

                }

        return result;
        }
}


//    private Cart getCartList() {
//        Cart cart1 = new Cart();
//        List<Cart> cart = cartRepository.findByCart();
//             for(Cart cars: cart){
//                 cart1.setId(cars.getId());
//                 cart1.setTotalCartPrice(cars.getTotalCartPrice());
//                 cart1.setTotalPrice(cars.getTotalPrice());
//                 cart1.setDateCreated(cars.getDateCreated());
//                 cart1.setCartItemList(Collections.singletonList(getCartItemList()));
//                 cart1.setTotalCargoPrice(cars.getTotalCargoPrice());
//                 cart1.setUser(cars.getUser());
//
//        }
//             return cart1;
//    }

//    private CartItem getCartItemList() {
//        CartItem cartItem1 = new CartItem();
//        List<CartItem> cartItem = cartItemRepository.findByCartItem();
//        for (CartItem cartel : cartItem) {
//            cartItem1.setCart(cartel.getCart());
//            cartItem1.setProductVariant(cartel.getProductVariant());
//            cartItem1.setAmount(cartel.getAmount());
//            cartItem1.setId(cartel.getId());
//        }
//        return cartItem1;
//    }


