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
public class LineaDetalle {

    int NumeroLinea;
    String Codigo, Cantidad, UnidadMedida, Detalle;
    double MontoTotalLinea, PrecioUnitario, MontoTotal, SubTotal;
    CodigoComercial codigoComercial;
    Descuento descuento;
    Impuesto impuesto;

    public static void print(LineaDetalle l) {
        System.out.println("printing linea detalle............................");
        System.out.println("Numero linea " + l.getNumeroLinea()
                + "\n, codigo " + l.getCodigo()
                + "\n, cantidad " + l.getCantidad()
                + "\n, " + l.getUnidadMedida()
                + "\n, detalle " + l.getDetalle()
                + "\n, monto total linea " + l.getMontoTotalLinea()
                + "\n, Precio unitario " + l.getPrecioUnitario()
                + "\n, monto total " + l.getMontoTotal()
                + "\n, subtotal " + l.getSubTotal()
        );
        Impuesto.print(l.getImpuesto());
    }

    public int getNumeroLinea() {
        return NumeroLinea;
    }

    public void setNumeroLinea(int NumeroLinea) {
        this.NumeroLinea = NumeroLinea;
    }

    public String getCodigo() {
        return Codigo;
    }

    public void setCodigo(String Codigo) {
        this.Codigo = Codigo;
    }

    public String getCantidad() {
        return Cantidad;
    }

    public void setCantidad(String Cantidad) {
        this.Cantidad = Cantidad;
    }

    public String getUnidadMedida() {
        return UnidadMedida;
    }

    public void setUnidadMedida(String UnidadMedida) {
        this.UnidadMedida = UnidadMedida;
    }

    public String getDetalle() {
        return Detalle;
    }

    public void setDetalle(String Detalle) {
        this.Detalle = Detalle;
    }

    public double getMontoTotalLinea() {
        return MontoTotalLinea;
    }

    public void setMontoTotalLinea(double MontoTotalLinea) {
        this.MontoTotalLinea = MontoTotalLinea;
    }

    public double getPrecioUnitario() {
        return PrecioUnitario;
    }

    public void setPrecioUnitario(double PrecioUnitario) {
        this.PrecioUnitario = PrecioUnitario;
    }

    public double getMontoTotal() {
        return MontoTotal;
    }

    public void setMontoTotal(double MontoTotal) {
        this.MontoTotal = MontoTotal;
    }

    public double getSubTotal() {
        return SubTotal;
    }

    public void setSubTotal(double SubTotal) {
        this.SubTotal = SubTotal;
    }

    public CodigoComercial getCodigoComercial() {
        return codigoComercial;
    }

    public void setCodigoComercial(CodigoComercial codigoComercial) {
        this.codigoComercial = codigoComercial;
    }

    public Descuento getDescuento() {
        return descuento;
    }

    public void setDescuento(Descuento descuento) {
        this.descuento = descuento;
    }

    public Impuesto getImpuesto() {
        return impuesto;
    }

    public void setImpuesto(Impuesto impuesto) {
        this.impuesto = impuesto;
    }

    public static LineaDetalle getLineaDetalleFromJson(JSONObject jsonLinea) {
        LineaDetalle li = new LineaDetalle();
        try {

            li.setCantidad(jsonLinea.get("Cantidad").toString());
            li.setNumeroLinea(jsonLinea.getInt("NumeroLinea"));
            li.setCodigo(jsonLinea.get("Codigo").toString());
            //li.setUnidadMedidaComercial(jsonLinea.getString("UnidadMedidaComercial"));
            li.setDetalle(jsonLinea.getString("Detalle"));
            li.setPrecioUnitario(jsonLinea.getDouble("PrecioUnitario"));
            li.setMontoTotal(jsonLinea.getDouble("MontoTotal"));
            li.setSubTotal(jsonLinea.getDouble("SubTotal"));
            li.setMontoTotalLinea(jsonLinea.getDouble("MontoTotalLinea"));
            li.setMontoTotalLinea(jsonLinea.getDouble("MontoTotalLinea"));
            JSONObject jsonLineaCodigoComercial = CodigoComercial.getJsonCodigoComercialFrom(jsonLinea);// jsonLinea.getJSONObject("CodigoComercial");
            JSONObject jsonLineaDescuento = JsonCommonFunctions.getJsonObject(jsonLinea, "Descuento");
            Descuento d = Descuento.getDescuentoFromJson(jsonLineaDescuento);
            li.setDescuento(d);
            d.print();
            JSONObject jsonLineaImpuesto = Impuesto.getJsonImpuesto(jsonLinea);// jsonLinea.getJSONObject("Impuesto");
            Impuesto imp = Impuesto.getImpuestoFromJson(jsonLineaImpuesto);
            li.setImpuesto(imp);
            imp.print();
            CodigoComercial c = CodigoComercial.getCodigoComercialFromJson(jsonLineaCodigoComercial);
            c.pint();
            li.setCodigoComercial(c);
            li.setUnidadMedida(JsonCommonFunctions.getJsonString(jsonLinea, "UnidadMedida"));
            

        } catch (Exception e) {
            System.out.println("entitys.LineaDetalle.getLineaDetalleFromJson() error " + e.getMessage());
        }

        return li;
    }

}
/*<LineaDetalle>
<NumeroLinea>1</NumeroLinea>
<Codigo>2714099009900</Codigo>
<CodigoComercial>
    <Tipo>04</Tipo>
    <Codigo>002115</Codigo>
</CodigoComercial>
<Cantidad>1.000</Cantidad>
<UnidadMedida>Unid</UnidadMedida>
<Detalle>PIANO DANCE</Detalle>
<PrecioUnitario>35398.23009</PrecioUnitario>
<MontoTotal>35398.23009</MontoTotal>
<Descuento>
    <MontoDescuento>17699.11504</MontoDescuento>
    <NaturalezaDescuento>Promocion de temporada</NaturalezaDescuento>
</Descuento>
<SubTotal>17699.11505</SubTotal>
<Impuesto>
    <Codigo>01</Codigo>
    <CodigoTarifa>08</CodigoTarifa>
    <Tarifa>13.00000</Tarifa>
    <Monto>2300.88496</Monto>
</Impuesto>
<MontoTotalLinea>20000.00001</MontoTotalLinea>
</LineaDetalle>*/
