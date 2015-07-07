package pl.java.scalatech.web.advice.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class ErrorHolderFieldDetails extends ErrorHolderDetails {

    private static final long serialVersionUID = 2569997101700570516L;
    private List<FieldErrorDTO> fieldErrors = new ArrayList<>();
    // for global errors
    private List<String> errors;

    public void addFieldError(String path, String message) {
        FieldErrorDTO error = new FieldErrorDTO(path, message);
        fieldErrors.add(error);
    }
}
