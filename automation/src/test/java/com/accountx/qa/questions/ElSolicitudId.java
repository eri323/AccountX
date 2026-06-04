package com.accountx.qa.questions;

import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;

/**
 * CAPA QUESTION (Screenplay).
 * Lee el campo "solicitudId" del cuerpo de la ultima respuesta (T3).
 */
public class ElSolicitudId implements Question<String> {

    public static ElSolicitudId delUltimoResponse() {
        return new ElSolicitudId();
    }

    @Override
    public String answeredBy(Actor actor) {
        return SerenityRest.lastResponse().jsonPath().getString("solicitudId");
    }
}
