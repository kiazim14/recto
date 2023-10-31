package com.commerce.backend.model.request.color;

import lombok.Data;

@Data
public class UpdateColorRequest {
    private Long id;
    private String name;
    private String hex;
}
