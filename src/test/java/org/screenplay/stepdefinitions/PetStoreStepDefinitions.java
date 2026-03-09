package org.screenplay.stepdefinitions;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.*;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.actors.OnlineCast;
import org.screenplay.actions.CallPetStoreApi;
import org.screenplay.model.Pet;
import org.screenplay.questions.TheResponse;
import org.screenplay.tasks.*;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * Step Definitions que conectan los pasos Gherkin del .feature
 * con las Tasks, Questions y el Actor del patrón Screenplay.
 *
 * Flujo: cada escenario obtiene un Actor fresco desde OnStage.
 * El petId se genera por instancia para evitar colisiones en la API demo.
 */
public class PetStoreStepDefinitions {

    // ID fijo para poder buscar la mascota en Swagger UI durante la depuración.
    // Cámbialo a System.currentTimeMillis() % 9_000_000L + 1_000_000L en CI/CD
    // para evitar colisiones con otros usuarios de la API demo.
    private final long petId = 99999L;

    // ── Lifecycle ─────────────────────────────────────────────────────────────

    @Before
    public void prepararEscenario() {
        OnStage.setTheStage(new OnlineCast());
    }

    @After
    public void cerrarEscenario() {
        OnStage.drawTheCurtain();
    }

    // ── Given ─────────────────────────────────────────────────────────────────

    @Given("el actor está configurado para llamar a la PetStore API")
    public void elActorEstaConfiguradoParaLlamarALaPetStoreAPI() {
        OnStage.theActorCalled("Tester")
               .whoCan(CallPetStoreApi.asAnon());
    }

    // ── When ──────────────────────────────────────────────────────────────────

    @When("el actor crea una mascota con nombre {string} y estado {string}")
    public void elActorCreaUnaMascota(String nombre, String estado) {
        Pet pet = Pet.called(nombre).withId(petId).withStatus(estado);
        actor().attemptsTo(CreatePet.withData(pet));
    }

    @When("el actor consulta la mascota creada")
    public void elActorConsultaLaMascotaCreada() {
        actor().attemptsTo(GetPet.withId(petId));
    }

    @When("el actor actualiza la mascota con nombre {string} y estado {string}")
    public void elActorActualizaLaMascota(String nombre, String estado) {
        Pet updatedPet = Pet.called(nombre).withId(petId).withStatus(estado);
        actor().attemptsTo(UpdatePet.withData(updatedPet));
    }

    @When("el actor elimina la mascota")
    public void elActorEliminaLaMascota() {
        actor().attemptsTo(DeletePet.withId(petId));
    }

    // ── Then ──────────────────────────────────────────────────────────────────

    @Then("el status code de la respuesta debe ser {int}")
    public void elStatusCodeDebeSerX(int statusEsperado) {
        actor().should(
            seeThat("el status code", TheResponse.statusCode(), equalTo(statusEsperado))
        );
    }

    @Then("el nombre de la mascota debe ser {string}")
    public void elNombreDeLaMascotaDebeSer(String nombreEsperado) {
        actor().should(
            seeThat("el nombre de la mascota", TheResponse.petName(), equalTo(nombreEsperado))
        );
    }

    @Then("el estado de la mascota debe ser {string}")
    public void elEstadoDeLaMascotaDebeSer(String estadoEsperado) {
        actor().should(
            seeThat("el estado de la mascota", TheResponse.petStatus(), equalTo(estadoEsperado))
        );
    }

    // ── Helper ────────────────────────────────────────────────────────────────

    private Actor actor() {
        return OnStage.theActorInTheSpotlight();
    }
}
