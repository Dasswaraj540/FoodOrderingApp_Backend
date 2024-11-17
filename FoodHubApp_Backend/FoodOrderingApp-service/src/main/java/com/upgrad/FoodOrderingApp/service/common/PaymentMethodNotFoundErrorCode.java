package com.upgrad.FoodOrderingApp.service.common;

import java.util.HashMap;
import java.util.Map;

public enum PaymentMethodNotFoundErrorCode implements ErrorCode {

  /**
   * Error message: <b>No payment method found by this id</b><br>
   * <b>Cause:</b> If the payment uuid entered by the customer
   * does not match any payment method that exists in the database<br>
   * <b>Action: Provide proper contact number which exist in the database</b><br>
   */
  PNF_001("PNF-001", "Payment id field should not be empty"),

  /**
   * Error message: <b>No payment method found by this id</b><br>
   * <b>Cause:</b> If the payment uuid entered by the customer
   * does not match any payment method that exists in the database<br>
   * <b>Action: Provide proper contact number which exist in the database</b><br>
   */
  PNF_002("PNF-002", "No payment method found by this id");


  private static final Map<String, PaymentMethodNotFoundErrorCode> LOOKUP =
      new HashMap<String, PaymentMethodNotFoundErrorCode>();

  static {
    for (final PaymentMethodNotFoundErrorCode enumeration : PaymentMethodNotFoundErrorCode.values()) {
      LOOKUP.put(enumeration.getCode(), enumeration);
    }
  }

  private final String code;

  private final String defaultMessage;

  private PaymentMethodNotFoundErrorCode(final String code, final String defaultMessage) {
    this.code = code;
    this.defaultMessage = defaultMessage;
  }

  @Override
  public String getCode() {
    return code;
  }

  @Override
  public String getDefaultMessage() {
    return defaultMessage;
  }
}
