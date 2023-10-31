package com.commerce.backend.service;

import com.commerce.backend.converter.color.ProductColorResponseConverter;
import com.commerce.backend.dao.ColorRepository;
import com.commerce.backend.error.exception.ResourceNotFoundException;
import com.commerce.backend.model.dto.ColorDTO;
import com.commerce.backend.model.entity.Color;
import com.commerce.backend.model.request.color.UpdateColorRequest;
import com.commerce.backend.model.response.color.ProductColorResponse;
import com.commerce.backend.service.cache.ProductColorCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class ProductColorServiceImpl implements ProductColorService {

    private final ProductColorCacheService productColorCacheService;
    private final ProductColorResponseConverter productColorResponseConverter;
    private final ColorRepository colorRepository;

    @Autowired
    public ProductColorServiceImpl(ProductColorCacheService productColorCacheService, ProductColorResponseConverter productColorResponseConverter, ColorRepository colorRepository) {
        this.productColorCacheService = productColorCacheService;
        this.productColorResponseConverter = productColorResponseConverter;
        this.colorRepository = colorRepository;
    }


    @Override
    public List<ProductColorResponse> findAll() {
        List<Color> productColors = productColorCacheService.findAll();
        if (productColors.isEmpty()) {
            throw new ResourceNotFoundException("Could not find product colors");
        }
        return productColors
                .stream()
                .map(productColorResponseConverter)
                .collect(Collectors.toList());
    }


    public ProductColorResponse addToColor(ColorDTO colorDTO) {
//        if (colorDTO.getName() == null || colorDTO.getHex() == null) {
//            try {
//                throw new Exception("Name or hex is missing");
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//        }
        Color color = new Color();
        color.setName(colorDTO.getName());
        color.setHex(colorDTO.getHex());
        color = colorRepository.save(color);
        return productColorResponseConverter.apply(color);
    }

    @Override
    public ProductColorResponse updateColor(ColorDTO colorDTO) {
        Color color = getColor();
        color.setId(color.getId());
        color.setName(color.getName());
        color.setHex(color.getHex());
        colorRepository.save(color);
        return productColorResponseConverter.apply(color);
    }

    private Color getColor() {
        Optional<Color> color = Optional.of(new Color());
        if (!Objects.isNull(color.isPresent())) {
            colorRepository.findAll();
            if (color.isEmpty()) {
                throw new ResourceNotFoundException("User not found");
            }
            return color.get();
        } else {
            throw new AccessDeniedException("Invalid access");
        }
    }
}

