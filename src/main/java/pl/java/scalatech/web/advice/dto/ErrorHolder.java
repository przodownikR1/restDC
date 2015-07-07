package pl.java.scalatech.web.advice.dto;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.http.HttpStatus;

@XmlRootElement(name = "errorHolder")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorHolder implements Serializable {
    private static final long serialVersionUID = 2864366350107886961L;
    private HttpStatus status;
    private String errorCode;
    private String errorMessage;
    private String url;
    private String enityName;
    private Long entityId;

}
