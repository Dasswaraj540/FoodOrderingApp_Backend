package com.upgrad.FoodOrderingApp.service.common;

import java.util.HashMap;
import java.util.Map;

public enum AddressNotFoundErrorCode implements ErrorCode {

  /**
   * Error message: <b>No state by this id</b><br>
   * <b>Cause:</b> If the state uuid entered does not exist in the database<br>
   * <b>Action: Provide proper id which exist in database</b><br>
   */
  ANF_002("ANF-002", "No state by this id"),

  /**
   * Error message: <b>No address by this id</b><br>
   * <b>Cause:</b> If address id entered is incorrect<br>
   * <b>Action: Provide proper address id</b><br>
   */
  ANF_003("ANF-003", "No address by this id"),

  /**
   * Error message: <b>Address id can not be empty</b><br>
   * <b>Cause:</b> If address id field is empty<br>
   * <b>Action: Provide proper address id, don't leave it blank</b><br>
   */
  ANF_005("ANF-005", "Address id can not be empty");


  private static final Map<String, AddressNotFoundErrorCode> LOOKUP =
      new HashMap<String, AddressNotFoundErrorCode>();

  static {
    for (final AddressNotFoundErrorCode enumeration : AddressNotFoundErrorCode.values()) {
      LOOKUP.put(enumeration.getCode(), enumeration);
    }
  }

  private final String code;

  private final String defaultMessage;

  private AddressNotFoundErrorCode(final String code, final String defaultMessage) {
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
