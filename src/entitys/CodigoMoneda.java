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
public class CodigoMoneda {

    public static JSONObject getJsonCodigoMoneda(JSONObject jsonResumenFactura) {
        JSONObject json = null;
        try {
            json = jsonResumenFactura.getJSONObject("CodigoTipoMoneda");
        } catch (Exception e) {
            json = new JSONObject();
            json.put("CodigoMoneda", "ND");
            json.put("TipoCambio", 1);
        }

        return json;
    }

    String CodigoMoneda;
    double TipoCambio;

    public String getCodigoMoneda() {
        return CodigoMoneda;
    }

    public void setCodigoMoneda(String CodigoMoneda) {
        this.CodigoMoneda = CodigoMoneda;
    }

    public double getTipoCambio() {
        return TipoCambio;
    }

    public void setTipoCambio(double TipoCambio) {
        this.TipoCambio = TipoCambio;
    }

    static CodigoMoneda getCodigoMonedaFromJson(JSONObject jsonCodigoTipoMoneda) {
        CodigoMoneda cm = new CodigoMoneda();
        cm.setCodigoMoneda(jsonCodigoTipoMoneda.getString("CodigoMoneda"));
        cm.setTipoCambio(jsonCodigoTipoMoneda.getDouble("TipoCambio"));
        return cm;
    }

}
/*<CodigoTipoMoneda>
<CodigoMoneda>CRC</CodigoMoneda>
<TipoCambio>525.2700</TipoCambio>
</CodigoTipoMoneda>*/
