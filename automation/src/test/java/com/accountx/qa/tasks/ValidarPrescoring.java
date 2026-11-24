package com.accountx.qa.tasks;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Post;

import java.util.HashMap;
import java.util.Map;

import static net.serenitybdd.screenplay.Tasks.instrumented;

/**
 * CAPA TASK (Screenplay).
 * El cliente ejecuta el pre-scoring (T5), el servicio de mayor riesgo.
 */
public class ValidarPrescoring implements Task {

    private final String clienteId;

    public ValidarPrescoring(String clienteId) {
        this.clienteId = clienteId;
    }

    public static ValidarPrescoring paraCliente(String clienteId) {
        return instrumented(ValidarPrescoring.class, clienteId);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        Map<String, Object> body = new HashMap<>();
        // null se omite: permite probar el caso "sin clienteId".
        if (clienteId != null) body.put("clienteId", clienteId);

        actor.attemptsTo(
                Post.to("/api/v1/prescoring")
                        .with(request -> request
                                .header("Content-Type", "application/json")
                                .body(body))
        );
    }
}
