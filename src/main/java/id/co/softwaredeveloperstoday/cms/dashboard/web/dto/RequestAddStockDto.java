package id.co.softwaredeveloperstoday.cms.dashboard.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestAddStockDto {

    @NotNull
    private Long id;
    @NotNull
    private int addedStock;

}
