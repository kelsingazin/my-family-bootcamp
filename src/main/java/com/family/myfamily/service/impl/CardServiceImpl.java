package com.family.myfamily.service.impl;

import com.family.myfamily.controller.exceptions.ServiceException;
import com.family.myfamily.model.dto.CardDto;
import com.family.myfamily.model.entities.CardEntity;
import com.family.myfamily.model.entities.UserEntity;
import com.family.myfamily.payload.codes.ErrorCode;
import com.family.myfamily.payload.response.CardResponse;
import com.family.myfamily.repository.CardRepository;
import com.family.myfamily.repository.UserRepository;
import com.family.myfamily.service.CardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;

    @Override
    public CardResponse createCard(CardDto cardDto) {
        CardEntity card = modelMapper.map(cardDto, CardEntity.class);

        checkCardExistence(card);
        checkCardExpDate(card);
        setCardAlias(card);

        return modelMapper.map(cardRepository.save(card), CardResponse.class);
    }

    @Override
    public List<CardResponse> getCards(UUID userId) {
        log.info("Получение всех карт пользователя по userId");
        UserDetails contextUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<UserEntity> optionalUserEntity = userRepository.findById(userId);
        UserEntity currentUser = optionalUserEntity.orElseThrow(
                () -> ServiceException.builder()
                        .message("Пользователь с таким id не существует")
                        .errorCode(ErrorCode.NOT_EXISTS)
                        .build());
        if (contextUser.getPassword().equals(currentUser.getPassword())) {
            List<CardEntity> cardEntityList = cardRepository.findAllByUser_IdAndDeletedIsFalse(userId);

            Type listType = new TypeToken<List<CardResponse>>() {
            }.getType();

            return modelMapper.map(cardEntityList, listType);
        } else {
            throw ServiceException
                    .builder()
                    .message("Ошибка валидаций пользователя")
                    .errorCode(ErrorCode.INVALID_ARGUMENT)
                    .build();
        }
    }

    @Override
    public CardResponse getCardByCardId(UUID userId, UUID cardId) {
        log.info("Получение карты пользователя по userId и cardId");
        UserDetails contextUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<UserEntity> optionalUserEntity = userRepository.findById(userId);
        UserEntity currentUser = optionalUserEntity.orElseThrow(
                () -> ServiceException.builder()
                        .message("Пользователь с таким id не существует")
                        .errorCode(ErrorCode.NOT_EXISTS)
                        .build());
        if (contextUser.getPassword().equals(currentUser.getPassword())) {
            List<CardEntity> cardEntityList = cardRepository.findAllByUser_IdAndDeletedIsFalse(userId);
            Optional<CardEntity> cardEntity = cardEntityList.stream()
                    .filter(card -> card.getId().equals(cardId))
                    .findFirst();
            if (cardEntity.isPresent()) {
                return modelMapper.map(cardEntity.get(), CardResponse.class);
            } else {
                log.error("Карта с таким id не существует");
                throw ServiceException
                        .builder()
                        .message("Карта с таким id не существует")
                        .errorCode(ErrorCode.NOT_EXISTS)
                        .build();
            }
        } else {
            log.error("Ошибка валидаций пользователя");
            throw ServiceException
                    .builder()
                    .message("Ошибка валидаций пользователя")
                    .errorCode(ErrorCode.INVALID_ARGUMENT)
                    .build();
        }
    }

    @Override
    public HttpStatus deleteCardByCardId(UUID cardId) {
        Optional<CardEntity> optionalCardEntity = cardRepository.findByIdAndDeletedIsFalse(cardId);
        if (optionalCardEntity.isPresent()) {
            CardEntity card = optionalCardEntity.get();
            card.setDeleted(Boolean.TRUE);
            cardRepository.save(card);
            return HttpStatus.OK;
        } else {
            log.error("Карта с таким cardId не существует");
            throw ServiceException
                    .builder()
                    .errorCode(ErrorCode.NOT_EXISTS)
                    .message("Карта с таким cardId не существует")
                    .build();
        }
    }

    private void checkCardExistence(CardEntity card) {
        log.info("Проверка на существование карты в системе");
        if (card.getId() != null) {
            cardRepository.findByIdAndDeletedIsFalse(card.getId()).ifPresent(cardDb -> {
                        throw ServiceException.builder()
                                .message("Карта с таким номером уже существует")
                                .errorCode(ErrorCode.ALREADY_EXISTS)
                                .build();
                    }
            );
        }
    }

    private void checkCardExpDate(CardEntity card) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/yy");
        simpleDateFormat.setLenient(false);
        Date expiry;
        try {
            expiry = simpleDateFormat.parse(card.getExpirationDate());
        } catch (ParseException e) {
            throw ServiceException
                    .builder()
                    .errorCode(ErrorCode.EXPIRED_CARD)
                    .message("Срок действия карты должен быть в формате MM/yy")
                    .build();
        }
        if (expiry.before(new Date())) {
            throw ServiceException
                    .builder()
                    .errorCode(ErrorCode.EXPIRED_CARD)
                    .message("Срок действия карты истек")
                    .build();
        }
    }

    private void setCardAlias(CardEntity card) {
        card.setAlias(card.getNumber().substring(0, 7) + "******" + card.getNumber().substring(card.getNumber().length() - 4));
    }
}
