package com.accountx.qa.stepdefinitions;

import com.accountx.qa.questions.ElCodigoDeRespuesta;
import com.accountx.qa.questions.ElSolicitudId;
import com.accountx.qa.tasks.EnviarSolicitudPrincipal;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Entonces;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.rest.abilities.CallAnApi;
import org.hamcrest.Matchers;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;

/**
 * CAPA STEP DEFINITIONS.
 * Es el PEGAMENTO entre el Gherkin y el codigo. Regla de oro: DELGADO.
 * No mete logica ni assertions complejas; solo orquesta al actor para que
 * ejecute Tasks y haga Questions. La logica vive en tasks/ y questions/.
 */
public class SolicitudSteps {

    private final String baseUrl = System.getProperty("restapi.baseUrl", "http://localhost:8080");
    private final Actor cliente = Actor.named("Cliente bancario");

    @Dado("un cliente con ID {string} autenticado")
    public void unClienteConIdAutenticado(String clienteId) {
        // Le damos al actor la HABILIDAD de llamar al API.
        cliente.whoCan(CallAnApi.at(baseUrl));
    }

    @Cuando("envia una solicitud con tipoTarjeta {string} e ingreso {int}")
    public void enviaUnaSolicitudConTipoTarjetaEIngreso(String tipoTarjeta, Integer ingreso) {
        cliente.attemptsTo(EnviarSolicitudPrincipal.con("1001", tipoTarjeta, ingreso));
    }

    @Cuando("envia una solicitud sin tipoTarjeta")
    public void enviaUnaSolicitudSinTipoTarjeta() {
        cliente.attemptsTo(EnviarSolicitudPrincipal.con("1001", null, 2500000));
    }

    @Cuando("envia una solicitud con ingreso {int}")
    public void enviaUnaSolicitudConIngreso(Integer ingreso) {
        cliente.attemptsTo(EnviarSolicitudPrincipal.con("1001", "T001", ingreso));
    }

    @Entonces("el servicio responde con codigo {int}")
    public void elServicioRespondeConCodigo(Integer codigoEsperado) {
        cliente.should(seeThat("El codigo de respuesta",
                ElCodigoDeRespuesta.delUltimoResponse(), Matchers.equalTo(codigoEsperado)));
    }

    @Entonces("la respuesta contiene un solicitudId no vacio")
    public void laRespuestaContieneUnSolicitudIdNoVacio() {
        cliente.should(seeThat("El solicitudId",
                ElSolicitudId.delUltimoResponse(), Matchers.not(Matchers.emptyOrNullString())));
    }
}
