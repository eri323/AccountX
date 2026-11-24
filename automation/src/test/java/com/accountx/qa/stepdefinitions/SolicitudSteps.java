package com.accountx.qa.stepdefinitions;

import com.accountx.qa.tasks.EnviarSolicitudPrincipal;
import io.cucumber.java.es.Cuando;

import static com.accountx.qa.stepdefinitions.ComunSteps.CLIENTE_ID;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;

/**
 * CAPA STEP DEFINITIONS - Solicitud principal (T3).
 * El clienteId sale del paso "Dado el cliente ..." (memoria del actor), no de
 * un valor fijo: lo que dice el Gherkin es lo que se envia.
 */
public class SolicitudSteps {

    @Cuando("envia una solicitud con tipoTarjeta {string} e ingreso {int}")
    public void enviaUnaSolicitud(String tipoTarjeta, Integer ingreso) {
        enviar(tipoTarjeta, ingreso);
    }

    @Cuando("envia una solicitud sin tipoTarjeta con ingreso {int}")
    public void enviaUnaSolicitudSinTipoTarjeta(Integer ingreso) {
        enviar(null, ingreso);
    }

    @Cuando("envia una solicitud con tipoTarjeta {string} sin ingreso")
    public void enviaUnaSolicitudSinIngreso(String tipoTarjeta) {
        enviar(tipoTarjeta, null);
    }

    @Cuando("envia una solicitud sin clienteId con tipoTarjeta {string} e ingreso {int}")
    public void enviaUnaSolicitudSinClienteId(String tipoTarjeta, Integer ingreso) {
        theActorInTheSpotlight().attemptsTo(EnviarSolicitudPrincipal.con(null, tipoTarjeta, ingreso));
    }

    private void enviar(String tipoTarjeta, Integer ingreso) {
        String clienteId = theActorInTheSpotlight().recall(CLIENTE_ID);
        theActorInTheSpotlight().attemptsTo(EnviarSolicitudPrincipal.con(clienteId, tipoTarjeta, ingreso));
    }
}
