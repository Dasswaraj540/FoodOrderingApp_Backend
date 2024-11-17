package com.upgrad.FoodOrderingApp.service.exception;

import com.upgrad.FoodOrderingApp.service.common.SignupErrorCode;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * SignUpRestrictedException is thrown when a customer is restricted
 * to register in the application due to Validation or Data Inconsistency issues.
 */
public class SignUpRestrictedException extends Exception {
    private final String code;
    private final String errorMessage;

    public SignUpRestrictedException(final SignupErrorCode errorCode) {
        this.code = errorCode.getCode();
        this.errorMessage = errorCode.getDefaultMessage();
    }

    public SignUpRestrictedException(final String code, final String errorMessage){
        this.code = code;
        this.errorMessage = errorMessage;
    }

    @Override
    public void printStackTrace() {
        super.printStackTrace();
    }

    @Override
    public void printStackTrace(PrintStream s) {
        super.printStackTrace(s);
    }

    @Override
    public void printStackTrace(PrintWriter s) {
        super.printStackTrace(s);
    }

    public String getCode() {
        return code;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

}

