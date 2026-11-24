package com.accountx.qa.stepdefinitions;

import com.accountx.qa.questions.ElCampoDeRespuesta;
import com.accountx.qa.questions.ElCodigoDeRespuesta;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Entonces;
import io.cucumber.java.es.Y;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.actors.OnStage.theActorCalled;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;

/**
 * CAPA STEP DEFINITIONS - pasos compartidos por todas las features.
 * Regla de oro: DELGADOS. Solo ponen al actor en escena, le piden Tasks y
 * le hacen Questions. El actor vive en OnStage (ver Hooks), asi que estos
 * pasos funcionan en cualquier feature sin duplicarse.
 */
public class ComunSteps {

    public static final String ACTOR = "Cliente bancario";
    public static final String CLIENTE_ID = "clienteId";

    @Dado("el cliente {string}")
    public void elCliente(String clienteId) {
        theActorCalled(ACTOR).remember(CLIENTE_ID, clienteId);
    }

    @Entonces("el servicio responde con codigo {int}")
    public void elServicioRespondeConCodigo(Integer codigoEsperado) {
        theActorInTheSpotlight().should(seeThat("El codigo de respuesta",
                ElCodigoDeRespuesta.delUltimoResponse(), equalTo(codigoEsperado)));
    }

    @Y("el campo {string} de la respuesta es {string}")
    public void elCampoDeLaRespuestaEs(String campo, String valorEsperado) {
        theActorInTheSpotlight().should(seeThat(
                ElCampoDeRespuesta.llamado(campo), equalTo(valorEsperado)));
    }

    @Y("el campo {string} de la respuesta no esta vacio")
    public void elCampoDeLaRespuestaNoEstaVacio(String campo) {
        theActorInTheSpotlight().should(seeThat(
                ElCampoDeRespuesta.llamado(campo), not(emptyOrNullString())));
    }

    @Y("la respuesta no trae el campo {string}")
    public void laRespuestaNoTraeElCampo(String campo) {
        theActorInTheSpotlight().should(seeThat(
                ElCampoDeRespuesta.llamado(campo), emptyOrNullString()));
    }
}
