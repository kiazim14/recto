package com.commerce.backend.service;

import com.commerce.backend.converter.user.UserResponseConverter;
import com.commerce.backend.dao.CartRepository;
import com.commerce.backend.dao.UserRepository;
import com.commerce.backend.error.exception.InvalidArgumentException;
import com.commerce.backend.error.exception.ResourceFetchException;
import com.commerce.backend.error.exception.ResourceNotFoundException;
import com.commerce.backend.model.dto.UserDTO;
import com.commerce.backend.model.entity.Cart;
import com.commerce.backend.model.entity.User;
import com.commerce.backend.model.request.user.PasswordResetRequest;
import com.commerce.backend.model.request.user.RegisterUserRequest;
import com.commerce.backend.model.request.user.UpdateUserAddressRequest;
import com.commerce.backend.model.request.user.UpdateUserRequest;
import com.commerce.backend.model.response.user.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserResponseConverter userResponseConverter;
    private final CartRepository cartRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           UserResponseConverter userResponseConverter, CartRepository cartRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userResponseConverter = userResponseConverter;
        this.cartRepository = cartRepository;
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
        for(User users: user) {
            if (users.getCart() != null) {
                cartRepository.findAll().forEach(System.out::println);
            }
        }

       // user.parallelStream().map(User::getCart).forEach(System.out::println);
        List<User> users = new ArrayList<>();
    return user;
    }
}

