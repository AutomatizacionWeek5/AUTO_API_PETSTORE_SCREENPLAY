#language: en
Feature: Gestión de mascotas en PetStore API - Operaciones CRUD
  Como usuario de la PetStore API
  Quiero gestionar mascotas mediante operaciones CRUD individuales
  Para verificar cada operación REST de forma aislada

  Background:
    Given el actor está configurado para llamar a la PetStore API

  @crud @crear
  Scenario Outline: Crear una nueva mascota via POST
    When el actor crea una mascota con nombre "<nombre>" y estado "<estado>"
    Then el status code de la respuesta debe ser 200

    Examples:
      | nombre     | estado    |
      | maxii      | available |
      | Rex Garcia | pending   |

  @crud @consultar
  Scenario Outline: Consultar una mascota existente via GET
    When el actor crea una mascota con nombre "<nombre>" y estado "<estado>"
    And el actor consulta la mascota creada
    Then el status code de la respuesta debe ser 200
    And el nombre de la mascota debe ser "<nombre>"
    And el estado de la mascota debe ser "<estado>"

    Examples:
      | nombre     | estado    |
      | maxii      | available |
      | Rex Garcia | pending   |

  @crud @actualizar
  Scenario Outline: Actualizar una mascota existente via PUT
    When el actor crea una mascota con nombre "<nombre_original>" y estado "<estado_original>"
    And el actor actualiza la mascota con nombre "<nombre_actualizado>" y estado "<estado_actualizado>"
    Then el status code de la respuesta debe ser 200
    And el nombre de la mascota debe ser "<nombre_actualizado>"
    And el estado de la mascota debe ser "<estado_actualizado>"

    Examples:
      | nombre_original | estado_original | nombre_actualizado     | estado_actualizado |
      | maxii           | available       | maxii Actualizado      | sold               |
      | Rex Garcia      | pending         | Rex Garcia Actualizado | sold               |

  @crud @eliminar
  Scenario Outline: Eliminar una mascota existente via DELETE
    When el actor crea una mascota con nombre "<nombre>" y estado "<estado>"
    And el actor elimina la mascota
    Then el status code de la respuesta debe ser 200

    Examples:
      | nombre                 | estado |
      | maxii Actualizado      | sold   |
      | Rex Garcia Actualizado | sold   |
