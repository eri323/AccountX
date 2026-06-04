package com.accountx.qa.questions;

import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;

/**
 * CAPA QUESTION (Screenplay).
 * Lee el campo "resultado" de la respuesta del pre-scoring (T5).
 */
public class ElResultadoPrescoring implements Question<String> {

    public static ElResultadoPrescoring delUltimoResponse() {
        return new ElResultadoPrescoring();
    }

    @Override
    public String answeredBy(Actor actor) {
        return SerenityRest.lastResponse().jsonPath().getString("resultado");
    }
}
