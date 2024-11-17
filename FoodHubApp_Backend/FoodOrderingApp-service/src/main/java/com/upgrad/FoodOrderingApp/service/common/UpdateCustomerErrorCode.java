package com.upgrad.FoodOrderingApp.service.common;

import java.util.HashMap;
import java.util.Map;

public enum UpdateCustomerErrorCode implements ErrorCode {

  /**
   * Error message: <b>Weak password!</b><br>
   * <b>Cause:</b> If the new password provided by the customer is weak, i.e.,
   * it doesn’t have at least eight characters and does not contain at least one digit,
   * one uppercase letter, and one of the following characters [#@$%&*!^] <br>
   * <b>Action: Provide valid password matching all requirements</b><br>
   */
  UCR_001("UCR-001", "Weak password!"),

  /**
   * Error message: <b>First name field should not be empty</b><br>
   * <b>Cause:</b> If firstname field is empty.<br>
   * <b>Action: Provide proper First name field value</b><br>
   */
  UCR_002("UCR-002", "First name field should not be empty"),

  /**
   * Error message: <b>No field should be empty</b><br>
   * <b>Cause:</b> If the old or new password field is empty.<br>
   * <b>Action: Provide value on all required fields</b><br>
   */
  UCR_003("UCR-003", "No field should be empty"),

  /**
   * Error message: <b>Incorrect old password!</b><br>
   * <b>Cause:</b> If the old password field entered is incorrect.<br>
   * <b>Action: Old Password provided should match with the value in the database</b><br>
   */
  UCR_004("UCR-004", "Incorrect old password!");


  private static final Map<String, UpdateCustomerErrorCode> LOOKUP =
      new HashMap<String, UpdateCustomerErrorCode>();

  static {
    for (final UpdateCustomerErrorCode enumeration : UpdateCustomerErrorCode.values()) {
      LOOKUP.put(enumeration.getCode(), enumeration);
    }
  }

  private final String code;

  private final String defaultMessage;

  private UpdateCustomerErrorCode(final String code, final String defaultMessage) {
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
