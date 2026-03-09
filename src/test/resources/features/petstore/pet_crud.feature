#language: en
Feature: Gestión de mascotas en PetStore API
  Como usuario de la PetStore API
  Quiero gestionar mascotas mediante operaciones CRUD
  Para verificar la integridad del servicio REST

  Scenario: Flujo completo CRUD de una mascota
    Given el actor está configurado para llamar a la PetStore API

    When el actor crea una mascota con nombre "Firulais" y estado "available"
    Then el status code de la respuesta debe ser 200

    When el actor consulta la mascota creada
    Then el status code de la respuesta debe ser 200
    And el nombre de la mascota debe ser "Firulais"
    And el estado de la mascota debe ser "available"

    When el actor actualiza la mascota con nombre "Firulais Actualizado" y estado "sold"
    Then el status code de la respuesta debe ser 200
    And el nombre de la mascota debe ser "Firulais Actualizado"
    And el estado de la mascota debe ser "sold"

    When el actor elimina la mascota
    Then el status code de la respuesta debe ser 200
