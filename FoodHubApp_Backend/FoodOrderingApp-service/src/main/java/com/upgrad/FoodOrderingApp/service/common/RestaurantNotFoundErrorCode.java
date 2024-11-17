package com.upgrad.FoodOrderingApp.service.common;

import java.util.HashMap;
import java.util.Map;

public enum RestaurantNotFoundErrorCode implements ErrorCode {

  /**
   * Error message: <b>No restaurant by this id</b><br>
   * <b>Cause:</b> If there is no restaurant by the restaurant uuid entered by the customer<br>
   * <b>Action: Provide proper Uuid of the restaurant which present in database</b><br>
   */
  RNF_001("RNF-001", "No restaurant by this id"),

  /**
   * Error message: <b>Restaurant id field should not be empty</b><br>
   * <b>Cause:</b> If the restaurant id field entered by the customer is empty<br>
   * <b>Action: Please provide restaurant id it should not be left blank</b><br>
   */
  RNF_002("RNF-002", "Restaurant id field should not be empty"),

  /**
   * Error message: <b>Restaurant name field should not be empty</b><br>
   * <b>Cause:</b> If the restaurant name field entered by the customer is empty<br>
   * <b>Action: Provide proper restaurant name, don't leave it blank</b><br>
   */
  RNF_003("RNF-003", "Restaurant name field should not be empty");


  private static final Map<String, RestaurantNotFoundErrorCode> LOOKUP =
      new HashMap<String, RestaurantNotFoundErrorCode>();

  static {
    for (final RestaurantNotFoundErrorCode enumeration : RestaurantNotFoundErrorCode.values()) {
      LOOKUP.put(enumeration.getCode(), enumeration);
    }
  }

  private final String code;

  private final String defaultMessage;

  private RestaurantNotFoundErrorCode(final String code, final String defaultMessage) {
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
