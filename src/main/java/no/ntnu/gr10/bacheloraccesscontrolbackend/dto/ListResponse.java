package no.ntnu.gr10.bacheloraccesscontrolbackend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * A generic response class for paginated lists.
 *
 * @param <T> the type of the data in the list
 *
 * @author Anders Lund
 * @version 16.04.2025
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ListResponse<T> {
  @NotNull
  @NotBlank
  private List<T> data;

  @NotNull
  private int totalPages;

  @NotNull
  private long totalElements;
}

