package com.commerce.backend.model.dto;

import com.commerce.backend.model.entity.ProductCategory;
import com.commerce.backend.model.entity.ProductVariant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDTO {

    private String sku;
    private String name;
    private String url;
    private String longDesc;
    private Date dateCreated;
    private Date lastUpdated;
    private Integer unlimited;
    private ProductCategory productCategory;
    private List<ProductVariant> productVariantList;

}
