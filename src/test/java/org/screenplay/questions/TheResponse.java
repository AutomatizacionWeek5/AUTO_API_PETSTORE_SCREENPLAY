package org.screenplay.questions;

import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Question;

/**
 * Questions sobre la última respuesta HTTP recibida.
 * Cada método estático crea una Question tipada que el Actor puede evaluar.
 */
public class TheResponse {

    /** Status code HTTP de la última respuesta (ej. 200, 404). */
    public static Question<Integer> statusCode() {
        return actor -> SerenityRest.lastResponse().statusCode();
    }

    /** Campo "id" del JSON de respuesta, parseado como Long. */
    public static Question<Long> petId() {
        return actor -> SerenityRest.lastResponse().jsonPath().getLong("id");
    }

    /** Campo "name" del JSON de respuesta. */
    public static Question<String> petName() {
        return actor -> SerenityRest.lastResponse().jsonPath().getString("name");
    }

    /** Campo "status" del JSON de respuesta. */
    public static Question<String> petStatus() {
        return actor -> SerenityRest.lastResponse().jsonPath().getString("status");
    }

    /** Cuerpo completo de la respuesta como String (útil para debugging). */
    public static Question<String> body() {
        return actor -> SerenityRest.lastResponse().body().asString();
    }
}
