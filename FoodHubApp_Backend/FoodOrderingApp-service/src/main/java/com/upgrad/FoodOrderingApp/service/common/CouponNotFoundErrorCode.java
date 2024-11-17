package com.upgrad.FoodOrderingApp.service.common;

import java.util.HashMap;
import java.util.Map;

public enum CouponNotFoundErrorCode implements ErrorCode {

  /**
   * Error message: <b>No coupon by this name</b><br>
   * <b>Cause:</b> If the coupon name entered by the customer does not match any coupon that exists in the database<br>
   * <b>Action: Provide proper coupon code which exist in the database</b><br>
   */
  CPF_001("CPF-001", "No coupon by this name"),

  /**
   * Error message: <b>Coupon name field should not be empty</b><br>
   * <b>Cause:</b> If the coupon name entered by the customer is empty<br>
   * <b>Action: Coupon name field should not be left blank</b><br>
   */
  CPF_002("CPF-002", "Coupon name field should not be empty"),

  /**
   * Error message: <b>Coupon name field should not be empty</b><br>
   * <b>Cause:</b> If the coupon name entered by the customer is empty<br>
   * <b>Action: Coupon name field should not be left blank</b><br>
   */
  CPF_002_ID("CPF-002", "No coupon by this id");


  private static final Map<String, CouponNotFoundErrorCode> LOOKUP =
      new HashMap<String, CouponNotFoundErrorCode>();

  static {
    for (final CouponNotFoundErrorCode enumeration : CouponNotFoundErrorCode.values()) {
      LOOKUP.put(enumeration.getCode(), enumeration);
    }
  }

  private final String code;

  private final String defaultMessage;

  private CouponNotFoundErrorCode(final String code, final String defaultMessage) {
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
