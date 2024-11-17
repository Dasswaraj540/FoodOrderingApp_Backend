package com.upgrad.FoodOrderingApp.service.businness;

import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UtilityProvider {


    public static Predicate<String> isValidEmail = (email) ->{
        String emailRegex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(emailRegex);
    };

    public static  Predicate<String> isValidMobileNumber = (mobno) ->{
        Pattern mobileNoPatter = Pattern.compile("[0-9]{10}");
        Matcher matcher = mobileNoPatter.matcher(mobno);
        return (matcher.find() &&
                matcher.group().equals(mobno));
    };

    private static  Predicate<String> minimumOneNumberCheck = (value) ->{
        return value.matches("(?=.*[0-9]).*");
    };

    private static  Predicate<String> minimumOneUppercaseCheck = (value) ->{
        return value.matches("(?=.*[A-Z]).*");
    };

    private static  Predicate<String> minimumOneSpecialCharacterCheck = (value) ->{
        return value.matches("(?=.*[#@$%&*!^]).*");
    };

    public static Predicate<String> isValidPassword = (password) ->{
       return  password.length() >= 8 &&
               minimumOneNumberCheck.test(password) &&
               minimumOneUppercaseCheck.test(password) &&
               minimumOneSpecialCharacterCheck.test(password);
    };

    public static boolean isPincodeValid(String pincode) {
        Pattern pincodePattern = Pattern.compile("\\d{6}\\b");
        Matcher matcher = pincodePattern.matcher(pincode);
        return (matcher.find() && matcher.group().equals(pincode));
    }
}
