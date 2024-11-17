package com.upgrad.FoodOrderingApp.service.common;

import java.util.HashMap;
import java.util.Map;

public enum ItemNotFoundErrorCode implements ErrorCode {

  /**
   * Error message: <b>No item by this id exist</b><br>
   * <b>Cause:</b> If there are no items by the item uuid entered by the customer<br>
   * <b>Action: Provide proper Item Uuid which exist in the database</b><br>
   */
  INF_003("INF-003", "No item by this id exist");


  private static final Map<String, ItemNotFoundErrorCode> LOOKUP =
      new HashMap<String, ItemNotFoundErrorCode>();

  static {
    for (final ItemNotFoundErrorCode enumeration : ItemNotFoundErrorCode.values()) {
      LOOKUP.put(enumeration.getCode(), enumeration);
    }
  }

  private final String code;

  private final String defaultMessage;

  private ItemNotFoundErrorCode(final String code, final String defaultMessage) {
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
