package org.screenplay.runner;

import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

/**
 * Punto de entrada para ejecutar los escenarios Cucumber con Serenity BDD.
 *
 * La configuración (glue, plugin, features) está en:
 *   src/test/resources/cucumber.properties
 *
 * Ejecución:
 *   .\gradlew.bat test
 *   .\gradlew.bat test --tests "org.screenplay.runner.CucumberTestRunner"
 */
@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
public class CucumberTestRunner {
}
