package pl.java.scalatech.error;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorStatus {
    private HttpStatus status;
    private String message;
    private String url;
}
