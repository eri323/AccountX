package com.accountx.qa.stepdefinitions;

import com.accountx.qa.questions.ElResultadoPrescoring;
import com.accountx.qa.tasks.ValidarPrescoring;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Entonces;

import static com.accountx.qa.stepdefinitions.ComunSteps.CLIENTE_ID;
import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;
import static org.hamcrest.Matchers.equalTo;

/**
 * CAPA STEP DEFINITIONS - Pre-scoring (T5).
 * Cada cliente es una particion de equivalencia distinta:
 *   1001 -> APROBADO_PRELIMINAR
 *   1002 -> RECHAZADO
 *   1003 -> REQUIERE_ANALISIS_MANUAL
 *   otro -> RECHAZADO (valor por defecto seguro)
 */
public class PrescoringSteps {

    @Cuando("se ejecuta el prescoring")
    public void seEjecutaElPrescoring() {
        String clienteId = theActorInTheSpotlight().recall(CLIENTE_ID);
        theActorInTheSpotlight().attemptsTo(ValidarPrescoring.paraCliente(clienteId));
    }

    @Cuando("se ejecuta el prescoring sin clienteId")
    public void seEjecutaElPrescoringSinClienteId() {
        theActorInTheSpotlight().attemptsTo(ValidarPrescoring.paraCliente(null));
    }

    @Entonces("el resultado del prescoring es {string}")
    public void elResultadoEs(String resultadoEsperado) {
        theActorInTheSpotlight().should(seeThat("El resultado del prescoring",
                ElResultadoPrescoring.delUltimoResponse(), equalTo(resultadoEsperado)));
    }
}
