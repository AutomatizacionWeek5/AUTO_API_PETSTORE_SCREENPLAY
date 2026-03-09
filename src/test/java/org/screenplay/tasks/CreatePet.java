package org.screenplay.tasks;

import io.restassured.http.ContentType;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Post;
import org.screenplay.model.Pet;

/**
 * Task: POST /pet
 * Crea una nueva mascota en la PetStore y deja la respuesta disponible
 * para que las Questions la consulten después.
 */
public class CreatePet implements Task {

    private final Pet pet;

    public CreatePet(Pet pet) {
        this.pet = pet;
    }

    @Step("{0} crea una nueva mascota llamada '#pet.name'")
    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
            Post.to("/pet")
                .with(req -> req
                    .contentType(ContentType.JSON)
                    .body(pet)
                    .log().all())
        );
    }

    public static CreatePet withData(Pet pet) {
        return new CreatePet(pet);
    }
}
