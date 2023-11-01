package com.commerce.backend.service;

import com.commerce.backend.converter.color.ProductColorResponseConverter;
import com.commerce.backend.dao.ColorRepository;
import com.commerce.backend.error.exception.ResourceNotFoundException;
import com.commerce.backend.model.dto.ColorDTO;
import com.commerce.backend.model.entity.Cart;
import com.commerce.backend.model.entity.Color;
import com.commerce.backend.model.entity.ProductVariant;
import com.commerce.backend.model.response.cart.CartResponse;
import com.commerce.backend.model.response.color.ProductColorResponse;
import com.commerce.backend.service.cache.ProductColorCacheService;
import com.github.javafaker.Faker;
import org.hibernate.Session;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class ProductColorServiceImplTest {

    @InjectMocks
    private ProductColorServiceImpl productColorService;

    @Mock
    private ProductColorCacheService productColorCacheService;

    @Mock
    private ProductColorResponseConverter productColorResponseConverter;

    private Faker faker;
    private ColorRepository colorRepository;

    @BeforeEach
    public void setUp() {
        faker = new Faker();
    }

    @Test
    void it_should_add_to_color_when_color_is_empty() {

        // given
        String name = faker.color().name();
        String hex = faker.color().hex();

        Color color = new Color();
        color.setName(name);
        color.setHex(hex);

        ColorDTO colorDTO = new ColorDTO();
        colorDTO.setName(faker.color().name());
        colorDTO.setHex(faker.color().hex());

        ProductColorResponse productColorResponse = new ProductColorResponse();

        given(productColorService.addToColor(colorDTO)).willReturn(productColorResponse);
        given(colorRepository.save(color)).willReturn(color);
        given(productColorResponseConverter.apply(color)).willReturn(productColorResponse);

        // when
        ProductColorResponse colorResponseResult = productColorService.addToColor(colorDTO);

        // then
        then(colorResponseResult).isEqualTo(productColorResponse);

    }


    @Test
    void it_should_find_all_product_colors() {

        // given
        String colorName = faker.color().name();
        Color color = new Color();
        color.setName(colorName);
        ProductColorResponse productColorResponse = new ProductColorResponse();
        productColorResponse.setColor(ColorDTO.builder().name(colorName).build());

        List<Color> colorList = Stream.generate(() -> color)
                .limit(faker.number().randomDigitNotZero())
                .collect(Collectors.toList());

        given(productColorCacheService.findAll()).willReturn(colorList);
        given(productColorResponseConverter.apply(any(Color.class))).willReturn(productColorResponse);

        // when
        List<ProductColorResponse> productColorResponseList = productColorService.findAll();

        // then
        then(productColorResponseList.size()).isEqualTo(colorList.size());
        productColorResponseList.forEach(productColorResponse1 -> then(productColorResponse1.getColor().getName()).isEqualTo(color.getName()));

    }

    @Test
    void it_should_throw_exception_when_no_color() {

        // given
        given(productColorCacheService.findAll()).willReturn(Collections.emptyList());

        // when, then
        assertThatThrownBy(() -> productColorService.findAll())
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Could not find product colors");

    }

}