package com.family.myfamily.controller.rest;

import com.family.myfamily.model.dto.CardDto;
import com.family.myfamily.payload.response.CardResponse;
import com.family.myfamily.service.CardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/cards")
@RequiredArgsConstructor
@Slf4j
public class CardController {

    private final CardService cardService;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public CardResponse createCard(@RequestBody CardDto cardDto) {
        return cardService.createCard(cardDto);
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public List<CardResponse> getCards(@PathVariable UUID userId) {
        return cardService.getCards(userId);
    }

    @GetMapping("/user/{userId}/card/{cardId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public CardResponse getCard(@PathVariable UUID userId,
                                @PathVariable UUID cardId) {
        return cardService.getCardByCardId(userId, cardId);
    }

    @DeleteMapping("/card/{cardId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public HttpStatus deleteCard(@PathVariable UUID cardId) {
        return cardService.deleteCardByCardId(cardId);
    }
}
