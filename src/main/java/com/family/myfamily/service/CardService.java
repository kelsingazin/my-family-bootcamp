package com.family.myfamily.service;

import com.family.myfamily.model.dto.CardDto;
import com.family.myfamily.payload.response.CardResponse;
import org.springframework.http.HttpStatus;

import java.text.ParseException;
import java.util.List;
import java.util.UUID;

public interface CardService {

    CardResponse createCard(CardDto cardDto) throws ParseException;

    List<CardResponse> getCards(UUID userId);

    CardResponse getCardByCardId(UUID userId, UUID cardId);

    HttpStatus deleteCardByCardId(UUID cardId);
}
