#language: en
Feature: Gestión de mascotas en PetStore API
  Como usuario de la PetStore API
  Quiero gestionar mascotas mediante operaciones CRUD
  Para verificar la integridad del servicio REST

  @crud @crear
  Scenario: Crear una nueva mascota via POST
    Given el actor está configurado para llamar a la PetStore API
    When el actor crea una mascota con nombre "Toby Marin" y estado "available"
    Then el status code de la respuesta debe ser 200

  @crud @consultar
  Scenario: Consultar una mascota existente via GET
    Given el actor está configurado para llamar a la PetStore API
    When el actor crea una mascota con nombre "Toby Marin" y estado "available"
    And el actor consulta la mascota creada
    Then el status code de la respuesta debe ser 200
    And el nombre de la mascota debe ser "Toby Marin"
    And el estado de la mascota debe ser "available"

  @crud @actualizar
  Scenario: Actualizar una mascota existente via PUT
    Given el actor está configurado para llamar a la PetStore API
    When el actor crea una mascota con nombre "Toby Marin" y estado "available"
    And el actor actualiza la mascota con nombre "Toby Marin Actualizado" y estado "sold"
    Then el status code de la respuesta debe ser 200
    And el nombre de la mascota debe ser "Toby Marin Actualizado"
    And el estado de la mascota debe ser "sold"

  @crud @eliminar
  Scenario: Eliminar una mascota existente via DELETE
    Given el actor está configurado para llamar a la PetStore API
    When el actor crea una mascota con nombre "Toby Marin" y estado "available"
    And el actor elimina la mascota
    Then el status code de la respuesta debe ser 200

  @crud @flujo-completo
  Scenario: Flujo completo CRUD de una mascota
    Given el actor está configurado para llamar a la PetStore API

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
