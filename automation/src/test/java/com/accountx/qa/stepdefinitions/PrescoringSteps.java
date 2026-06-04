package com.accountx.qa.stepdefinitions;

import com.accountx.qa.questions.ElResultadoPrescoring;
import com.accountx.qa.tasks.ValidarPrescoring;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Entonces;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.rest.abilities.CallAnApi;
import org.hamcrest.Matchers;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;

/**
 * CAPA STEP DEFINITIONS - Pre-scoring (T5).
 * Cada cliente representa una particion de equivalencia distinta:
 *   1001 -> APROBADO_PRELIMINAR
 *   1002 -> RECHAZADO
 *   1003 -> REQUIERE_ANALISIS_MANUAL
 */
public class PrescoringSteps {

    private final String baseUrl = System.getProperty("restapi.baseUrl", "http://localhost:8080");
    private final Actor cliente = Actor.named("Cliente bancario");
    private String clienteId;

    @Dado("el cliente {string} solicita la tarjeta {string}")
    public void elClienteSolicitaLaTarjeta(String clienteId, String tipoTarjeta) {
        this.clienteId = clienteId;
        cliente.whoCan(CallAnApi.at(baseUrl));
    }

    @Cuando("se ejecuta el prescoring")
    public void seEjecutaElPrescoring() {
        cliente.attemptsTo(ValidarPrescoring.paraCliente(clienteId));
    }

    @Entonces("el resultado es {string}")
    public void elResultadoEs(String resultadoEsperado) {
        cliente.should(seeThat("El resultado del prescoring",
                ElResultadoPrescoring.delUltimoResponse(), Matchers.equalTo(resultadoEsperado)));
    }
}
