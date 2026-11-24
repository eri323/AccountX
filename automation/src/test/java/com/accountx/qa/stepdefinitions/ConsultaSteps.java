package com.accountx.qa.stepdefinitions;

import com.accountx.qa.tasks.ConsultarServicio;
import com.accountx.qa.tasks.EnviarSolicitudAdicional;
import io.cucumber.java.es.Cuando;

import static com.accountx.qa.stepdefinitions.ComunSteps.CLIENTE_ID;
import static com.accountx.qa.tasks.CompletarFlujoDeSolicitud.SOLICITUD_ID;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;

/**
 * CAPA STEP DEFINITIONS - Servicios de consulta (T0, T1, T2) y
 * solicitud adicional (T4).
 */
public class ConsultaSteps {

    @Cuando("consulta sus productos disponibles")
    public void consultaSusProductos() {
        theActorInTheSpotlight().attemptsTo(
                ConsultarServicio.productosDelCliente(theActorInTheSpotlight().recall(CLIENTE_ID)));
    }

    @Cuando("consulta su informacion personal")
    public void consultaSuInformacion() {
        theActorInTheSpotlight().attemptsTo(
                ConsultarServicio.infoDelCliente(theActorInTheSpotlight().recall(CLIENTE_ID)));
    }

    @Cuando("consulta la informacion del adicional con cedula {string}")
    public void consultaElAdicional(String cedula) {
        theActorInTheSpotlight().attemptsTo(ConsultarServicio.infoDelAdicional(cedula));
    }

    @Cuando("registra un adicional con cedula {string}")
    public void registraUnAdicional(String cedula) {
        String solicitudId = theActorInTheSpotlight().recall(SOLICITUD_ID);
        theActorInTheSpotlight().attemptsTo(EnviarSolicitudAdicional.con(solicitudId, cedula));
    }

    @Cuando("registra un adicional sin cedula")
    public void registraUnAdicionalSinCedula() {
        String solicitudId = theActorInTheSpotlight().recall(SOLICITUD_ID);
        theActorInTheSpotlight().attemptsTo(EnviarSolicitudAdicional.con(solicitudId, null));
    }

    @Cuando("registra un adicional con cedula {string} sin solicitud principal")
    public void registraUnAdicionalSinSolicitud(String cedula) {
        theActorInTheSpotlight().attemptsTo(EnviarSolicitudAdicional.con(null, cedula));
    }

    @Cuando("registra un adicional con cedula {string} para la solicitud {string}")
    public void registraUnAdicionalPara(String cedula, String solicitudId) {
        theActorInTheSpotlight().attemptsTo(EnviarSolicitudAdicional.con(solicitudId, cedula));
    }
}
