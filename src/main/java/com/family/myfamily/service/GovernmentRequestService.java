package com.family.myfamily.service;

import com.family.myfamily.model.dto.CityDto;
import com.family.myfamily.model.dto.GovernmentRequestDto;
import com.family.myfamily.payload.request.ConfirmMarriage;
import com.family.myfamily.payload.request.RegisterCouple;
import com.family.myfamily.payload.response.Check;
import com.family.myfamily.payload.response.MarriageResponse;

import java.util.List;
import java.util.UUID;

public interface GovernmentRequestService {
    Check registerCouple(RegisterCouple request);

    Check confirmMarriage(ConfirmMarriage request);

    List<GovernmentRequestDto> getAllRequests(UUID id);

    MarriageResponse getAllCities();
}
