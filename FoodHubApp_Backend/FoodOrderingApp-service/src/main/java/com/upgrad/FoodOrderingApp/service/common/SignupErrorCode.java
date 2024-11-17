package com.upgrad.FoodOrderingApp.service.common;

import java.util.HashMap;
import java.util.Map;

public enum SignupErrorCode implements ErrorCode {

  /**
   * Error message: <b>This contact number is already registered! Try other contact number</b><br>
   * <b>Cause:</b> If the contact number provided already exists in the current database.<br>
   * <b>Action: Try again with another contact number</b><br>
   */
  SGR_001("SGR-001", "This contact number is already registered! Try other contact number"),

  /**
   * Error message: <b>Invalid email-id format!</b><br>
   * <b>Cause:</b> If the email Id provided by the user is not proper format of a standard emalid.<br>
   * <b>Action: Try again with proper email Id format</b><br>
   */
  SGR_002("SGR-002", "Invalid email-id format!"),

  /**
   * Error message: <b>Invalid contact number!</b><br>
   * <b>Cause:</b> If the contact number provided by the customer is not in correct format,
   * i.e., it does not contain
   * only numbers and has more or less than 10 digits<br>
   * <b>Action: Try again with proper contact number format</b><br>
   */
  SGR_003("SGR-003", "Invalid contact number!"),

  /**
   * Error message: <b>Weak password!</b><br>
   * <b>Cause:</b> If the password provided by the customer is weak, i.e.,
   * it doesn’t have at least eight characters and does not contain at least one digit,
   * one uppercase letter, and one of the following characters [#@$%&*!^] <br>
   * <b>Action: All criteria for strong password should be satified</b><br>
   */
  SGR_004("SGR-004", "Weak password!"),

  /**
   * Error message: <b>Except last name all fields should be filled</b><br>
   * <b>Cause:</b> If any field other than last name is empty<br>
   * <b>Action: Provide all fields except lastname, where lastname is optional</b><br>
   */
  SGR_005("SGR-005", "Except last name all fields should be filled");

  private static final Map<String, SignupErrorCode> LOOKUP =
      new HashMap<String, SignupErrorCode>();

  static {
    for (final SignupErrorCode enumeration : SignupErrorCode.values()) {
      LOOKUP.put(enumeration.getCode(), enumeration);
    }
  }

  private final String code;

  private final String defaultMessage;

  private SignupErrorCode(final String code, final String defaultMessage) {
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
