package id.co.softwaredeveloperstoday.cms.dashboard.web.util.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDataDto {

    private String acknowledge;
    private Integer responseCode;
    private String responseMessage;

}
