package com.ecommerce.user.exception;

public final class ApiExceptions {
    private ApiExceptions() {}
    public static class UserNotFoundException extends RuntimeException { public UserNotFoundException(){super("User not found");} }
    public static class AddressNotFoundException extends RuntimeException { public AddressNotFoundException(){super("Address not found");} }
    public static class DuplicateEmailException extends RuntimeException { public DuplicateEmailException(){super("Email is already registered");} }
    public static class DuplicateMobileException extends RuntimeException { public DuplicateMobileException(){super("Mobile number is already registered");} }
    public static class InvalidCredentialsException extends RuntimeException { public InvalidCredentialsException(){super("Invalid email or password");} }
    public static class UserBlockedException extends RuntimeException { public UserBlockedException(){super("User account is blocked");} }
}
