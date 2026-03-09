package org.screenplay.tests;

import net.serenitybdd.junit5.SerenityJUnit5Extension;
import net.serenitybdd.screenplay.Actor;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.screenplay.actions.CallPetStoreApi;
import org.screenplay.model.Pet;
import org.screenplay.questions.TheResponse;
import org.screenplay.tasks.*;

import static net.serenitybdd.screenplay.GivenWhenThen.*;
import static org.hamcrest.Matchers.equalTo;

/**
 * Test E2E: Ciclo completo CRUD sobre la PetStore Swagger API.
 *
 * Flujo: POST → GET → PUT → DELETE
 *
 * Patrón Screenplay:
 *   Actor  → quien ejecuta las acciones (el "tester")
 *   Tasks  → CreatePet, GetPet, UpdatePet, DeletePet  (qué hace)
 *   Questions → TheResponse  (qué pregunta sobre el resultado)
 */
@ExtendWith(SerenityJUnit5Extension.class)
@DisplayName("PetStore API - Ciclo Completo CRUD")
class PetStoreCrudTest {

    private Actor actor;

    // ID único basado en timestamp para evitar colisiones con otros usuarios del API demo
    private static final long PET_ID = System.currentTimeMillis() % 9_000_000L + 1_000_000L;

    @BeforeEach
    void setUp() {
        actor = Actor.named("Tester")
                     .whoCan(CallPetStoreApi.asAnon());
    }

    @Test
    @DisplayName("Flujo completo: crear → consultar → actualizar → eliminar una mascota")
    void shouldPerformFullCrudCycle() {

        // ════════════════════════════════════════════════════════════════
        // POST /pet  →  Crear mascota con ID controlado
        // ════════════════════════════════════════════════════════════════
        Pet newPet = Pet.called("Firulais").withId(PET_ID);

        givenThat(actor).attemptsTo(
            CreatePet.withData(newPet)
        );

        then(actor).should(
            seeThat("el Status Code del POST es 200",
                    TheResponse.statusCode(), equalTo(200))
        );

        // ════════════════════════════════════════════════════════════════
        // GET /pet/{PET_ID}  →  Consultar mascota recién creada
        // ════════════════════════════════════════════════════════════════
        when(actor).attemptsTo(
            GetPet.withId(PET_ID)
        );

        then(actor).should(
            seeThat("el Status Code del GET es 200",
                    TheResponse.statusCode(), equalTo(200)),
            seeThat("el nombre de la mascota es correcto",
                    TheResponse.petName(), equalTo("Firulais")),
            seeThat("el estado inicial de la mascota es 'available'",
                    TheResponse.petStatus(), equalTo("available"))
        );

        // ════════════════════════════════════════════════════════════════
        // PUT /pet  →  Actualizar nombre y estado de la mascota
        // ════════════════════════════════════════════════════════════════
        Pet updatedPet = Pet.called("Firulais Actualizado")
                            .withId(PET_ID)
                            .withStatus("sold");

        when(actor).attemptsTo(
            UpdatePet.withData(updatedPet)
        );

        then(actor).should(
            seeThat("el Status Code del PUT es 200",
                    TheResponse.statusCode(), equalTo(200)),
            seeThat("el nuevo nombre fue guardado correctamente",
                    TheResponse.petName(), equalTo("Firulais Actualizado")),
            seeThat("el nuevo estado es 'sold'",
                    TheResponse.petStatus(), equalTo("sold"))
        );

        // ════════════════════════════════════════════════════════════════
        // DELETE /pet/{PET_ID}  →  Eliminar mascota
        // ════════════════════════════════════════════════════════════════
        when(actor).attemptsTo(
            DeletePet.withId(PET_ID)
        );

        then(actor).should(
            seeThat("el Status Code del DELETE es 200",
                    TheResponse.statusCode(), equalTo(200))
        );
    }
}
