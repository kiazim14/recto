package com.commerce.backend.service;

import com.commerce.backend.model.dto.ColorDTO;
import com.commerce.backend.model.request.color.UpdateColorRequest;
import com.commerce.backend.model.response.color.ProductColorResponse;

import java.util.List;

public interface ProductColorService {
    List<ProductColorResponse> findAll();

    ProductColorResponse addToColor(ColorDTO colorDTO);


    ProductColorResponse updateColor(ColorDTO colorDTO);
}
