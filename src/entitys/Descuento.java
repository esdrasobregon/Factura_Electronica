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
public class Descuento {

    double MontoDescuento;
    String NaturalezaDescuento;

    public double getMontoDescuento() {
        return MontoDescuento;
    }

    public void setMontoDescuento(double MontoDescuento) {
        this.MontoDescuento = MontoDescuento;
    }

    public String getNaturalezaDescuento() {
        return NaturalezaDescuento;
    }

    public void setNaturalezaDescuento(String NaturalezaDescuento) {
        this.NaturalezaDescuento = NaturalezaDescuento;
    }

    public static Descuento getDescuentoFromJson(JSONObject jsonLineaDescuento) {
        Descuento d = null;
        try {
            d = new Descuento();
            d.setMontoDescuento(jsonLineaDescuento.getDouble("MontoDescuento"));
            d.setNaturalezaDescuento(jsonLineaDescuento.getString("NaturalezaDescuento"));
        } catch (NullPointerException e) {
            System.out.println("entitys.Descuento.getDescuentoFromJson() error " + e.getMessage() + " the function returns a descuento object with empty values");
            d = new Descuento();
            d.setMontoDescuento(0);
            d.setNaturalezaDescuento("");
        }
        return d;
    }

    public void print() {
        System.out.println("entitys.Descuento.print() Monto descuento " + this.MontoDescuento + " naturaleza descuento " + this.NaturalezaDescuento);
    }

}
/*<Descuento>
    <MontoDescuento>17699.11504</MontoDescuento>
    <NaturalezaDescuento>Promocion de temporada</NaturalezaDescuento>
</Descuento>*/
