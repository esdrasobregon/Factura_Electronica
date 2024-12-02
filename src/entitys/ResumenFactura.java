/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entitys;

import logic.JsonCommonFunctions;
import org.json.JSONObject;

/**
 *
 * @author eobregon
 */
public class ResumenFactura {

    double TotalIVADevuelto, TotalExonerado, TotalMercExonerada, TotalServExonerado, TotalServGravados, TotalServExentos, TotalMercanciasGravadas, TotalMercanciasExentas, TotalGravado, TotalExento, TotalVenta, TotalDescuentos, TotalVentaNeta, TotalImpuesto, TotalOtrosCargos, TotalComprobante;

    public double getTotalIVADevuelto() {
        return TotalIVADevuelto;
    }

    public void setTotalIVADevuelto(double TotalIVADevuelto) {
        this.TotalIVADevuelto = TotalIVADevuelto;
    }
    CodigoMoneda codigoMoneda;

    public CodigoMoneda getCodigoMoneda() {
        return codigoMoneda;
    }

    public void setCodigoMoneda(CodigoMoneda codigoMoneda) {
        this.codigoMoneda = codigoMoneda;
    }

    public double getTotalExonerado() {
        return TotalExonerado;
    }

    public void setTotalExonerado(double TotalExonerado) {
        this.TotalExonerado = TotalExonerado;
    }

    public double getTotalMercExonerada() {
        return TotalMercExonerada;
    }

    public void setTotalMercExonerada(double TotalMercExonerada) {
        this.TotalMercExonerada = TotalMercExonerada;
    }

    public double getTotalServExonerado() {
        return TotalServExonerado;
    }

