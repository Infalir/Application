package com.innowise.userservice.dto.request;

import lombok.Data;

@Data
public class PaymentCardFilterRequest {
  private String holder;
  private Boolean active;
}
