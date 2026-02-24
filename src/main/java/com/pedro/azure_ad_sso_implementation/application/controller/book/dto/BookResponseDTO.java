package com.pedro.azure_ad_sso_implementation.application.controller.book.dto;
import lombok.Data;

@Data
public class BookResponseDTO {
    private String name;
    private String isbn;
    private String genere;
    private int size;
}
