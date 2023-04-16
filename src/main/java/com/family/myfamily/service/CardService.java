package com.family.myfamily.service;

import com.family.myfamily.model.dto.CardDto;
import com.family.myfamily.model.enums.MaritalStatus;
import com.family.myfamily.payload.response.CardResponse;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

public interface CardService {

    CardResponse createCard(CardDto cardDto);

    List<CardResponse> getCards(UUID userId);

    CardResponse getCardByCardId(UUID userId, UUID cardId);

    HttpStatus deleteCardByCardId(UUID cardId);
}
