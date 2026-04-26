package ru.netology.web.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class DashBoardPage {
    // находим заголовок страницы с картами, чтобы убедиться, что мы вошли
    private final SelenideElement header = $("[data-test-id=dashboard]");
    // находим все блоки с картами на странице и сохраняем их в коллекцию (список)
    private final ElementsCollection cards = $$(".list__item div");
    // находим слово "баланс"
    private final String balanceStart = "баланс: ";
    // находим символ (стоит сразу после баланса)
    private final String balanceFinish = " р.";

    public DashBoardPage() {
        // после создания объекта этой страницы, Selenide проверяет, виден ли заголовок
        header.should(Condition.visible);
    }

    // метод, который принимает номер карты по порядку (0 или 1), берет ее текст с экрана
    // и возвращает баланс в виде целого числа
    public int getCardBalance(int index) {
        var text = cards.get(index).getText();
        return extractBalance(text);
    }

    // yаходим кнопку пополнения у карты по ее индексу (0 или 1) кликаем на неё
    // и сообщаем программе, что теперь мы находимся на странице перевода TransferPage
    public TransferPage selectCardToTransfer(int index) {
        cards.get(index).$("button[data-test-id='action-deposit']").click();
        return new TransferPage();
    }

    private int extractBalance(String text) {
        // находим порядковый номер символа, где стоят слова «баланс: » и « р.».
        var start = text.indexOf(balanceStart);
        var finish = text.indexOf(balanceFinish);
        // удаляем все, что находится между этими двумя словами
        var value = text.substring(start + balanceStart.length(), finish);
        // превращаем строку с вырезанным текстом в математическое число,
        // чтобы с ним можно было делать вычисления (сравнивать, вычитать).
        return Integer.parseInt(value);
    }
}