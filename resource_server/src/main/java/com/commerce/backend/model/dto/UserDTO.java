package com.commerce.backend.model.dto;

import com.commerce.backend.model.entity.Cart;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {

    private Cart cart;

    private String email;

    private String password;

    private String firstName;

    private String lastName;

    private String city;

    private String state;

    private String zip;

    private Integer emailVerified;

    private Date registrationDate;

    private String phone;

    private String country;

    private String address;
}
