package com.pedro.azure_ad_sso_implementation.application.controller.book;

import com.pedro.azure_ad_sso_implementation.application.controller.book.dto.BookResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/books")
public class BookController {

    @GetMapping
    public ResponseEntity<BookResponseDTO> getBook(){
        return ResponseEntity.ok(new BookResponseDTO());
    }

}
