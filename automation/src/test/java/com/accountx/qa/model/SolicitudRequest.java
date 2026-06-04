package com.accountx.qa.model;

/**
 * CAPA MODEL.
 * POJO que representa el cuerpo (body) de la solicitud principal (T3).
 *
 * Por que existe esta capa: tener un contrato tipado del request en vez de
 * andar armando JSON crudo regado por las pruebas. Si el contrato del API
 * cambia, se ajusta aqui en un solo lugar.
 */
public class SolicitudRequest {

    private final String clienteId;
    private final String tipoTarjeta;
    private final Integer ingreso;

    public SolicitudRequest(String clienteId, String tipoTarjeta, Integer ingreso) {
        this.clienteId = clienteId;
        this.tipoTarjeta = tipoTarjeta;
        this.ingreso = ingreso;
    }

    // Getters: los necesita Jackson para serializar el objeto a JSON.
    public String getClienteId() {
        return clienteId;
    }

    public String getTipoTarjeta() {
        return tipoTarjeta;
    }

    public Integer getIngreso() {
        return ingreso;
    }
}
