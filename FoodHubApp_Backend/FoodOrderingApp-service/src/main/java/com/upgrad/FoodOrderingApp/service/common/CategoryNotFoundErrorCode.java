package com.upgrad.FoodOrderingApp.service.common;

import java.util.HashMap;
import java.util.Map;

public enum CategoryNotFoundErrorCode implements ErrorCode {

  /**
   * Error message: <b>Category id field should not be empty</b><br>
   * <b>Cause:</b> If the category id field entered by the customer is empty<br>
   * <b>Action: Provide proper Category id, it should not be left blank</b><br>
   */
  CNF_001("CNF-001", "Category id field should not be empty"),

  /**
   * Error message: <b>No category by this id</b><br>
   * <b>Cause:</b> If there is no category by the uuid entered by the customer<br>
   * <b>Action: Provide proper Category Uuid/Id which exist in the database</b><br>
   */
  CNF_002("CNF-002", "No category by this id");


  private static final Map<String, CategoryNotFoundErrorCode> LOOKUP =
      new HashMap<String, CategoryNotFoundErrorCode>();

  static {
    for (final CategoryNotFoundErrorCode enumeration : CategoryNotFoundErrorCode.values()) {
      LOOKUP.put(enumeration.getCode(), enumeration);
    }
  }

  private final String code;

  private final String defaultMessage;

  private CategoryNotFoundErrorCode(final String code, final String defaultMessage) {
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
