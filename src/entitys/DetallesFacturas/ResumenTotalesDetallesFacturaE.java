/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entitys.DetallesFacturas;

import java.util.Date;

/**
 *
 * @author eobregon
 */
public class ResumenTotalesDetallesFacturaE {

    double TotalComprobante, TotalDescuentos, TotalServGravados, TotalServExentos, TotalServExonerado, TotalMercanciasGravadas, TotalMercanciasExentas, TotalExento, TotalImpuesto, TotalOtrosCargos;
    String CodigoMoneda;
    java.util.Date inicio, fin;

    public Date getInicio() {
        return inicio;
    }

    public void setInicio(Date inicio) {
        this.inicio = inicio;
    }

    public Date getFin() {
        return fin;
    }

    public void setFin(Date fin) {
        this.fin = fin;
    }

    public double getTotalComprobante() {
        return TotalComprobante;
    }

    public void setTotalComprobante(double TotalComprobante) {
        this.TotalComprobante = TotalComprobante;
    }

    public double getTotalDescuentos() {
        return TotalDescuentos;
    }

    public void setTotalDescuentos(double TotalDescuentos) {
        this.TotalDescuentos = TotalDescuentos;
    }

    public double getTotalServGravados() {
        return TotalServGravados;
    }

    public void setTotalServGravados(double TotalServGravados) {
        this.TotalServGravados = TotalServGravados;
    }

    public double getTotalServExentos() {
        return TotalServExentos;
    }

    public void setTotalServExentos(double TotalServExentos) {
        this.TotalServExentos = TotalServExentos;
    }

    public double getTotalServExonerado() {
        return TotalServExonerado;
    }

    public void setTotalServExonerado(double TotalServExonerado) {
        this.TotalServExonerado = TotalServExonerado;
    }

    public double getTotalMercanciasGravadas() {
        return TotalMercanciasGravadas;
    }

    public void setTotalMercanciasGravadas(double TotalMercanciasGravadas) {
        this.TotalMercanciasGravadas = TotalMercanciasGravadas;
    }

    public double getTotalMercanciasExentas() {
        return TotalMercanciasExentas;
    }

    public void setTotalMercanciasExentas(double TotalMercanciasExentas) {
        this.TotalMercanciasExentas = TotalMercanciasExentas;
    }

    public double getTotalExento() {
        return TotalExento;
    }

    public void setTotalExento(double TotalExento) {
        this.TotalExento = TotalExento;
    }

    public double getTotalImpuesto() {
        return TotalImpuesto;
    }

    public void setTotalImpuesto(double TotalImpuesto) {
        this.TotalImpuesto = TotalImpuesto;
    }

    public double getTotalOtrosCargos() {
        return TotalOtrosCargos;
    }

    public void setTotalOtrosCargos(double TotalOtrosCargos) {
        this.TotalOtrosCargos = TotalOtrosCargos;
    }

    public String getCodigoMoneda() {
        return CodigoMoneda;
    }

    public void setCodigoMoneda(String CodigoMoneda) {
        this.CodigoMoneda = CodigoMoneda;
    }
    
}
