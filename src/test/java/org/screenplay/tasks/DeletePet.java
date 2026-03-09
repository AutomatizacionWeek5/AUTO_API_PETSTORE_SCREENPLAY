package org.screenplay.tasks;

import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Delete;

/**
 * Task: DELETE /pet/{petId}
 * Elimina una mascota de la PetStore por su ID.
 */
public class DeletePet implements Task {

    private final long petId;

    public DeletePet(long petId) {
        this.petId = petId;
    }

    @Step("{0} elimina la mascota con id '#petId'")
    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
            Delete.from("/pet/{petId}")
                  .with(req -> req.pathParam("petId", petId))
        );
    }

    public static DeletePet withId(long petId) {
        return new DeletePet(petId);
    }
}
