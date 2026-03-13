package org.screenplay.interactions;

import net.serenitybdd.screenplay.Ability;
import net.serenitybdd.screenplay.rest.abilities.CallAnApi;

public class CallPetStoreApi {

    public static final String BASE_URL = "https://petstore.swagger.io/v2";

    public static Ability asAnon() {
        return CallAnApi.at(BASE_URL);
    }
}
        