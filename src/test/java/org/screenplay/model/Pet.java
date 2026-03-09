package org.screenplay.model;

/**
 * Modelo de datos que representa una mascota en la PetStore API.
 * Usa un fluent builder para facilitar la construcción legible en los tests.
 */
public class Pet {

    private Long id;
    private String name;
    private String status;

    private Pet() {}

    // ── Getters ──────────────────────────────────────────────────────────────
    public Long getId()     { return id;     }
    public String getName() { return name;   }
    public String getStatus() { return status; }

    // ── Factory / fluent builder ──────────────────────────────────────────────

    /** Crea una mascota con nombre y estado "available" por defecto. */
    public static Pet called(String name) {
        Pet pet = new Pet();
        pet.name   = name;
        pet.status = "available";
        return pet;
    }

    /** Encadena un ID a la mascota (útil al actualizar). */
    public Pet withId(long id) {
        this.id = id;
        return this;
    }

    /** Encadena un estado personalizado. */
    public Pet withStatus(String status) {
        this.status = status;
        return this;
    }

    @Override
    public String toString() {
        return "Pet{id=" + id + ", name='" + name + "', status='" + status + "'}";
    }
}
