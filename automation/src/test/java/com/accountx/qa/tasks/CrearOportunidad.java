package com.accountx.qa.tasks;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Post;

import java.util.HashMap;
import java.util.Map;

import static net.serenitybdd.screenplay.Tasks.instrumented;

/**
 * CAPA TASK (Screenplay).
 * Crea la oportunidad en el CRM (T6). Solo deberia ocurrir tras una aprobacion.
 */
public class CrearOportunidad implements Task {

    private final String solicitudId;
    private final String resultadoPrescoring;

    public CrearOportunidad(String solicitudId, String resultadoPrescoring) {
        this.solicitudId = solicitudId;
        this.resultadoPrescoring = resultadoPrescoring;
    }

    public static CrearOportunidad paraSolicitud(String solicitudId, String resultadoPrescoring) {
        return instrumented(CrearOportunidad.class, solicitudId, resultadoPrescoring);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        Map<String, Object> body = new HashMap<>();
        body.put("solicitudId", solicitudId);
        body.put("resultadoPrescoring", resultadoPrescoring);

        actor.attemptsTo(
                Post.to("/api/v1/crear-oportunidad")
                        .with(request -> request
                                .header("Content-Type", "application/json")
                                .body(body))
        );
    }
}
