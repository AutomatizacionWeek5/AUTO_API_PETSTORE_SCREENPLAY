#language: en
Feature: Flujo CRUD completo de mascotas en PetStore API
  Como usuario de la PetStore API
  Quiero ejecutar un flujo completo de creación, consulta, actualización y eliminación
  Para verificar el ciclo de vida completo de una mascota

  Background:
    Given el actor está configurado para llamar a la PetStore API

  @e2e @flujo-completo
  Scenario: Flujo completo CRUD de una mascota
    When el actor crea una mascota con nombre "Toby Marin" y estado "available"
    Then el status code de la respuesta debe ser 200

    When el actor consulta la mascota creada
    Then el status code de la respuesta debe ser 200
    And el nombre de la mascota debe ser "Toby Marin"
    And el estado de la mascota debe ser "available"

    When el actor actualiza la mascota con nombre "Toby Marin Actualizado" y estado "sold"
    Then el status code de la respuesta debe ser 200
    And el nombre de la mascota debe ser "Toby Marin Actualizado"
    And el estado de la mascota debe ser "sold"

    When el actor elimina la mascota
    Then el status code de la respuesta debe ser 200
