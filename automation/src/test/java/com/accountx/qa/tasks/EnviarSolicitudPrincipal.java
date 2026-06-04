package com.accountx.qa.tasks;

import com.accountx.qa.model.SolicitudRequest;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Post;

import static net.serenitybdd.screenplay.Tasks.instrumented;

/**
 * CAPA TASK (Screenplay).
 * Una Task es una accion de NEGOCIO de alto nivel y reutilizable.
 * "El cliente envia la solicitud principal" -> POST a T3.
 *
 * El factory method con(...) se lee casi como lenguaje natural:
 *   EnviarSolicitudPrincipal.con("1001", "T001", 2500000)
 */
public class EnviarSolicitudPrincipal implements Task {

    private final SolicitudRequest solicitud;

    public EnviarSolicitudPrincipal(SolicitudRequest solicitud) {
        this.solicitud = solicitud;
    }

    public static EnviarSolicitudPrincipal con(String clienteId, String tipoTarjeta, Integer ingreso) {
        // instrumented() permite que Serenity reporte esta tarea en el informe.
        return instrumented(EnviarSolicitudPrincipal.class,
                new SolicitudRequest(clienteId, tipoTarjeta, ingreso));
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        // La Task se compone de una Interaction de bajo nivel (Post).
        actor.attemptsTo(
                Post.to("/api/v1/solicitud-principal")
                        .with(request -> request
                                .header("Content-Type", "application/json")
                                .body(solicitud))
        );
    }
}
