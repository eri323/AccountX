package com.accountx.qa.tasks;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Get;

import static net.serenitybdd.screenplay.Tasks.instrumented;

/**
 * CAPA TASK (Screenplay).
 * Consultas GET de los servicios de lectura: T0 (productos), T1 (cliente)
 * y T2 (adicional). Los tres son "trae el recurso X por su ID", asi que una
 * sola Task con factories con nombre de negocio evita tres clases identicas.
 */
public class ConsultarServicio implements Task {

    private final String recurso;
    private final String id;

    public ConsultarServicio(String recurso, String id) {
        this.recurso = recurso;
        this.id = id;
    }

    /** T0 - productos que el cliente puede solicitar. */
    public static ConsultarServicio productosDelCliente(String clienteId) {
        return instrumented(ConsultarServicio.class, "/api/v1/productos-no-disponibles/{id}", clienteId);
    }

    /** T1 - datos del cliente principal. */
    public static ConsultarServicio infoDelCliente(String clienteId) {
        return instrumented(ConsultarServicio.class, "/api/v1/info-cliente/{id}", clienteId);
    }

    /** T2 - datos del tarjetahabiente adicional. */
    public static ConsultarServicio infoDelAdicional(String cedula) {
        return instrumented(ConsultarServicio.class, "/api/v1/info-adicional/{id}", cedula);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Get.resource(recurso).with(request -> request.pathParam("id", id))
        );
    }
}
