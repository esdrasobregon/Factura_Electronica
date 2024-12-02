/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entitys;

import org.json.JSONObject;

/**
 *
 * @author eobregon
 */
public class Impuesto {

    String Codigo, CodigoTarifa;
    double Monto, Tarifa;

    public static void print(Impuesto i) {
        System.out.println("printing impuesto line..............................");
        System.out.println("codigo " + i.getCodigo()
                + ", codigo tarifa " + i.getCodigoTarifa()
                + ", monto " + i.getMonto()
                + ", tarifa " + i.getTarifa());
    }

    public double getTarifa() {
        return Tarifa;
    }

    public void setTarifa(double Tarifa) {
        this.Tarifa = Tarifa;
    }

    public String getCodigo() {
        return Codigo;
    }

    public void setCodigo(String Codigo) {
        this.Codigo = Codigo;
    }

    public String getCodigoTarifa() {
        return CodigoTarifa;
    }

    public void setCodigoTarifa(String CodigoTarifa) {
        this.CodigoTarifa = CodigoTarifa;
    }

    public double getMonto() {
        return Monto;
    }

    public void setMonto(double Monto) {
        this.Monto = Monto;
    }

    public static Impuesto getImpuestoFromJson(JSONObject json) {
        Impuesto imp = new Impuesto();
        try {
            
            imp.setMonto(json.getDouble("Monto"));
            imp.setCodigoTarifa(json.getString("CodigoTarifa"));
            imp.setCodigo(json.getString("Codigo"));
            imp.setTarifa(json.getDouble("Tarifa"));
        } catch (Exception e) {
            
            System.out.println("entitys.Impuesto.getImpuestoFromJson() error " + e.getMessage());
        }
        return imp;
    }

    public void print() {
        System.out.println("entitys.Impuesto.print() monto " + this.Monto + " codigo tarifa " + this.CodigoTarifa + " codigo " + this.Codigo + " tarifa " + this.Tarifa);
    }

    public static JSONObject getJsonImpuesto(JSONObject jsonResumenFactura) {
        JSONObject json = null;
        try {
            json = jsonResumenFactura.getJSONObject("Impuesto");
        } catch (Exception e) {
            json = new JSONObject();
            json.put("Codigo", "");
            json.put("Tarifa", 0);
            json.put("Monto", 0);
            json.put("CodigoTarifa", "");
            System.out.println("entitys.Impuesto.getJsonImpuesto() error "+e.getMessage());
        }

        return json;
    }
}
/*<Impuesto>
    <Codigo>01</Codigo>
    <CodigoTarifa>08</CodigoTarifa>
    <Tarifa>13.00000</Tarifa>
    <Monto>2300.88496</Monto>
</Impuesto>*/
