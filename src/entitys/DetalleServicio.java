/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entitys;

/**
 *
 * @author eobregon
 */
public class DetalleServicio {

    String Detalle, Codigo, CodigoComercial, Tipo, Cantidad, UnidadMedida;
    double PrecioUnitario, MontoTotal, SubTotal, MontoTotalLinea;
    int NumeroLinea;
    Impuesto impuesto;
    Descuento descuento;
    CodigoComercial codigoComercial;

    public Descuento getDescuento() {
        return descuento;
    }

    public void setDescuento(Descuento descuento) {
        this.descuento = descuento;
    }

    public String getCodigo() {
        return Codigo;
    }

    public void setCodigo(String Codigo) {
        this.Codigo = Codigo;
    }

    public CodigoComercial getcodigoComercial() {
        return codigoComercial;
    }

    public void setCodigoComercial(CodigoComercial codigoComercial) {
        this.codigoComercial = codigoComercial;
    }

    public String getCodigoComercial() {
        return CodigoComercial;
    }

    public void setCodigoComercial(String codigoComercial) {
        this.CodigoComercial = codigoComercial;
    }

    public String getTipo() {
        return Tipo;
    }

    public void setTipo(String Tipo) {
        this.Tipo = Tipo;
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

    public double getMontoTotalLinea() {
        return MontoTotalLinea;
    }

    public void setMontoTotalLinea(double MontoTotalLinea) {
        this.MontoTotalLinea = MontoTotalLinea;
    }

    public int getNumeroLinea() {
        return NumeroLinea;
    }

    public void setNumeroLinea(int NumeroLinea) {
        this.NumeroLinea = NumeroLinea;
    }

    public Impuesto getImpuesto() {
        return impuesto;
    }

    public void setImpuesto(Impuesto impuesto) {
        this.impuesto = impuesto;
    }

    public String getDetalle() {
        return Detalle;
    }

    public void setDetalle(String Detalle) {
        this.Detalle = Detalle;
    }

}
/*<DetalleServicio>
<LineaDetalle>
<NumeroLinea>1</NumeroLinea>
<Codigo>8422200000000</Codigo>
<CodigoComercial>
<Tipo>01</Tipo>
<Codigo>75299</Codigo>
</CodigoComercial>
<Cantidad>1.000</Cantidad>
<UnidadMedida>Os</UnidadMedida>
<Detalle>CR INTERNET BASICO PROMO 250MB C29995</Detalle>
<PrecioUnitario>26139.43000</PrecioUnitario>
<MontoTotal>26139.43000</MontoTotal>
<SubTotal>26139.43000</SubTotal>
<Impuesto>
<Codigo>01</Codigo>
<CodigoTarifa>08</CodigoTarifa>
<Tarifa>13.00</Tarifa>
<Monto>3398.13000</Monto>
</Impuesto>
<MontoTotalLinea>29537.56000</MontoTotalLinea>
</LineaDetalle>
</DetalleServicio>*/
