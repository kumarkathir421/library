package com.collabera.library.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO classes for Book requests and responses.
 *
 */
public class BookDto {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BookRequest {
        @NotBlank(message = "ISBN is required")
        private String isbn;

        @NotBlank(message = "Title is required")
        private String title;

        @NotBlank(message = "Author is required")
        private String author;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BookResponse {
        private Long id;
        private String code;
        private String isbn;
        private String title;
        private String author;
        private Long borrowedById;  // null if not borrowed
        private String borrowedByCode;
    }
}
