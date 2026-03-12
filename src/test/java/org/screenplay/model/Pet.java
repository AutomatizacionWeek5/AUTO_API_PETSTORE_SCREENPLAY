package org.screenplay.model;

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

    public static Pet called(String name) {
        Pet pet = new Pet();
        pet.name   = name;
        pet.status = "available";
        return pet;
    }

    public Pet withId(long id) {
        this.id = id;
        return this;
    }

    public Pet withStatus(String status) {
        this.status = status;
        return this;
    }

    @Override
    public String toString() {
        return "Pet{id=" + id + ", name='" + name + "', status='" + status + "'}";
    }
}
