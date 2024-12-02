/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entitys.DetallesFacturas;

/**
 *
 * @author eobregon
 */
public class DetallesLineasFactura {

    int totalLineas;
    double sumaMontoTotalLineas, sumaMontoTotalDesc, sumaSubTotal, sumaMontoImpuestoNeto,tarifaimp,  calcImpuestoNeto;
    String codigomoneda;

    public int getTotalLineas() {
        return totalLineas;
    }

    public void setTotalLineas(int totalLineas) {
        this.totalLineas = totalLineas;
    }

    
    public double getTarifaimp() {
        return tarifaimp;
    }

    public void setTarifaimp(double tarifaimp) {
        this.tarifaimp = tarifaimp;
    }

    public double getSumaMontoTotalLineas() {
        return sumaMontoTotalLineas;
    }

    public void setSumaMontoTotalLineas(double sumaMontoTotalLineas) {
        this.sumaMontoTotalLineas = sumaMontoTotalLineas;
    }

    public double getSumaMontoTotalDesc() {
        return sumaMontoTotalDesc;
    }

    public void setSumaMontoTotalDesc(double sumaMontoTotalDesc) {
        this.sumaMontoTotalDesc = sumaMontoTotalDesc;
    }

    public double getSumaSubTotal() {
        return sumaSubTotal;
    }

    public void setSumaSubTotal(double sumaSubTotal) {
        this.sumaSubTotal = sumaSubTotal;
    }

    public double getSumaMontoImpuestoNeto() {
        return sumaMontoImpuestoNeto;
    }

    public void setSumaMontoImpuestoNeto(double sumaMontoImpuestoNeto) {
        this.sumaMontoImpuestoNeto = sumaMontoImpuestoNeto;
    }

    public double getCalcImpuestoNeto() {
        return calcImpuestoNeto;
    }

    public void setCalcImpuestoNeto(double calcImpuestoNeto) {
        this.calcImpuestoNeto = calcImpuestoNeto;
    }

    
    public String getCodigomoneda() {
        return codigomoneda;
    }

    public void setCodigomoneda(String codigomoneda) {
        this.codigomoneda = codigomoneda;
    }

}
