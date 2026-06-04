package com.accountx.qa.runners;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;

/**
 * RUNNER.
 * Arranca el motor de Cucumber sobre la plataforma de JUnit 5.
 *   - @SelectClasspathResource("features"): donde estan los .feature
 *   - GLUE_PROPERTY_NAME: el paquete donde viven los step definitions
 * Serenity engancha su reporte automaticamente.
 */
@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "com.accountx.qa.stepdefinitions")
public class TestRunner {
}
