package com.accountx.qa.questions;

import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;

/**
 * CAPA QUESTION (Screenplay).
 * Una Question LEE el estado del sistema para poder validarlo.
 * Aqui: el codigo de estado HTTP de la ultima respuesta.
 */
public class ElCodigoDeRespuesta implements Question<Integer> {

    public static ElCodigoDeRespuesta delUltimoResponse() {
        return new ElCodigoDeRespuesta();
    }

    @Override
    public Integer answeredBy(Actor actor) {
        return SerenityRest.lastResponse().statusCode();
    }
}
