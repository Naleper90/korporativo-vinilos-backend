package com.korporativo.korporativo_backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "vinilo_configs")
public class ViniloConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Medidas
    private Double anchoCm;
    private Double altoCm;

    // Configuración
    private String tipoVinilo;     // monomerico, polimero, transparente, microperforado
    private String tipoCorte;      // recto, contorno
    private String tipoAdhesivo;   // normal, extra

    // IVA e instalación
    private String pais;           // ES, PT, CANARIAS
    private Boolean incluirIva;
    private Boolean incluirInstalacion;

    // Precios
    private Double precioBase;
    private Double precioFinal;

    // Relación con Presupuesto
    @ManyToOne
    @JoinColumn(name = "presupuesto_id")
    private Presupuesto presupuesto;

    public ViniloConfig() {
    }

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Double getAnchoCm() { return anchoCm; }
    public void setAnchoCm(Double anchoCm) { this.anchoCm = anchoCm; }

    public Double getAltoCm() { return altoCm; }
    public void setAltoCm(Double altoCm) { this.altoCm = altoCm; }

    public String getTipoVinilo() { return tipoVinilo; }
    public void setTipoVinilo(String tipoVinilo) { this.tipoVinilo = tipoVinilo; }

    public String getTipoCorte() { return tipoCorte; }
    public void setTipoCorte(String tipoCorte) { this.tipoCorte = tipoCorte; }

    public String getTipoAdhesivo() { return tipoAdhesivo; }
    public void setTipoAdhesivo(String tipoAdhesivo) { this.tipoAdhesivo = tipoAdhesivo; }

    public String getPais() { return pais; }
    public void setPais(String pais) { this.pais = pais; }

    public Boolean getIncluirIva() { return incluirIva; }
    public void setIncluirIva(Boolean incluirIva) { this.incluirIva = incluirIva; }

    public Boolean getIncluirInstalacion() { return incluirInstalacion; }
    public void setIncluirInstalacion(Boolean incluirInstalacion) { this.incluirInstalacion = incluirInstalacion; }

    public Double getPrecioBase() { return precioBase; }
    public void setPrecioBase(Double precioBase) { this.precioBase = precioBase; }

    public Double getPrecioFinal() { return precioFinal; }
    public void setPrecioFinal(Double precioFinal) { this.precioFinal = precioFinal; }

    public Presupuesto getPresupuesto() { return presupuesto; }
    public void setPresupuesto(Presupuesto presupuesto) { this.presupuesto = presupuesto; }
}
