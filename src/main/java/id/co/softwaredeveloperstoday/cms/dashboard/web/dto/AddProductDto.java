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
public class AddProductDto {

    @NotNull
    @NotBlank
    @NotEmpty
    private String name;
    @NotNull
    private CategoryDto categoryDto;
    private String code;
    private String description;
    @NotNull
    private Long stock;
    private String unit;
    @NotNull
    private Double pricePerUnit;
    private byte[] image;

}
