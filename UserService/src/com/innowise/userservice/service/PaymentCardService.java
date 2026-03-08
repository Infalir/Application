package com.innowise.userservice.service;

import com.innowise.userservice.dto.request.CreatePaymentCardRequest;
import com.innowise.userservice.dto.request.PaymentCardFilterRequest;
import com.innowise.userservice.dto.request.UpdatePaymentCardRequest;
import com.innowise.userservice.dto.response.PaymentCardResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PaymentCardService {
  PaymentCardResponse createCard(Long userId, CreatePaymentCardRequest request);
  PaymentCardResponse getCardById(Long id);
  Page<PaymentCardResponse> getAllCards(PaymentCardFilterRequest filter, Pageable pageable);
  List<PaymentCardResponse> getCardsByUserId(Long userId);
  PaymentCardResponse updateCard(Long id, UpdatePaymentCardRequest request);
  void activateCard(Long id);
  void deactivateCard(Long id);
  void deleteCard(Long id);
}
