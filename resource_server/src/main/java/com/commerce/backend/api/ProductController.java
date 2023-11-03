package com.commerce.backend.api;

import com.commerce.backend.error.exception.InvalidArgumentException;
import com.commerce.backend.model.dto.ProductDTO;
import com.commerce.backend.model.entity.Product;
import com.commerce.backend.model.request.order.PostOrderRequest;
import com.commerce.backend.model.response.order.OrderResponse;
import com.commerce.backend.model.response.product.ProductDetailsResponse;
import com.commerce.backend.model.response.product.ProductResponse;
import com.commerce.backend.model.response.product.ProductVariantResponse;
import com.commerce.backend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

@RestController
public class ProductController extends PublicApiController {

    private final ProductService productService;


    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping(value = "/product/produced")
    public ResponseEntity<Product> postOrder(@RequestBody @Valid ProductDTO productDTO) {
        Product productor = productService.addToProduct(productDTO);
        return new ResponseEntity<>(productor, HttpStatus.OK);
    }

    @GetMapping(value = "/product")
    public ResponseEntity<List<ProductVariantResponse>> getAll(@RequestParam("page") Integer page,
                                                               @RequestParam("size") Integer pageSize,
                                                               @RequestParam(value = "sort", required = false) String sort,
                                                               @RequestParam(value = "category", required = false) String category,
                                                               @RequestParam(value = "minPrice", required = false) Float minPrice,
                                                               @RequestParam(value = "maxPrice", required = false) Float maxPrice,
                                                               @RequestParam(value = "color", required = false) String color) {
        if (Objects.isNull(page) || page < 0) {
            throw new InvalidArgumentException("Invalid page");
        }
        if (Objects.isNull(pageSize) || pageSize < 0) {
            throw new InvalidArgumentException("Invalid pageSize");
        }
        List<ProductVariantResponse> products = productService.getAll(page, pageSize, sort, category, minPrice, maxPrice, color);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping(value = "/product/count")
    public ResponseEntity<Long> getAllCount(@RequestParam(value = "category", required = false) String category,
                                            @RequestParam(value = "minPrice", required = false) Float minPrice,
                                            @RequestParam(value = "maxPrice", required = false) Float maxPrice,
                                            @RequestParam(value = "color", required = false) String color) {
        Long productCount = productService.getAllCount(category, minPrice, maxPrice, color);
        return new ResponseEntity<>(productCount, HttpStatus.OK);
    }

    @GetMapping(value = "/product/{url}")
    public ResponseEntity<ProductDetailsResponse> getByUrl(@PathVariable("url") String url) {
        if (url.isBlank()) {
            throw new InvalidArgumentException("Invalid url params");
        }
        ProductDetailsResponse productDetailsResponse = productService.findByUrl(url);
        return new ResponseEntity<>(productDetailsResponse, HttpStatus.OK);
    }

    @GetMapping(value = "/product/related/{url}")
    public ResponseEntity<List<ProductResponse>> getByRelated(@PathVariable("url") String url) {
        if (url.isBlank()) {
            throw new InvalidArgumentException("Invalid url params");
        }
        List<ProductResponse> products = productService.getRelatedProducts(url);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping(value = "/product/recent")
    public ResponseEntity<List<ProductResponse>> getByNewlyAdded() {
        List<ProductResponse> products = productService.getNewlyAddedProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping(value = "/product/mostselling")
    public ResponseEntity<List<ProductVariantResponse>> getByMostSelling() {
        List<ProductVariantResponse> products = productService.getMostSelling();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping(value = "/product/interested")
    public ResponseEntity<List<ProductResponse>> getByInterested() {
        List<ProductResponse> products = productService.getInterested();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping(value = "/product/search")
    public ResponseEntity<List<ProductResponse>> searchProduct(@RequestParam("page") Integer page,
                                                               @RequestParam("size") Integer size,
                                                               @RequestParam("keyword") String keyword) {
        List<ProductResponse> products = productService.searchProductDisplay(keyword, page, size);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }
}
