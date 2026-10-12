package id.co.softwaredeveloperstoday.cms.dashboard.web.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProcessedGeneratedPdfDto extends ResponseGeneratedPdfDto {

    private String processedTemplateEngine;

    public ProcessedGeneratedPdfDto(String orderNumber, Long generatedDate, byte[] pdfFile, String processedTemplateEngine) {
        super(orderNumber, generatedDate, pdfFile);
        this.processedTemplateEngine = processedTemplateEngine;
    }
}
