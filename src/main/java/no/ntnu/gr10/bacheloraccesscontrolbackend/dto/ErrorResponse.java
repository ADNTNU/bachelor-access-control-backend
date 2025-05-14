package no.ntnu.gr10.bacheloraccesscontrolbackend.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ErrorResponse is a DTO class that represents an error response.
 * It contains a single field, message, which is a string that describes the error.
 *
 * @author Anders Lund
 * @version 09.04.2025
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ErrorResponse {
  @NotNull
  @NotBlank
  private  String message;
}
