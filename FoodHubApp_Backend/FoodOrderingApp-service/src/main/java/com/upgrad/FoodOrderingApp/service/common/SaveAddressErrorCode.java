package com.upgrad.FoodOrderingApp.service.common;

import java.util.HashMap;
import java.util.Map;

public enum SaveAddressErrorCode implements ErrorCode {

  /**
   * Error message: <b>No field can be empty</b><br>
   * <b>Cause:</b> If any field is empty<br>
   * <b>Action: Provide proper values in all fields</b><br>
   */
  SAR_001("SAR-001", "No field can be empty"),

  /**
   * Error message: <b>Invalid pincode</b><br>
   * <b>Cause:</b> If the pincode entered is invalid
   * (i.e it does not include only numbers or its size is not six)<br>
   * <b>Action: Provide proper pincode</b><br>
   */
  SAR_002("SAR-002", "Invalid pincode");


  private static final Map<String, SaveAddressErrorCode> LOOKUP =
      new HashMap<String, SaveAddressErrorCode>();

  static {
    for (final SaveAddressErrorCode enumeration : SaveAddressErrorCode.values()) {
      LOOKUP.put(enumeration.getCode(), enumeration);
    }
  }

  private final String code;

  private final String defaultMessage;

  private SaveAddressErrorCode(final String code, final String defaultMessage) {
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
