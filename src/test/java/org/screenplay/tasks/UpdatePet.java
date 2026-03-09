package org.screenplay.tasks;

import io.restassured.http.ContentType;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Put;
import org.screenplay.model.Pet;

/**
 * Task: PUT /pet
 * Actualiza los datos de una mascota existente.
 */
public class UpdatePet implements Task {

    private final Pet pet;

    public UpdatePet(Pet pet) {
        this.pet = pet;
    }

    @Step("{0} actualiza la mascota '#pet.name' con estado '#pet.status'")
    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
            Put.to("/pet")
               .with(req -> req
                   .contentType(ContentType.JSON)
                   .body(pet))
        );
    }

    public static UpdatePet withData(Pet pet) {
        return new UpdatePet(pet);
    }
}
