package com.korporativo.korporativo_backend.dto;

public class ViniloConfigDTO {

    private Long id;

    private Double anchoCm;
    private Double altoCm;

    private String tipoVinilo;
    private String tipoCorte;
    private String tipoAdhesivo;

    private String pais;
    private Boolean incluirIva;
    private Boolean incluirInstalacion;

    private Double precioBase;
    private Double precioFinal;

    private Long presupuestoId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getAnchoCm() {
        return anchoCm;
    }

    public void setAnchoCm(Double anchoCm) {
        this.anchoCm = anchoCm;
    }

    public Double getAltoCm() {
        return altoCm;
    }

    public void setAltoCm(Double altoCm) {
        this.altoCm = altoCm;
    }

    public String getTipoVinilo() {
        return tipoVinilo;
    }

    public void setTipoVinilo(String tipoVinilo) {
        this.tipoVinilo = tipoVinilo;
    }

    public String getTipoCorte() {
        return tipoCorte;
    }

    public void setTipoCorte(String tipoCorte) {
        this.tipoCorte = tipoCorte;
    }

    public String getTipoAdhesivo() {
        return tipoAdhesivo;
    }

    public void setTipoAdhesivo(String tipoAdhesivo) {
        this.tipoAdhesivo = tipoAdhesivo;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public Boolean getIncluirIva() {
        return incluirIva;
    }

    public void setIncluirIva(Boolean incluirIva) {
        this.incluirIva = incluirIva;
    }

    public Boolean getIncluirInstalacion() {
        return incluirInstalacion;
    }

    public void setIncluirInstalacion(Boolean incluirInstalacion) {
        this.incluirInstalacion = incluirInstalacion;
    }

    public Double getPrecioBase() {
        return precioBase;
    }

    public void setPrecioBase(Double precioBase) {
        this.precioBase = precioBase;
    }

    public Double getPrecioFinal() {
        return precioFinal;
    }

    public void setPrecioFinal(Double precioFinal) {
        this.precioFinal = precioFinal;
    }

    public Long getPresupuestoId() {
        return presupuestoId;
    }

    public void setPresupuestoId(Long presupuestoId) {
        this.presupuestoId = presupuestoId;
    }
}
