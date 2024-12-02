/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entitys;

import java.util.Date;

/**
 *
 * @author eobregon
 */
public class CompromisosProveedor {

    String ResponsableUltimaModificacion, cia_prov, nombreProveedor, responsable, observaciones, proveedor, moneda;
    double monto;
    int estado, periodo,id;
    java.util.Date FechaCreacion,UltimaModificacion ;

    public String getResponsableUltimaModificacion() {
        return ResponsableUltimaModificacion;
    }

    public void setResponsableUltimaModificacion(String ResponsableUltimaModificacion) {
        this.ResponsableUltimaModificacion = ResponsableUltimaModificacion;
    }

    public Date getUltimaModificacion() {
        return UltimaModificacion;
    }

    public void setUltimaModificacion(Date UltimaModificacion) {
        this.UltimaModificacion = UltimaModificacion;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public Date getFechaCreacion() {
        return FechaCreacion;
    }

    public void setFechaCreacion(Date FechaCreacion) {
        this.FechaCreacion = FechaCreacion;
    }

    public int getPeriodo() {
        return periodo;
    }

    public void setPeriodo(int periodo) {
        this.periodo = periodo;
    }

    public String getProveedor() {
        return proveedor;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }

    public String getCia_prov() {
        return cia_prov;
    }

    public void setCia_prov(String cia_prov) {
        this.cia_prov = cia_prov;
    }

    public String getNombreProveedor() {
        return nombreProveedor;
    }

    public void setNombreProveedor(String nombreProveedor) {
        this.nombreProveedor = nombreProveedor;
    }

    public String getResponsable() {
        return responsable;
    }

    public void setResponsable(String responsable) {
        this.responsable = responsable;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
