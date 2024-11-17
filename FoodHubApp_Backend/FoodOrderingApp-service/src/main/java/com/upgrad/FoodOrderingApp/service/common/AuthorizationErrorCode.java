package com.upgrad.FoodOrderingApp.service.common;

import java.util.HashMap;
import java.util.Map;

public enum AuthorizationErrorCode implements ErrorCode {

  /**
   * Error message: <b>Customer is not Logged in.</b><br>
   * <b>Cause:</b> If the access token provided by the customer does not exist in the database<br>
   * <b>Action: Provide valid access-token which is not expired or recently created</b><br>
   */
  ATHR_001("ATHR-001", "Customer is not Logged in."),

  /**
   * Error message: <b>Customer is logged out. Log in again to access this endpoint.</b><br>
   * <b>Cause:</b> If the access token provided by the customer exists in the database, but the customer has already logged out<br>
   * <b>Action: Provide recently created access-token</b><br>
   */
  ATHR_002("ATHR-002", "Customer is logged out. Log in again to access this endpoint."),

  /**
   * Error message: <b>Your session is expired. Log in again to access this endpoint.</b><br>
   * <b>Cause:</b> If the access token provided by the customer exists in the database, but the session has expired<br>
   * <b>Action: Provide recently created access-token</b><br>
   */
  ATHR_003("ATHR-003", "Your session is expired. Log in again to access this endpoint."),

  /**
   * Error message: <b>You are not authorized to view/update/delete any one else's address</b><br>
   * <b>Cause:</b> If the access token provided by the customer exists in the database,
   * but the user who has logged in is not the same user who has created the address<br>
   * <b>Action: Login with proper user to authorize view/update/delete actions</b><br>
   */
  ATHR_004("ATHR-004", "You are not authorized to view/update/delete any one else's address");


  private static final Map<String, AuthorizationErrorCode> LOOKUP =
      new HashMap<String, AuthorizationErrorCode>();

  static {
    for (final AuthorizationErrorCode enumeration : AuthorizationErrorCode.values()) {
      LOOKUP.put(enumeration.getCode(), enumeration);
    }
  }

  private final String code;

  private final String defaultMessage;

  private AuthorizationErrorCode(final String code, final String defaultMessage) {
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
