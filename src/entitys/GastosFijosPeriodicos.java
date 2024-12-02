/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entitys;

import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author eobregon
 */
public class GastosFijosPeriodicos {

    String ProveedorActividad, Moneda, Fecuencia, Departamento, UsuarioCreador, UsuarioAutoriza, IdDepartamento, observaciones, ctaPresupuesto;
    int id, Estado;
    java.util.Date FechaSolicitud, FechaCreacion;
    double Monto;
    String montoF;
    boolean Realizado;

    public String getMontoF() {
        return montoF;
    }

    public void setMontoF(String montoF) {
        this.montoF = montoF;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getCtaPresupuesto() {
        return ctaPresupuesto;
    }

    public void setCtaPresupuesto(String ctaPresupuesto) {
        this.ctaPresupuesto = ctaPresupuesto;
    }

    public boolean isRealizado() {
        return Realizado;
    }

    public void setRealizado(boolean Realizado) {
        this.Realizado = Realizado;
    }

    public String getProveedorActividad() {
        return ProveedorActividad;
    }

    public void setProveedorActividad(String ProveedorActividad) {
        this.ProveedorActividad = ProveedorActividad;
    }

    public String getMoneda() {
        return Moneda;
    }

    public void setMoneda(String Moneda) {
        this.Moneda = Moneda;
    }

    public String getFecuencia() {
        return Fecuencia;
    }

    public void setFecuencia(String Fecuencia) {
        this.Fecuencia = Fecuencia;
    }

    public String getDepartamento() {
        return Departamento;
    }

    public void setDepartamento(String Departamento) {
        this.Departamento = Departamento;
    }

    public String getUsuarioCreador() {
        return UsuarioCreador;
    }

    public void setUsuarioCreador(String UsuarioCreador) {
        this.UsuarioCreador = UsuarioCreador;
    }

    public String getUsuarioAutoriza() {
        return UsuarioAutoriza;
    }

    public void setUsuarioAutoriza(String UsuarioAutoriza) {
        this.UsuarioAutoriza = UsuarioAutoriza;
    }

    public String getIdDepartamento() {
        return IdDepartamento;
    }

    public void setIdDepartamento(String IdDepartamento) {
        this.IdDepartamento = IdDepartamento;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEstado() {
        return Estado;
    }

    public void setEstado(int Estado) {
        this.Estado = Estado;
    }

    public Date getFechaSolicitud() {
        return FechaSolicitud;
    }

    public void setFechaSolicitud(Date FechaSolicitud) {
        this.FechaSolicitud = FechaSolicitud;
    }

    public Date getFechaCreacion() {
        return FechaCreacion;
    }

    public void setFechaCreacion(Date FechaCreacion) {
        this.FechaCreacion = FechaCreacion;
    }

    public double getMonto() {
        return Monto;
    }

    public void setMonto(double Monto) {
        this.Monto = Monto;
    }

    public static GastosFijosPeriodicos getGastoById(int id, ArrayList<GastosFijosPeriodicos> listaGastos) {
        int count = 0;
        boolean found = false;
        GastosFijosPeriodicos g = null;
        while (!found && count < listaGastos.size()) {
            GastosFijosPeriodicos ga = listaGastos.get(count);
            if (ga.getId() == id) {
                g = ga;
                found = true;
            }
            count++;
        }
        return g;
    }
}
