package org.duckdns.androidghost77.gamelove.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
public class GameRequest {
    @NotEmpty
    private String name;
    @NotEmpty
    private String company;
}
