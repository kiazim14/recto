package com.commerce.backend.model.dto;

import com.commerce.backend.model.entity.CartItem;
import com.commerce.backend.model.entity.Discount;
import com.commerce.backend.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartDTO {
    //private User user;
    //private List<CartItem> cartItemList;
    //private Discount discount;
    private Float totalCartPrice;
    private Float totalCargoPrice;
    private Float totalPrice;
    private Date dateCreated;
}
