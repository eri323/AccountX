package com.accountx.qa.questions;

import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;

/**
 * CAPA QUESTION (Screenplay).
 * Lee cualquier campo de la ultima respuesta por su ruta JSON
 * ("nombre", "productos.size()", "error"...). Evita una clase por campo
 * cuando la verificacion es solo "este campo vale X".
 */
public class ElCampoDeRespuesta implements Question<String> {

    private final String ruta;

    private ElCampoDeRespuesta(String ruta) {
        this.ruta = ruta;
    }

    public static ElCampoDeRespuesta llamado(String ruta) {
        return new ElCampoDeRespuesta(ruta);
    }

    @Override
    public String answeredBy(Actor actor) {
        return SerenityRest.lastResponse().jsonPath().getString(ruta);
    }

    @Override
    public String toString() {
        return "el campo '" + ruta + "' de la respuesta";
    }
}
