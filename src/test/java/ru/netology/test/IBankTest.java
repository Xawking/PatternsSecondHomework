package ru.netology.test;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataGen;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class IBankTest {
    @BeforeEach
    void setUp() {
        open("http://localhost:9999");
    }

    @Test
    void shouldEnter() {
        var registeredUser = DataGen.Registration.getRegisteredUser("active");
        $("[data-test-id='login'] input").setValue(registeredUser.getLogin());
        $("[data-test-id='password'] input").setValue(registeredUser.getPassword());
        $("[data-test-id='action-login']").click();
        $("h2").shouldHave(Condition.exactText("Личный кабинет"));

    }

    @Test
    void blockedUser() {
        DataGen.RegistrationDto registeredUser = DataGen.Registration.getRegisteredUser("blocked");
        $("[data-test-id='login'] input").setValue(registeredUser.getLogin());
        $("[data-test-id='password'] input").setValue(registeredUser.getPassword());
        $("[data-test-id='action-login']").click();
        $(".notification").shouldHave(Condition.text("Пользователь заблокирован"));

    }

    @Test
    void notRegistered() {
        DataGen.RegistrationDto randomUser = DataGen.Registration.getUser("active");
        $("[data-test-id='login'] input").setValue(randomUser.getLogin());
        $("[data-test-id='password'] input").setValue(randomUser.getPassword());
        $("[data-test-id='action-login']").click();
        $(".notification").shouldHave(Condition.text("Неверно указан логин или пароль"));

    }

    @Test
    void registeredWrongPassword() {
        DataGen.RegistrationDto registeredUser = DataGen.Registration.getRegisteredUser("active");
        $("[data-test-id='login'] input").setValue(registeredUser.getLogin());
        $("[data-test-id='password'] input").setValue("12345");
        $("[data-test-id='action-login']").click();
        $(".notification").shouldHave(Condition.text("Неверно указан логин или пароль"));

    }

    @Test
    void registeredWrongLogin() {
        DataGen.RegistrationDto registeredUser = DataGen.Registration.getRegisteredUser("active");
        DataGen.RegistrationDto randomUser = DataGen.Registration.getUser("active");
        $("[data-test-id='login'] input").setValue(randomUser.getLogin());
        $("[data-test-id='password'] input").setValue(registeredUser.getPassword());
        $("[data-test-id='action-login']").click();
        $(".notification").shouldHave(Condition.text("Неверно указан логин или пароль"));

    }

    @Test
    void emptyFields() {
        $("[data-test-id='action-login']").click();
        $("[data-test-id='login'] .input__sub").shouldHave(Condition.text("Поле обязательно для заполнения"));
        $("[data-test-id='password'] .input__sub").shouldHave(Condition.text("Поле обязательно для заполнения"));

    }
}
