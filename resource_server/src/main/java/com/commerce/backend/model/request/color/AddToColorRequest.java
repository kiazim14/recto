package com.commerce.backend.model.request.color;

import lombok.Data;

@Data
public class AddToColorRequest {
    private String name;
    private String hex;
}
