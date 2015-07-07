package pl.java.scalatech.web.advice.dto;

import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;

@Data
@XmlRootElement(name = "errorHolderDetails")
public class ErrorHolderDetails extends ErrorHolder {

    private static final long serialVersionUID = 3273079325495869947L;
    private String helpLink;
    private String messasge4Developers;

}
