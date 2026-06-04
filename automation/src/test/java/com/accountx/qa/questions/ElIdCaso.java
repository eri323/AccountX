package com.accountx.qa.questions;

import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;

/**
 * CAPA QUESTION (Screenplay).
 * Lee el campo "idCaso" de la respuesta de crear oportunidad (T6).
 */
public class ElIdCaso implements Question<String> {

    public static ElIdCaso delUltimoResponse() {
        return new ElIdCaso();
    }

    @Override
    public String answeredBy(Actor actor) {
        return SerenityRest.lastResponse().jsonPath().getString("idCaso");
    }
}
