package pl.java.scalatech.web.advice.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class FieldErrorDTO implements Serializable {

    private static final long serialVersionUID = -1159831713017695763L;
    @Getter
    private String field;
    @Getter
    private String message;

}
