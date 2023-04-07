package com.family.myfamily.payload.response;

import com.family.myfamily.model.dto.CityDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CitiesResponse {

    private Double sum;
    private List<CityDto> cityDtoLis;
}
