package com.accountx.qa.stepdefinitions;

import io.cucumber.java.Before;
import net.serenitybdd.model.environment.EnvironmentSpecificConfiguration;
import net.serenitybdd.screenplay.actors.Cast;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.rest.abilities.CallAnApi;
import net.thucydides.model.util.EnvironmentVariables;
import net.thucydides.model.environment.SystemEnvironmentVariables;

/**
 * HOOKS.
 * Antes de cada escenario se monta el "escenario" de Screenplay: un Cast donde
 * todo actor puede llamar al API. Asi los actores viven en OnStage y no en cada
 * clase de steps: un escenario puede mezclar pasos de varias clases y todas
 * hablan con el mismo actor, que ya tiene la habilidad CallAnApi.
 *
 * La URL sale de serenity.conf segun el ambiente (-Denvironment=qa) y se puede
 * forzar con -Drestapi.baseUrl=...
 */
public class Hooks {

    @Before(order = 0)
    public void montarEscenario() {
        EnvironmentVariables env = SystemEnvironmentVariables.currentEnvironmentVariables();
        String baseUrl = EnvironmentSpecificConfiguration.from(env)
                .getOptionalProperty("restapi.baseUrl")
                .orElse("http://localhost:8080");

        OnStage.setTheStage(Cast.whereEveryoneCan(CallAnApi.at(baseUrl)));
    }
}
