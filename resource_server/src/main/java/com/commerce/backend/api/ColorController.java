package com.commerce.backend.api;

import com.commerce.backend.model.dto.ColorDTO;
import com.commerce.backend.model.response.color.ProductColorResponse;
import com.commerce.backend.service.ProductColorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class ColorController extends PublicApiController {

    private final ProductColorService productColorService;


    @Autowired
    public ColorController(ProductColorService productColorService) {
        this.productColorService = productColorService;
    }

    @PostMapping(value = "/color")
    public ResponseEntity<ProductColorResponse> addColor(@RequestBody @Valid ColorDTO colorDTO) {

        ProductColorResponse colored = productColorService.addToColor(colorDTO);
        return new ResponseEntity<>(colored, HttpStatus.OK);
    }

    @GetMapping(value = "/colors")
    public ResponseEntity<List<ProductColorResponse>> getAllColors() {
        List<ProductColorResponse> productColors = productColorService.findAll();
        return new ResponseEntity<>(productColors, HttpStatus.OK);
    }
    @PutMapping(value = "/upcolor")
    public ResponseEntity<ProductColorResponse> updateColor(@RequestBody @Valid ColorDTO colorDTO) {
        ProductColorResponse updateResponse = productColorService.updateColor(colorDTO);
        return new ResponseEntity<>(updateResponse, HttpStatus.OK);
    }
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteColor(@PathVariable("id") Long Id){
        productColorService.deleteColor(Id);
        return ResponseEntity.ok("color deleted successfully!.");
    }
}
