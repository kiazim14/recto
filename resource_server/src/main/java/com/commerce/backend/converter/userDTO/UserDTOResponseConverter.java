package com.commerce.backend.converter.userDTO;

import com.commerce.backend.model.dto.UserDTO;
import com.commerce.backend.model.entity.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class UserDTOResponseConverter implements Function<User, UserDTO> {

//
//    public UserDTO apply(UserDTO userDTO) {
//        UserDTO userDTO = new UserDTO();
//        userDTO.setFirstName(user.getFirstName());
//        userDTO.setEmail(user.getEmail());
//        userDTO.setCart(user.getCart());
//        userDTO.setLastName(user.getLastName());
//        userDTO.setAddress(user.getAddress());
//        userDTO.setCity(user.getCity());
//        userDTO.setState(user.getState());
//        userDTO.setZip(user.getZip());
//        userDTO.setPhone(user.getPhone());
//        userDTO.setCountry(user.getCountry());
//        userDTO.setEmailVerified(user.getEmailVerified());
//        userDTO.setRegistrationDate(user.getRegistrationDate());
//        userDTO.setPassword(user.getPassword());
//
//        return userDTO;
//    }

    @Override
    public UserDTO apply(User user) {
        return null;
    }

    public UserDTO apply(UserDTO userDTO) {
        User user = new User();

        userDTO.setFirstName(user.getFirstName());
        userDTO.setEmail(user.getEmail());
        userDTO.setCart(user.getCart());
        userDTO.setLastName(user.getLastName());
        userDTO.setAddress(user.getAddress());
        userDTO.setCity(user.getCity());
        userDTO.setState(user.getState());
        userDTO.setZip(user.getZip());
        userDTO.setPhone(user.getPhone());
        userDTO.setCountry(user.getCountry());
        userDTO.setEmailVerified(user.getEmailVerified());
        userDTO.setRegistrationDate(user.getRegistrationDate());
        userDTO.setPassword(user.getPassword());

        return userDTO;

    }

    public List<UserDTO> apply(List<User> user) {

        List<UserDTO> userDTO = new ArrayList<>();
        Arrays.stream(userDTO.stream().toArray()).collect(Collectors.toList());
        return userDTO;
    }
}
