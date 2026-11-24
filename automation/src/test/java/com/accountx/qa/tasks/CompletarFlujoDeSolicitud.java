package com.accountx.qa.tasks;

import com.accountx.qa.questions.ElCodigoDeRespuesta;
import com.accountx.qa.questions.ElSolicitudId;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.Tasks.instrumented;
import static org.hamcrest.Matchers.equalTo;

/**
 * CAPA TASK (Screenplay) - compuesta.
 * El flujo E2E T5 -> T3 -> T6 como UNA accion de negocio, armada con las
 * mismas Tasks que usan los escenarios unitarios.
 *
 * Verifica cada paso intermedio: si T5 o T3 fallan, el escenario se cae ahi
 * con un mensaje que dice cual, en vez de llegar a T6 con un solicitudId nulo
 * y fallar con un error que no apunta a la causa.
 *
 * T6 se invoca siempre, aunque el pre-scoring no sea aprobado: precisamente
 * se quiere probar que el servidor la rechaza. El resultado lo valida el
 * escenario, no esta Task.
 */
public class CompletarFlujoDeSolicitud implements Task {

    public static final String SOLICITUD_ID = "solicitudId";

    private final String clienteId;
    private final String tipoTarjeta;
    private final Integer ingreso;

    public CompletarFlujoDeSolicitud(String clienteId, String tipoTarjeta, Integer ingreso) {
        this.clienteId = clienteId;
        this.tipoTarjeta = tipoTarjeta;
        this.ingreso = ingreso;
    }

    public static CompletarFlujoDeSolicitud de(String clienteId, String tipoTarjeta, Integer ingreso) {
        return instrumented(CompletarFlujoDeSolicitud.class, clienteId, tipoTarjeta, ingreso);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        // 1) Pre-scoring (T5)
        actor.attemptsTo(ValidarPrescoring.paraCliente(clienteId));
        actor.should(seeThat("El codigo de T5 (pre-scoring)",
                ElCodigoDeRespuesta.delUltimoResponse(), equalTo(200)));

        // 2) Solicitud principal (T3)
        actor.attemptsTo(EnviarSolicitudPrincipal.con(clienteId, tipoTarjeta, ingreso));
        actor.should(seeThat("El codigo de T3 (solicitud principal)",
                ElCodigoDeRespuesta.delUltimoResponse(), equalTo(201)));
        String solicitudId = ElSolicitudId.delUltimoResponse().answeredBy(actor);
        actor.remember(SOLICITUD_ID, solicitudId);

        // 3) Crear oportunidad (T6)
        actor.attemptsTo(CrearOportunidad.paraSolicitud(solicitudId));
    }
}
