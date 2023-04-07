package com.family.myfamily.mapper;

import com.family.myfamily.model.dto.OfficeDto;
import com.family.myfamily.model.entities.OfficeEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OfficeMapper {

    public OfficeDto toDto(OfficeEntity officeEntity) {
        return OfficeDto.builder()
                .id(officeEntity.getId())
                .name(officeEntity.getName())
                .build();
    }

    public List<OfficeDto> officeDtoList(List<OfficeEntity> lIst) {
        return lIst.stream().map(this::toDto).collect(Collectors.toList());
    }
}
