package ru.netology.web.data;

import lombok.Value;

public class DataHelper {

    // приватный конструктор, чтобы нельзя было создавать объекты этого класса
    private DataHelper() {
    }

    // метод должен возвращать объект AuthInfo
    public static AuthInfo getAuthInfo() {
        return new AuthInfo("vasya", "qwerty123");
    }

    public static AuthInfo getOtherAuthInfo(AuthInfo original) {
        return new AuthInfo("petya", "123qwerty");
    }

    public static VerificationCode getVerificationCodeFor(AuthInfo authInfo) {
        return new VerificationCode("12345");
    }

    // метод для получения данных первой карты
    public static CardInfo getFirstCardInfo() {
        return new CardInfo("5559 0000 0000 0001");
    }

    // метод для получения данных второй карты
    public static CardInfo getSecondCardInfo() {
        return new CardInfo("5559 0000 0000 0002");
    }

    @Value
    // класс, который аписыватинформацию о карте
    public static class CardInfo {
        String cardNumber;
    }

    @Value
    public static class AuthInfo {
        String login;
        String password;
    }

    @Value
    public static class VerificationCode {
        String code;
    }

}