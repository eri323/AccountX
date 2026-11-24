package com.accountx.qa.tasks;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Post;

import java.util.HashMap;
import java.util.Map;

import static net.serenitybdd.screenplay.Tasks.instrumented;

/**
 * CAPA TASK (Screenplay).
 * Crea la oportunidad en el CRM (T6). El servidor decide si procede segun el
 * pre-scoring que el mismo registro en T5.
 *
 * declarandoResultado(...) existe solo para la prueba de seguridad: manda un
 * resultado falsificado en el body y verifica que el servidor lo ignore.
 */
public class CrearOportunidad implements Task {

    private final String solicitudId;
    private final String resultadoDeclarado;

    public CrearOportunidad(String solicitudId, String resultadoDeclarado) {
        this.solicitudId = solicitudId;
        this.resultadoDeclarado = resultadoDeclarado;
    }

    public static CrearOportunidad paraSolicitud(String solicitudId) {
        return instrumented(CrearOportunidad.class, solicitudId, null);
    }

    public CrearOportunidad declarandoResultado(String resultado) {
        return instrumented(CrearOportunidad.class, solicitudId, resultado);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        Map<String, Object> body = new HashMap<>();
        body.put("solicitudId", solicitudId);
        if (resultadoDeclarado != null) body.put("resultadoPrescoring", resultadoDeclarado);

        actor.attemptsTo(
                Post.to("/api/v1/crear-oportunidad")
                        .with(request -> request
                                .header("Content-Type", "application/json")
                                .body(body))
        );
    }
}
