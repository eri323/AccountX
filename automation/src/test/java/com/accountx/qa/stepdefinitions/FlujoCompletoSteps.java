package com.accountx.qa.stepdefinitions;

import com.accountx.qa.questions.ElSolicitudId;
import com.accountx.qa.tasks.CompletarFlujoDeSolicitud;
import com.accountx.qa.tasks.CrearOportunidad;
import com.accountx.qa.tasks.EnviarSolicitudPrincipal;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Dado;

import static com.accountx.qa.stepdefinitions.ComunSteps.CLIENTE_ID;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;

/**
 * CAPA STEP DEFINITIONS - Flujo E2E y oportunidad (T6).
 * Delgados: el flujo vive en la Task CompletarFlujoDeSolicitud.
 */
public class FlujoCompletoSteps {

    @Cuando("completa el flujo de solicitud de la tarjeta {string} con ingreso {int}")
    public void completaElFlujoDeSolicitud(String tipoTarjeta, Integer ingreso) {
        String clienteId = theActorInTheSpotlight().recall(CLIENTE_ID);
        theActorInTheSpotlight().attemptsTo(CompletarFlujoDeSolicitud.de(clienteId, tipoTarjeta, ingreso));
    }

    @Dado("tiene una solicitud principal registrada sin pasar por el prescoring")
    public void tieneUnaSolicitudSinPrescoring() {
        registrarSolicitud();
    }

    @Dado("tiene una solicitud principal registrada")
    public void tieneUnaSolicitudRegistrada() {
        registrarSolicitud();
    }

    @Cuando("solicita crear la oportunidad")
    public void solicitaCrearLaOportunidad() {
        String solicitudId = theActorInTheSpotlight().recall(CompletarFlujoDeSolicitud.SOLICITUD_ID);
        theActorInTheSpotlight().attemptsTo(CrearOportunidad.paraSolicitud(solicitudId));
    }

    @Cuando("solicita crear la oportunidad para la solicitud {string}")
    public void solicitaCrearLaOportunidadPara(String solicitudId) {
        theActorInTheSpotlight().attemptsTo(CrearOportunidad.paraSolicitud(solicitudId));
    }

    @Cuando("solicita crear la oportunidad declarando el resultado {string}")
    public void solicitaCrearLaOportunidadDeclarando(String resultadoFalso) {
        String solicitudId = theActorInTheSpotlight().recall(CompletarFlujoDeSolicitud.SOLICITUD_ID);
        theActorInTheSpotlight().attemptsTo(
                CrearOportunidad.paraSolicitud(solicitudId).declarandoResultado(resultadoFalso));
    }

    private void registrarSolicitud() {
        String clienteId = theActorInTheSpotlight().recall(CLIENTE_ID);
        theActorInTheSpotlight().attemptsTo(EnviarSolicitudPrincipal.con(clienteId, "T001", 2500000));
        theActorInTheSpotlight().remember(CompletarFlujoDeSolicitud.SOLICITUD_ID,
                ElSolicitudId.delUltimoResponse().answeredBy(theActorInTheSpotlight()));
    }
}
