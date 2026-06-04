package com.accountx.qa.stepdefinitions;

import com.accountx.qa.questions.ElIdCaso;
import com.accountx.qa.questions.ElResultadoPrescoring;
import com.accountx.qa.questions.ElSolicitudId;
import com.accountx.qa.tasks.CrearOportunidad;
import com.accountx.qa.tasks.EnviarSolicitudPrincipal;
import com.accountx.qa.tasks.ValidarPrescoring;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Entonces;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.rest.abilities.CallAnApi;
import org.hamcrest.Matchers;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;

/**
 * CAPA STEP DEFINITIONS - Flujo E2E (happy path).
 * Encadena T5 -> T3 -> T6 reutilizando las MISMAS tasks y questions.
 * Esto demuestra la reutilizacion: no se reescribe nada, se compone.
 */
public class FlujoCompletoSteps {

    private final String baseUrl = System.getProperty("restapi.baseUrl", "http://localhost:8080");
    private final Actor cliente = Actor.named("Cliente bancario");
    private String clienteId;

    @Dado("un cliente aprobado con ID {string}")
    public void unClienteAprobadoConId(String clienteId) {
        this.clienteId = clienteId;
        cliente.whoCan(CallAnApi.at(baseUrl));
    }

    @Cuando("completa el flujo de solicitud de la tarjeta {string} con ingreso {int}")
    public void completaElFlujoDeSolicitud(String tipoTarjeta, Integer ingreso) {
        // 1) Pre-scoring (T5)
        cliente.attemptsTo(ValidarPrescoring.paraCliente(clienteId));
        String resultado = ElResultadoPrescoring.delUltimoResponse().answeredBy(cliente);

        // 2) Grabar solicitud principal (T3)
        cliente.attemptsTo(EnviarSolicitudPrincipal.con(clienteId, tipoTarjeta, ingreso));
        String solicitudId = ElSolicitudId.delUltimoResponse().answeredBy(cliente);

        // 3) Crear oportunidad (T6), pasando el resultado del prescoring
        cliente.attemptsTo(CrearOportunidad.paraSolicitud(solicitudId, resultado));
    }

    @Entonces("se crea la oportunidad y el idCaso no esta vacio")
    public void seCreaLaOportunidadYElIdCasoNoEstaVacio() {
        cliente.should(seeThat("El idCaso",
                ElIdCaso.delUltimoResponse(), Matchers.not(Matchers.emptyOrNullString())));
    }
}
