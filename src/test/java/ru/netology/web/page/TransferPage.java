package ru.netology.web.page;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.Keys;
import ru.netology.web.data.DataHelper;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class TransferPage {
    private final SelenideElement amountInput = $("[data-test-id='amount'] input");
    private final SelenideElement fromInput = $("[data-test-id='from'] input");
    private final SelenideElement transferButton = $("[data-test-id='action-transfer']");

    public TransferPage() {
        amountInput.shouldBe(visible);
    }

    public DashBoardPage makeTransfer(String amount, DataHelper.CardInfo cardInfo) {
        amountInput.sendKeys(Keys.chord(Keys.CONTROL,"a", Keys.DELETE));
        amountInput.setValue(amount);

        fromInput.sendKeys(Keys.chord(Keys.CONTROL,"a", Keys.DELETE));
        fromInput.setValue(cardInfo.getCardNumber());

        transferButton.click();
        return new DashBoardPage();
    }
}