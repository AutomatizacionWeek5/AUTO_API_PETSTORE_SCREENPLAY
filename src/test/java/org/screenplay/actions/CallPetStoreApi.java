package org.screenplay.actions;

import net.serenitybdd.screenplay.Ability;
import net.serenitybdd.screenplay.rest.abilities.CallAnApi;

/**
 * Acción base que encapsula la capacidad de llamar a la PetStore API.
 * Centraliza la URL base para que los Tasks no la dupliquen.
 */
public class CallPetStoreApi {

    public static final String BASE_URL = "https://petstore.swagger.io/v2";

    /**
     * Devuelve la habilidad {@code CallAnApi} apuntando a la PetStore.
     * Se asigna al Actor con {@code actor.whoCan(CallPetStoreApi.asAnon())}.
     */
    public static Ability asAnon() {
        return CallAnApi.at(BASE_URL);
    }
}
