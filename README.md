# AUTO_API_PETSTORE_SCREENPLAY

Automatización de API REST utilizando el patrón **Screenplay** sobre la [PetStore Swagger API](https://petstore.swagger.io).  
Valida la integridad de los servicios REST ejecutando un flujo CRUD completo: **POST → GET → PUT → DELETE**.

---

## Tecnologías

| Herramienta | Versión | Propósito |
|---|---|---|
| Java | 21 LTS | Lenguaje base |
| Gradle | 8.14 | Build y gestión de dependencias |
| Serenity BDD | 4.1.20 | Framework de reporting y orquestación |
| Serenity Screenplay REST | 4.1.20 | Patrón Screenplay para APIs REST |
| Serenity Cucumber | 4.1.20 | Integración Cucumber + Serenity |
| Cucumber | 7.17.0 | Motor BDD — escenarios en Gherkin |
| REST Assured | 5.4.0 | Cliente HTTP de pruebas |
| JUnit Platform Suite | 1.10.3 | Runner de tests con JUnit Platform |

---

## Estructura del proyecto

```
src/
└── test/
    ├── java/org/screenplay/
    │   ├── model/
    │   │   └── Pet.java                      ← POJO con fluent-builder (id, name, status)
    │   ├── actions/
    │   │   └── CallPetStoreApi.java           ← Ability: configura la URL base del Actor
    │   ├── tasks/
    │   │   ├── CreatePet.java                 ← Task: POST /pet
    │   │   ├── GetPet.java                    ← Task: GET  /pet/{id}
    │   │   ├── UpdatePet.java                 ← Task: PUT  /pet
    │   │   └── DeletePet.java                 ← Task: DELETE /pet/{id}
    │   ├── questions/
    │   │   └── TheResponse.java               ← Questions: statusCode, petName, petStatus
    │   ├── stepdefinitions/
    │   │   └── PetStoreStepDefinitions.java   ← Implementación de los pasos Gherkin
    │   └── runner/
    │       └── CucumberTestRunner.java        ← Punto de entrada (@Suite + Cucumber engine)
    └── resources/
        ├── features/petstore/
        │   ├── pet_crud.feature               ← Escenarios CRUD individuales (data-driven)
        │   └── pet_crud_flow.feature          ← Flujo E2E: POST → GET → PUT → DELETE
        ├── cucumber.properties                ← Configuración: glue, plugin, features
        └── serenity.conf                      ← Configuración de Serenity BDD
```

---

## Patrón Screenplay + Cucumber

```
pet_crud.feature          (CRUD individuales — data-driven)
pet_crud_flow.feature     (flujo E2E — POST → GET → PUT → DELETE)
        │
        ▼
PetStoreStepDefinitions  (@Given / @When / @Then)
        │
        ▼
Actor  (OnStage)
  │
  ├── whoCan(Ability)            → CallPetStoreApi  — URL base de la API
  │
  ├── attemptsTo(Task)           → CreatePet / GetPet / UpdatePet / DeletePet
  │        └── usa internamente  → Post.to() / Get.resource() / Put.to() / Delete.from()
  │
  └── should(seeThat(Question)) → TheResponse.statusCode() / petName() / petStatus()
```

| Componente | Archivo | Qué hace |
|---|---|---|
| **Feature** | `pet_crud.feature` / `pet_crud_flow.feature` | Define los escenarios en Gherkin |
| **Step Definitions** | `PetStoreStepDefinitions` | Conecta Gherkin con el código Java |
| **Actor** | gestionado por `OnStage` | Sujeto que ejecuta y verifica |
| **Ability** | `CallPetStoreApi` | Conoce la URL base de la API |
| **Task** | `tasks/` | Qué hace el actor (nivel negocio) |
| **Question** | `TheResponse` | Qué observa sobre la respuesta HTTP |

---

## Prerrequisitos

- **Java 21 LTS** instalado y `JAVA_HOME` apuntando a él
- **Gradle Wrapper** incluido — no es necesario instalar Gradle globalmente

Verificar Java:

```powershell
java --version
# java 21.0.x ...

echo $env:JAVA_HOME
# C:\Program Files\Java\jdk-21.x.x
```

---

## Ejecución de las pruebas

### Ejecutar todos los escenarios

```powershell
# Limpiar caché y ejecutar
.\gradlew.bat clean test

# Solo ejecutar (sin limpiar)
.\gradlew.bat test
```

### Ejecutar el flujo E2E completo

El flujo E2E cubre el ciclo de vida completo: **POST → GET → PUT → DELETE** en un solo escenario.  
Está definido en `pet_crud_flow.feature` con los tags `@e2e` y `@flujo-completo`:

```powershell
# Flujo completo E2E (por tag @e2e)
.\gradlew.bat test -Ptags="@e2e"

# Flujo completo E2E (por tag @flujo-completo)
.\gradlew.bat test -Ptags="@flujo-completo"
```

### Ejecutar operaciones CRUD por separado

Cada operación CRUD tiene su propio `Scenario Outline` data-driven en `pet_crud.feature`.  
Usa `-Ptags` para filtrar (funciona correctamente en PowerShell):

```powershell
# Solo CREAR  (POST /pet)
.\gradlew.bat test -Ptags="@crear"

# Solo CONSULTAR  (GET /pet/{id})
.\gradlew.bat test -Ptags="@consultar"

# Solo ACTUALIZAR  (PUT /pet)
.\gradlew.bat test -Ptags="@actualizar"

# Solo ELIMINAR  (DELETE /pet/{id})
.\gradlew.bat test -Ptags="@eliminar"

# Todos los CRUDs individuales
.\gradlew.bat test -Ptags="@crud"
```

### Ejecutar el runner directamente

```powershell
.\gradlew.bat test --tests "org.screenplay.runner.CucumberTestRunner"
```

### Limpiar resultados anteriores y volver a ejecutar

```powershell
.\gradlew.bat clean test
```

---

## Tags disponibles en los Features

| Tag | Feature | Escenario | Operación HTTP |
|---|---|---|---|
| `@crear` | `pet_crud.feature` | Crear una nueva mascota | `POST /pet` |
| `@consultar` | `pet_crud.feature` | Consultar una mascota existente | `GET /pet/{id}` |
| `@actualizar` | `pet_crud.feature` | Actualizar una mascota existente | `PUT /pet` |
| `@eliminar` | `pet_crud.feature` | Eliminar una mascota existente | `DELETE /pet/{id}` |
| `@crud` | `pet_crud.feature` | Todas las operaciones CRUD individuales | — |
| `@e2e` | `pet_crud_flow.feature` | Flujo completo E2E | POST → GET → PUT → DELETE |
| `@flujo-completo` | `pet_crud_flow.feature` | Flujo completo E2E (alias) | POST → GET → PUT → DELETE |

---

## Dónde cambiar los valores de las pruebas

### Nombre y estado de la mascota → `.feature`

Editar directamente [`src/test/resources/features/petstore/pet_crud.feature`](src/test/resources/features/petstore/pet_crud.feature) o [`src/test/resources/features/petstore/pet_crud_flow.feature`](src/test/resources/features/petstore/pet_crud_flow.feature):

```gherkin
When el actor crea una mascota con nombre "Toby Marin" y estado "available"
```

Cambia `"Toby Marin"` y `"available"` sin tocar código Java.

### ID de la mascota → `PetStoreStepDefinitions`

El ID se genera automáticamente para evitar colisiones en la API pública compartida.  
Está en [`src/test/java/org/screenplay/stepdefinitions/PetStoreStepDefinitions.java`](src/test/java/org/screenplay/stepdefinitions/PetStoreStepDefinitions.java):

```java
private final long petId = System.currentTimeMillis() % 9_000_000L + 1_000_000L;
```

Para usar un ID fijo (útil para buscarlo en Swagger UI tras el test):

```java
private final long petId = 99999L;
```

---

## Verificar resultados en Swagger UI

Después de ejecutar los tests puedes consultar el estado de la mascota directamente en la interfaz de Swagger:

1. Abre [`https://petstore.swagger.io/#/pet/getPetById`](https://petstore.swagger.io/#/pet/getPetById)
2. Haz clic en **GET /pet/{petId}** → **Try it out**
3. Ingresa el ID que aparece en la consola durante la ejecución (por ejemplo `3065072`)
4. Haz clic en **Execute**

> **Nota:** La PetStore demo es una API pública compartida. Los datos pueden ser sobreescritos por otros usuarios. Consulta los datos inmediatamente después de ejecutar el test, o usa un ID fijo como se describe arriba.

---

## Reporte de resultados

Tras ejecutar los tests se generan dos reportes:

| Reporte | Ruta | Descripción |
|---|---|---|
| JUnit HTML | `build/reports/tests/test/index.html` | Resumen estándar por escenario |
| Serenity JSON | `target/site/serenity/` | Reporte narrativo BDD con pasos detallados |

Abrir el reporte JUnit en el navegador:

```powershell
Start-Process "build\reports\tests\test\index.html"
```

---

## Escenarios de prueba

### `@crear` — POST /pet

```gherkin
Given el actor está configurado para llamar a la PetStore API
When  el actor crea una mascota con nombre "Toby Marin" y estado "available"
Then  el status code de la respuesta debe ser 200
```

### `@consultar` — GET /pet/{id}

```gherkin
Given el actor está configurado para llamar a la PetStore API
When  el actor crea una mascota con nombre "Toby Marin" y estado "available"
And   el actor consulta la mascota creada
Then  el status code de la respuesta debe ser 200
And   el nombre de la mascota debe ser "Toby Marin"
And   el estado de la mascota debe ser "available"
```

### `@actualizar` — PUT /pet

```gherkin
Given el actor está configurado para llamar a la PetStore API
When  el actor crea una mascota con nombre "Toby Marin" y estado "available"
And   el actor actualiza la mascota con nombre "Toby Marin Actualizado" y estado "sold"
Then  el status code de la respuesta debe ser 200
And   el nombre de la mascota debe ser "Toby Marin Actualizado"
And   el estado de la mascota debe ser "sold"
```

### `@eliminar` — DELETE /pet/{id}

```gherkin
Given el actor está configurado para llamar a la PetStore API
When  el actor crea una mascota con nombre "Toby Marin" y estado "available"
And   el actor elimina la mascota
Then  el status code de la respuesta debe ser 200
```

---

## Java 21 vs Java 24

### Recomendación: usar **Java 21 LTS**

| | Java 21 LTS | Java 24 (actual) |
|---|---|---|
| Tipo de release | **LTS** (Long Term Support) | No-LTS |
| Soporte hasta | Septiembre 2031 | ~6 meses |
| Compatibilidad con ByteBuddy | Nativa (sin workarounds) | Requiere `byte-buddy ≥ 1.15.11` y `-XX:+EnableDynamicAgentLoading` |
| Estabilidad en CI/CD | Alta — versión estándar de la industria | Media — APIs internas en transición |
| Frameworks (Serenity, Spring, etc.) | Totalmente soportado | Soporte parcial / workarounds necesarios |

### Cómo instalar Java 21 (Windows)

Descargar desde: https://adoptium.net/temurin/releases/?version=21

O con [Scoop](https://scoop.sh):

```powershell
scoop install temurin21-jdk
```

Verificar:

```powershell
java --version
# java 21.0.x ...
```


---

## Tecnologías

| Herramienta | Versión | Propósito |
|---|---|---|
| Java | 21 LTS *(recomendado)* / 24 | Lenguaje base |
| Gradle | 8.14 | Build y gestión de dependencias |
| Serenity BDD | 4.1.20 | Framework de reporting y orquestación |
| Serenity Screenplay REST | 4.1.20 | Patrón Screenplay para APIs REST |
| REST Assured | 5.4.0 | Cliente HTTP de pruebas |
| JUnit 5 | 5.x (transitivo) | Motor de ejecución de tests |

---

## Estructura del proyecto

```
src/
└── test/
    ├── java/org/screenplay/
    │   ├── model/
    │   │   └── Pet.java               ← POJO con fluent-builder (id, name, status)
    │   ├── actions/
    │   │   └── CallPetStoreApi.java   ← Habilidad: configura la URL base del Actor
    │   ├── tasks/
    │   │   ├── CreatePet.java         ← Task: POST /pet
    │   │   ├── GetPet.java            ← Task: GET  /pet/{id}
    │   │   ├── UpdatePet.java         ← Task: PUT  /pet
    │   │   └── DeletePet.java         ← Task: DELETE /pet/{id}
    │   ├── questions/
    │   │   └── TheResponse.java       ← Questions: statusCode, petName, petStatus
    │   └── tests/
    │       └── PetStoreCrudTest.java  ← Test E2E: flujo CRUD completo
    └── resources/
        └── serenity.conf              ← Configuración de Serenity BDD
```

---

## Patrón Screenplay

```
Actor (Tester)
  │
  ├── whoCan(Ability)         → CallPetStoreApi  — configurar la URL base
  │
  ├── attemptsTo(Task)        → CreatePet / GetPet / UpdatePet / DeletePet
  │       └── uses Action     → Post.to() / Get.resource() / Put.to() / Delete.from()
  │
  └── should(seeThat(Question)) → TheResponse.statusCode() / petName() / petStatus()
```

| Componente | Archivo | Qué hace |
|---|---|---|
| **Actor** | `PetStoreCrudTest` | Sujeto que ejecuta y verifica |
| **Ability** | `CallPetStoreApi` | Conoce la URL base de la API |
| **Task** | `tasks/` | Qué hace el actor (nivel negocio) |
| **Action** | Serenity REST internals | Cómo lo hace (HTTP request) |
| **Question** | `TheResponse` | Qué observa sobre la respuesta |

---

## Prerrequisitos

- **Java 21** (ver [recomendación](#java-21-vs-java-24))
- **Gradle Wrapper** incluido — no es necesario instalar Gradle

Verificar Java instalado:

```bash
java --version
```

---

## Ejecución de las pruebas

### Ejecutar todos los tests

```bash
# Linux / macOS
./gradlew test

# Windows (PowerShell)
.\gradlew.bat test
```

### Ejecutar un test específico

```bash
.\gradlew.bat test --tests "org.screenplay.tests.PetStoreCrudTest"
```

### Mostrar salida en consola durante la ejecución

El `build.gradle.kts` ya tiene `testLogging { showStandardStreams = true }` habilitado.  
Los pasos de Serenity se imprimen automáticamente en la salida estándar.

### Limpiar y volver a ejecutar

```bash
.\gradlew.bat clean test
```

---

## Reporte de resultados

Tras ejecutar los tests, Gradle genera dos tipos de reporte:

| Reporte | Ruta | Descripción |
|---|---|---|
| JUnit HTML | `build/reports/tests/test/index.html` | Resumen estándar de JUnit 5 |
| Serenity JSON | `target/site/serenity/` | Reporte narrativo de Serenity BDD |

Abrir el reporte JUnit en el navegador:

```powershell
Start-Process "build\reports\tests\test\index.html"
```

---

## Escenario de prueba

**`PetStoreCrudTest` — Flujo completo: crear → consultar → actualizar → eliminar**

```
GIVEN  el Actor crea una mascota "Firulais" vía POST /pet
 THEN  el Status Code es 200

 WHEN  el Actor consulta la mascota vía GET /pet/{id}
 THEN  el Status Code es 200
  AND  el nombre es "Firulais"
  AND  el estado es "available"

 WHEN  el Actor actualiza la mascota vía PUT /pet
 THEN  el Status Code es 200
  AND  el nombre es "Firulais Actualizado"
  AND  el estado es "sold"

 WHEN  el Actor elimina la mascota vía DELETE /pet/{id}
 THEN  el Status Code es 200
```

---

## Java 21 vs Java 24

### Recomendación: usar **Java 21 LTS**

| | Java 21 LTS | Java 24 (actual) |
|---|---|---|
| Tipo de release | **LTS** (Long Term Support) | No-LTS |
| Soporte hasta | Septiembre 2031 | ~6 meses |
| Compatibilidad con ByteBuddy | Nativa (sin workarounds) | Requiere `byte-buddy ≥ 1.15.11` y `-XX:+EnableDynamicAgentLoading` |
| Estabilidad en CI/CD | Alta — versión estándar de la industria | Media — APIs internas en transición |
| Frameworks de testing (Serenity, Spring, etc.) | Totalmente soportado | Soporte parcial / workarounds necesarios |

### ¿Por qué importa para este proyecto?

Serenity BDD usa **ByteBuddy** internamente para crear proxies dinámicos.  
ByteBuddy 1.14.x (el que Serenity 4.1.20 arrastra) **no soporta el formato de clases de Java 24**, lo que obliga a:

1. Forzar **ByteBuddy 1.15.11** en `build.gradle.kts`
2. Añadir el JVM arg `-XX:+EnableDynamicAgentLoading`

Con **Java 21** ninguno de estos dos workarounds es necesario — el proyecto funciona sin configuración adicional.

### Cómo instalar Java 21 (Windows)

Descargar desde: https://adoptium.net/temurin/releases/?version=21

O con [Scoop](https://scoop.sh):

```powershell
scoop install temurin21-jdk
```

Verificar:

```powershell
java --version
# java 21.0.x ...
```

> El proyecto funciona correctamente con **Java 24** gracias a las dependencias ya configuradas, pero Java 21 LTS es la elección más robusta para entornos de CI/CD y trabajo en equipo.
