package com.example.library.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO classes for Borrower requests and responses.
 *
 */
public class BorrowerDto {

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class BorrowerRequest {

		@NotBlank(message = "Name is required")
		private String name;

		@NotBlank(message = "Email is required")
		@Email(message = "Invalid email format")
		private String email;
	}

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class BorrowerResponse {
		private Long id;
		private String code;
		private String name;
		private String email;
	}
}
