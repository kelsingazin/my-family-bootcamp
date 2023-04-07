package com.family.myfamily.mapper;

import com.family.myfamily.model.dto.CityDto;
import com.family.myfamily.model.entities.CityEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CityMapper {

    private final OfficeMapper officeMapper;

    public CityDto toDto(CityEntity city) {
        return CityDto.builder().
                id(city.getId())
                .name(city.getName())
                .officeList(officeMapper.officeDtoList(city.getOffices()))
                .build();
    }

    public List<CityDto> cityDtoList(List<CityEntity> list) {
        return list.stream().map(this::toDto).collect(Collectors.toList());
    }
}
