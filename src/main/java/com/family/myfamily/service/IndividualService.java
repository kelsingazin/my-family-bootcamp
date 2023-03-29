package com.family.myfamily.service;

import com.family.myfamily.model.dto.IndividualDto;

import java.util.UUID;

public interface IndividualService {

    IndividualDto save(IndividualDto individualDto);

    IndividualDto getIndividual(UUID userId);
}
