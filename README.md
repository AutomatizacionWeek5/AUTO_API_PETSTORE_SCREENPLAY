# AUTO_API_PETSTORE_SCREENPLAY

Automatización de API REST utilizando el patrón **Screenplay** sobre la [PetStore Swagger API](https://petstore.swagger.io).  
Valida la integridad de los servicios REST ejecutando un flujo CRUD completo: **POST → GET → PUT → DELETE**.

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
