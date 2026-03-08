package com.innowise.userservice.mapper;

import com.innowise.userservice.entity.PaymentCard;
import com.innowise.userservice.dto.response.PaymentCardResponse;
import com.innowise.userservice.dto.request.CreatePaymentCardRequest;
import com.innowise.userservice.dto.request.UpdatePaymentCardRequest;

import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface PaymentCardMapper {

  PaymentCard toEntity(CreatePaymentCardRequest request);

  @Mapping(source = "user.id", target = "userId")
  PaymentCardResponse toResponse(PaymentCard card);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void updateEntityFromRequest(UpdatePaymentCardRequest request, @MappingTarget PaymentCard card);
}