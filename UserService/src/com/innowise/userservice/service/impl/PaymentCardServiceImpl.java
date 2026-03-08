package com.innowise.userservice.service.impl;

import com.innowise.userservice.dto.request.CreatePaymentCardRequest;
import com.innowise.userservice.dto.request.PaymentCardFilterRequest;
import com.innowise.userservice.dto.request.UpdatePaymentCardRequest;
import com.innowise.userservice.dto.response.PaymentCardResponse;
import com.innowise.userservice.entity.PaymentCard;
import com.innowise.userservice.entity.User;
import com.innowise.userservice.exception.CardLimitExceededException;
import com.innowise.userservice.exception.DuplicateResourceException;
import com.innowise.userservice.exception.ResourceNotFoundException;
import com.innowise.userservice.mapper.PaymentCardMapper;
import com.innowise.userservice.repository.PaymentCardRepository;
import com.innowise.userservice.repository.PaymentCardSpecification;
import com.innowise.userservice.repository.UserRepository;
import com.innowise.userservice.service.PaymentCardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentCardServiceImpl implements PaymentCardService {

  private static final int MAX_CARDS_PER_USER = 5;

  private final PaymentCardRepository cardRepository;
  private final UserRepository userRepository;
  private final PaymentCardMapper cardMapper;

  @Override
  @Transactional
  public PaymentCardResponse createCard(Long userId, CreatePaymentCardRequest request) {
    User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User", userId));

    long cardCount = cardRepository.countCardsByUserId(userId);
    if (cardCount >= MAX_CARDS_PER_USER) {
      throw new CardLimitExceededException(userId);
    }

    if (cardRepository.existsByNumber(request.getNumber())) {
      throw new DuplicateResourceException("Card with number " + request.getNumber() + " already exists");
    }

    PaymentCard card = cardMapper.toEntity(request);
    card.setUser(user);
    PaymentCard saved = cardRepository.save(card);
    log.info("Created card with id: {} for user: {}", saved.getId(), userId);
    return cardMapper.toResponse(saved);
  }

  @Override
  public PaymentCardResponse getCardById(Long id) {
    log.info("Fetching card with id: {} (cache miss)", id);
    return cardMapper.toResponse(findCardOrThrow(id));
  }

  @Override
  @Transactional(readOnly = true)
  public Page<PaymentCardResponse> getAllCards(PaymentCardFilterRequest filter, Pageable pageable) {
    Specification<PaymentCard> spec = Specification
            .where(PaymentCardSpecification.hasHolder(filter.getHolder()))
            .and(PaymentCardSpecification.isActive(filter.getActive()));
    return cardRepository.findAll(spec, pageable).map(cardMapper::toResponse);
  }

  @Override
  @Transactional(readOnly = true)
  public List<PaymentCardResponse> getCardsByUserId(Long userId) {
    if (!userRepository.existsById(userId)) {
      throw new ResourceNotFoundException("User", userId);
    }
    return cardRepository.findByUserId(userId).stream().map(cardMapper::toResponse).toList();
  }

  @Override
  @Transactional
  public PaymentCardResponse updateCard(Long id, UpdatePaymentCardRequest request) {
    PaymentCard card = findCardOrThrow(id);
    cardMapper.updateEntityFromRequest(request, card);
    PaymentCard updated = cardRepository.save(card);
    log.info("Updated card with id: {}", id);
    return cardMapper.toResponse(updated);
  }

  @Override
  @Transactional
  public void activateCard(Long id) {
    findCardOrThrow(id);
    cardRepository.updateActiveStatus(id, true);
    log.info("Activated card with id: {}", id);
  }

  @Override
  @Transactional
  public void deactivateCard(Long id) {
    findCardOrThrow(id);
    cardRepository.updateActiveStatus(id, false);
    log.info("Deactivated card with id: {}", id);
  }

  @Override
  @Transactional
  public void deleteCard(Long id) {
    PaymentCard card = findCardOrThrow(id);
    cardRepository.delete(card);
    log.info("Deleted card with id: {}", id);
  }

  private PaymentCard findCardOrThrow(Long id) {
    return cardRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("PaymentCard", id));
  }
}
