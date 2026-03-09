package org.screenplay.tasks;

import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Get;

/**
 * Task: GET /pet/{petId}
 * Consulta una mascota existente por su ID.
 */
public class GetPet implements Task {

    private final long petId;

    public GetPet(long petId) {
        this.petId = petId;
    }

    @Step("{0} consulta la mascota con id '#petId'")
    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
            Get.resource("/pet/{petId}")
               .with(req -> req.pathParam("petId", petId))
        );
    }

    public static GetPet withId(long petId) {
        return new GetPet(petId);
    }
}
