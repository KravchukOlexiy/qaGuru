package ru.guru.qa;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selectors.byXpath;
import static com.codeborne.selenide.Selenide.*;

public class FirstTest {

    @BeforeAll
    static void beforeAll() {
        Configuration.startMaximized = true;
    }

    @Test
    void shouldOpen() {
        String[] date = chooseRandomDate();
        String day = date[0];
        String month_year = date[1];
        String year = date[2];

        String studentName, studentLastName, studentEmail, gender, mobile, dateOfBirth, subjects, hobbies, address, state, city;

        studentName = "Petr";
        studentLastName = "Suslikov";
        studentEmail = "Test@yandex.ru";
        gender = "Male";
        mobile = "7915208217";
        dateOfBirth = day + " " + month_year + "," + year;
        subjects = "English";
        hobbies = "Reading";
        address = "Working with the radio button was difficult";
        state = "Haryana";
        city = "Karnal";

        open("https://demoqa.com/automation-practice-form");

        $("#firstName").setValue(studentName);
        $("#lastName").setValue(studentLastName);
        $("#userEmail").setValue(studentEmail);
        $(byText(gender)).click();
        $("#userNumber").setValue(mobile);
        $("#dateOfBirthInput").click();
        $(".react-datepicker__year-select").selectOption(year);
        $(".react-datepicker__month-select").selectOption(month_year);
        $(byText(day)).click();
        executeJavaScript(
                "$('.react-datepicker').hide();"
        );

        $(".subjects-auto-complete__value-container").click();
        $("#subjectsInput").setValue(subjects);
        $("#react-select-2-option-0").click();
        $(byText(hobbies)).click();
        $("#uploadPicture").uploadFromClasspath("kit.jpg");
        $("#currentAddress").setValue(address);
        $("#stateCity-wrapper").scrollIntoView(true);
        $("#stateCity-wrapper").click();
        $(byText(state)).click();
        $("#city").click();
        $(byText(city)).click();

        $("#submit").click();


        $(".fade.modal.show").shouldBe(Condition.visible);
        compareValuesWithWeb(studentName + " " + studentLastName, studentEmail, gender, mobile,
                dateOfBirth, subjects, hobbies, "kit.jpg", address, state + " " + city);
    }

    private String[] chooseRandomDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");

        LocalDate start = LocalDate.of(1950, Month.JANUARY, 1);
        long days = ChronoUnit.DAYS.between(start, LocalDate.now());
        LocalDate randomDate = start.plusDays(new Random().nextInt((int) days + 1));

        String date = randomDate.format(formatter);
        String splitter[] = date.split(" ");

        if (splitter[0].startsWith("0")) {
            splitter[0] = splitter[0].replace("0", "");
        }

        return splitter;
    }

    private void compareValuesWithWeb(String studentName, String studentEmail, String gender, String mobile,
                                               String dateOfBirth, String subjects, String hobbies, String picture, String address, String stateAndcity) {
        ArrayList newCustomerValues = new ArrayList();

        List<String> valuesList = Arrays.asList(studentName, studentEmail, gender, mobile, dateOfBirth, subjects, hobbies, picture, address, stateAndcity);
        newCustomerValues.addAll(valuesList);

        for (int i = 1; i < 8; i++) {
            $(byXpath("//tr[" + i + "]//td[2]"))
                        .shouldHave(text(newCustomerValues.get(i-1).toString()));
        }
    }
}
