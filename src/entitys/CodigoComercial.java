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
public class CodigoComercial {

    String codigo, Tipo;

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String CodigoComercial) {
        this.codigo = CodigoComercial;
    }

    public String getTipo() {
        return Tipo;
    }

    public void setTipo(String Tipo) {
        this.Tipo = Tipo;
    }

    public static CodigoComercial getCodigoComercialFromJson(JSONObject json) {
        CodigoComercial c = null;
        try {
            c = new CodigoComercial();
            c.setCodigo(JsonCommonFunctions.getJsonString(json, "Codigo"));
            c.setTipo(JsonCommonFunctions.getJsonString(json, "Tipo"));
        } catch (Exception e) {
            System.err.println("entitys.CodigoComercial.getCodigoComercialFromJson() error " + e.getMessage());
            c = null;
        }
        return c;
    }

    public static JSONObject getJsonCodigoComercialFrom(JSONObject json) {
        JSONObject c = new JSONObject();
        try {
            c = json.getJSONObject("CodigoComercial");
        } catch (Exception e) {
            System.out.println("entitys.CodigoComercial.getCodigoComercialFromJson() error " + e.getMessage());
            c.put("Codigo", "");
            c.put("Tipo", "");
        }
        return c;
    }

    public void pint() {
        System.out.println("entitys.CodigoComercial.pint() codigo " + this.codigo + " Tipo " + this.Tipo);
    }
}
/*<CodigoComercial>
    <Tipo>04</Tipo>
    <Codigo>002115</Codigo>
</CodigoComercial>*/
