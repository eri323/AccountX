package com.accountx.qa.tasks;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Post;

import java.util.HashMap;
import java.util.Map;

import static net.serenitybdd.screenplay.Tasks.instrumented;

/**
 * CAPA TASK (Screenplay).
 * Graba la solicitud del tarjetahabiente adicional (T4), asociada a una
 * solicitud principal. Los null se omiten del body para poder probar
 * "campo ausente" sin armar JSON a mano.
 */
public class EnviarSolicitudAdicional implements Task {

    private final String solicitudId;
    private final String cedulaAdicional;

    public EnviarSolicitudAdicional(String solicitudId, String cedulaAdicional) {
        this.solicitudId = solicitudId;
        this.cedulaAdicional = cedulaAdicional;
    }

    public static EnviarSolicitudAdicional con(String solicitudId, String cedulaAdicional) {
        return instrumented(EnviarSolicitudAdicional.class, solicitudId, cedulaAdicional);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        Map<String, Object> body = new HashMap<>();
        if (solicitudId != null) body.put("solicitudId", solicitudId);
        if (cedulaAdicional != null) body.put("cedulaAdicional", cedulaAdicional);

        actor.attemptsTo(
                Post.to("/api/v1/solicitud-adicional")
                        .with(request -> request
                                .header("Content-Type", "application/json")
                                .body(body))
        );
    }
}
