package id.co.softwaredeveloperstoday.cms.dashboard.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ResponseGeneratedPdfDto {

    private String orderNumber;
    private Long generatedDate;
    private byte[] pdfFile;

}