    public void setTotalServExonerado(double TotalServExonerado) {
        this.TotalServExonerado = TotalServExonerado;
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

    public double getTotalGravado() {
        return TotalGravado;
    }

    public void setTotalGravado(double TotalGravado) {
        this.TotalGravado = TotalGravado;
    }

    public double getTotalExento() {
        return TotalExento;
    }

    public void setTotalExento(double TotalExento) {
        this.TotalExento = TotalExento;
    }

    public double getTotalVenta() {
        return TotalVenta;
    }

    public void setTotalVenta(double TotalVenta) {
        this.TotalVenta = TotalVenta;
    }

    public double getTotalDescuentos() {
        return TotalDescuentos;
    }

    public void setTotalDescuentos(double TotalDescuentos) {
        this.TotalDescuentos = TotalDescuentos;
    }

    public double getTotalVentaNeta() {
        return TotalVentaNeta;
    }

    public void setTotalVentaNeta(double TotalVentaNeta) {
        this.TotalVentaNeta = TotalVentaNeta;
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

    public double getTotalComprobante() {
        return TotalComprobante;
    }

    public void setTotalComprobante(double TotalComprobante) {
        this.TotalComprobante = TotalComprobante;
    }

    public void print() {

        System.out.println("entitys.ResumenFactura.print() TotalExonerado " + TotalExonerado
                + "\n TotalServExonerado " + TotalServExonerado
                + "\n TotalServGravados " + TotalServGravados
                + "\n TotalServExentos " + TotalServExentos
                + "\n TotalMercExonerada " + TotalMercExonerada
                + "\n TotalMercanciasGravadas " + TotalMercanciasGravadas
                + "\n TotalMercanciasExentas " + TotalMercanciasExentas
                + "\n TotalGravado " + TotalGravado
                + "\n TotalExento " + TotalExento
                + "\n TotalVenta " + TotalVenta
                + "\n TotalDescuentos " + TotalDescuentos
                + "\n TotalVentaNeta " + TotalVentaNeta
                + "\n TotalImpuesto " + TotalImpuesto
                + "\n TotalOtrosCargos " + TotalOtrosCargos
                + "\n TotalComprobante " + TotalComprobante
                + "\n CodigoMoneda " + codigoMoneda.getCodigoMoneda()
                + "\n TipoCambio " + codigoMoneda.getTipoCambio());
    }

    public static ResumenFactura getResumenFacturaFromJson(JSONObject jsonResumenFactura) {
        ResumenFactura rf = null;
        try {
            rf = new ResumenFactura();
            rf.setTotalServGravados(JsonCommonFunctions.getJsonDouble(jsonResumenFactura, "TotalServGravados"));
            rf.setTotalServExentos(JsonCommonFunctions.getJsonDouble(jsonResumenFactura, "TotalServExentos"));
            rf.setTotalServExonerado(JsonCommonFunctions.getJsonDouble(jsonResumenFactura, "TotalServExonerado"));//jsonResumenFactura.getDouble("TotalServExonerado"));
            rf.setTotalMercanciasGravadas(JsonCommonFunctions.getJsonDouble(jsonResumenFactura, "TotalMercanciasGravadas"));
            rf.setTotalMercanciasExentas(JsonCommonFunctions.getJsonDouble(jsonResumenFactura, "TotalMercanciasExentas"));
            rf.setTotalMercExonerada(JsonCommonFunctions.getJsonDouble(jsonResumenFactura, "TotalMercExonerada"));
            rf.setTotalGravado(JsonCommonFunctions.getJsonDouble(jsonResumenFactura, "TotalGravado"));
            rf.setTotalExento(JsonCommonFunctions.getJsonDouble(jsonResumenFactura, "TotalExento"));
            rf.setTotalExonerado(JsonCommonFunctions.getJsonDouble(jsonResumenFactura, "TotalExonerado"));// jsonResumenFactura.getDouble("TotalExonerado"));
            rf.setTotalVenta(JsonCommonFunctions.getJsonDouble(jsonResumenFactura, "TotalVenta"));
            rf.setTotalVentaNeta(JsonCommonFunctions.getJsonDouble(jsonResumenFactura, "TotalVentaNeta"));
            rf.setTotalImpuesto(JsonCommonFunctions.getJsonDouble(jsonResumenFactura, "TotalImpuesto"));
            rf.setTotalComprobante(JsonCommonFunctions.getJsonDouble(jsonResumenFactura, "TotalComprobante"));
            JSONObject jsonCodigoTipoMoneda = CodigoMoneda.getJsonCodigoMoneda(jsonResumenFactura);// jsonResumenFactura.getJSONObject("CodigoTipoMoneda");
            rf.setCodigoMoneda(CodigoMoneda.getCodigoMonedaFromJson(jsonCodigoTipoMoneda));
            rf.setTotalDescuentos(JsonCommonFunctions.getJsonDouble(jsonResumenFactura, "TotalDescuentos"));
            rf.setTotalOtrosCargos(JsonCommonFunctions.getJsonDouble(jsonResumenFactura, "TotalOtrosCargos"));
        } catch (Exception e) {
            System.out.println("entitys.ResumenFactura.getResumenFacturaFromJson() error " + e.getMessage());
        }
        return rf;
    }

}
/*<ResumenFactura>
<CodigoTipoMoneda>
<CodigoMoneda>CRC</CodigoMoneda>
<TipoCambio>1</TipoCambio>
</CodigoTipoMoneda>
<TotalServGravados>26139.43000</TotalServGravados>
<TotalServExentos>0.00000</TotalServExentos>
<TotalMercanciasGravadas>0.00000</TotalMercanciasGravadas>
<TotalMercanciasExentas>0.00000</TotalMercanciasExentas>
<TotalGravado>26139.43000</TotalGravado>
<TotalExento>0.00000</TotalExento>
<TotalVenta>26139.43000</TotalVenta>
<TotalDescuentos>0.00000</TotalDescuentos>
<TotalVentaNeta>26139.43000</TotalVentaNeta>
<TotalImpuesto>3398.13000</TotalImpuesto>
<TotalOtrosCargos>457.44000</TotalOtrosCargos>
<TotalComprobante>29995.00000</TotalComprobante>
</ResumenFactura>*/
