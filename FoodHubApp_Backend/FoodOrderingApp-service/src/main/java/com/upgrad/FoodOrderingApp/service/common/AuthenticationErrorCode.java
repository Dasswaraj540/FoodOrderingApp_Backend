package com.upgrad.FoodOrderingApp.service.common;

import java.util.HashMap;
import java.util.Map;

public enum AuthenticationErrorCode implements ErrorCode {

  /**
   * Error message: <b>This contact number has not been registered!</b><br>
   * <b>Cause:</b> If the contact number provided by the customer does not exist<br>
   * <b>Action: Provide proper contact number which exist in the database</b><br>
   */
  ATH_001("ATH-001", "This contact number has not been registered!"),

  /**
   * Error message: <b>Invalid Credentials</b><br>
   * <b>Cause:</b> If the password provided by the customer does not match the password in the existing database.<br>
   * <b>Action: Provide proper password which matches with the database</b><br>
   */
  ATH_002("ATH-002", "Invalid Credentials"),

  /**
   * Error message: <b>Incorrect format of decoded customer name and password</b><br>
   * <b>Cause:</b> If the Basic authentication is not provided incorrect format.<br>
   * <b>Action: Provide authorization header base64 encoded with format of contact_number:password</b><br>
   */
  ATH_003("ATH-003", "Incorrect format of decoded customer name and password");


  private static final Map<String, AuthenticationErrorCode> LOOKUP =
      new HashMap<String, AuthenticationErrorCode>();

  static {
    for (final AuthenticationErrorCode enumeration : AuthenticationErrorCode.values()) {
      LOOKUP.put(enumeration.getCode(), enumeration);
    }
  }

  private final String code;

  private final String defaultMessage;

  private AuthenticationErrorCode(final String code, final String defaultMessage) {
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
