package com.upgrad.FoodOrderingApp.service.common;

import java.util.HashMap;
import java.util.Map;

public enum InvalidRatingErrorCode implements ErrorCode {

  /**
   * Error message: <b>Restaurant should be in the range of 1 to 5</b><br>
   * <b>Cause:</b> If the customer rating field entered by the
   * customer is empty or is not in the range of 1 to 5<br>
   * <b>Action: Provide proper rating in the range of 1-5</b><br>
   */
  IRE_001("IRE-001", "Restaurant should be in the range of 1 to 5");


  private static final Map<String, InvalidRatingErrorCode> LOOKUP =
      new HashMap<String, InvalidRatingErrorCode>();

  static {
    for (final InvalidRatingErrorCode enumeration : InvalidRatingErrorCode.values()) {
      LOOKUP.put(enumeration.getCode(), enumeration);
    }
  }

  private final String code;

  private final String defaultMessage;

  private InvalidRatingErrorCode(final String code, final String defaultMessage) {
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
