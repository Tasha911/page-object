package ru.netology.web.test;

import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.DashBoardPage;
import ru.netology.web.page.LoginPage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.web.data.DataHelper.getAuthInfo;

public class MoneyTransferTest {

    @BeforeAll
    static void beforeAll() {
        // получаем из DataHelper логин и пароль (vasya/qwerty123)
        var info = getAuthInfo();
        // получаем из DataHelper секретный код для этого пользователя (12345)
        var verificationCode = DataHelper.getVerificationCodeFor(info);

        // открываем браузер и сразу инициализируем страницу логина
        var loginPage = Selenide.open("http://localhost:9999", LoginPage.class);
        // вводим логин/пароль и нажимаем "Продолжить" - метод возвращает страницу ввода кода
        var verificationPage = loginPage.validLogin(info);
        // вводим SMS-код и нажимаем "Подтвердить" - попадаем на главную страницу с картами
        verificationPage.validVerify(verificationCode);
    }

    @Test
        void shouldTransferMoneyPositive() {
        var dashBoardPage = new DashBoardPage();

        // для перевода берем из DataHelper переменные с информацией о первой карте (номер с которого преводить)
        var firstCardInfo = DataHelper.getFirstCardInfo();

        // вызываем метод из DashBoardPage для переменных баланса - считываем текущие балансы карт
        var firstCardBalance = dashBoardPage.getCardBalance(0);
        var secondCardBalance = dashBoardPage.getCardBalance(1);

        // позитивный тест - сумма меньше остатка
        int amount = firstCardBalance / 2;

        // вычисляем сколько должно остаться на первой карте
        var expectedBalanceFirstCard = firstCardBalance - amount;
        // вычисляем, сколько должно стать на второй карте
        var expectedBalanceSecondCard = secondCardBalance + amount;

        // нажимаем кнопку "Пополнить" у второй карты, открывается страница перевода - выполняем перевод на вторую карту
        var transferPage = dashBoardPage.selectCardToTransfer(1);
        // вводим сумму, номер первой карты и жмем "Пополнить"
        dashBoardPage = transferPage.makeTransfer(String.valueOf(amount), firstCardInfo);

        // вызываем метод из DashBoardPage для переменных баланса - считываем изменившиеся балансы карт
        var actualBalanceFirstCard = dashBoardPage.getCardBalance(0);
        var actualBalanceSecondCard = dashBoardPage.getCardBalance(1);

        // сравниваем с тем сколько должно было остаться (стр.40,42) с тем, что реально показал сайт
        assertEquals(expectedBalanceFirstCard, actualBalanceFirstCard);
        assertEquals(expectedBalanceSecondCard, actualBalanceSecondCard);
    }

    @Test
    void shouldNotTransferIfAmountMoreThanBalance() {
        var dashBoardPage = new DashBoardPage();
        var firstCardInfo = DataHelper.getFirstCardInfo();

        // вызываем метод из DashBoardPage для переменных баланса - считываем текущие балансы карт
        var firstCardBalance = dashBoardPage.getCardBalance(0);
        var secondCardBalance = dashBoardPage.getCardBalance(1);

        // негативный тест - сумма больше остатка на 500
        int amount = firstCardBalance + 500;

        // нажимаем кнопку "Пополнить" у второй карты, открывается страница перевода - выполняем перевод на вторую карту
        var transferPage = dashBoardPage.selectCardToTransfer(1);
        // вводим сумму, номер первой карты и жмем "Пополнить"
        dashBoardPage = transferPage.makeTransfer(String.valueOf(amount), firstCardInfo);

        // проверяем, что балансы НЕ изменились
        assertEquals(firstCardBalance, dashBoardPage.getCardBalance(0));
        assertEquals(secondCardBalance, dashBoardPage.getCardBalance(1));
    }
}