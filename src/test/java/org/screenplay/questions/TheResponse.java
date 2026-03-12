package org.screenplay.questions;

import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Question;

public class TheResponse {

    public static Question<Integer> statusCode() {
        return actor -> SerenityRest.lastResponse().statusCode();
    }

    public static Question<Long> petId() {
        return actor -> SerenityRest.lastResponse().jsonPath().getLong("id");
    }

    public static Question<String> petName() {
        return actor -> SerenityRest.lastResponse().jsonPath().getString("name");
    }

    public static Question<String> petStatus() {
        return actor -> SerenityRest.lastResponse().jsonPath().getString("status");
    }

    public static Question<String> body() {
        return actor -> SerenityRest.lastResponse().body().asString();
    }
}
