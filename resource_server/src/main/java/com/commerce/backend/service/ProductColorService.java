package com.commerce.backend.service;

import com.commerce.backend.model.dto.ColorDTO;
import com.commerce.backend.model.entity.Color;
import com.commerce.backend.model.response.color.ProductColorResponse;

import java.util.List;

public interface ProductColorService {
    List<ProductColorResponse> findAll();

    ProductColorResponse addToColor(ColorDTO colorDTO);


    ProductColorResponse updateColor(ColorDTO colorDTO);

    Color getColor();

    void deleteColor(Long id);

    Object addToColor(String hex, String name);
}
