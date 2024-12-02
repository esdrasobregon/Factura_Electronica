/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entitys;

import java.util.Date;

/**
 *
 * @author eobregon
 */
public class DetallesHistoricoPagos {

    int id;
    String Observacion, CIA, NOMBRE_PROVEEDOR, NUM_PROVEEDOR, FACTURA, USUARIO;
    java.util.Date FECHA, ULTIMA_ACTUALIZACIO;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getObservacion() {
        return Observacion;
    }

    public void setObservacion(String Observacion) {
        this.Observacion = Observacion;
    }

    public String getCIA() {
        return CIA;
    }

    public void setCIA(String CIA) {
        this.CIA = CIA;
    }

    public String getNOMBRE_PROVEEDOR() {
        return NOMBRE_PROVEEDOR;
    }

    public void setNOMBRE_PROVEEDOR(String NOMBRE_PROVEEDOR) {
        this.NOMBRE_PROVEEDOR = NOMBRE_PROVEEDOR;
    }

    public String getNUM_PROVEEDOR() {
        return NUM_PROVEEDOR;
    }

    public void setNUM_PROVEEDOR(String NUM_PROVEEDOR) {
        this.NUM_PROVEEDOR = NUM_PROVEEDOR;
    }

    public String getFACTURA() {
        return FACTURA;
    }

    public void setFACTURA(String FACTURA) {
        this.FACTURA = FACTURA;
    }

    public String getUSUARIO() {
        return USUARIO;
    }

    public void setUSUARIO(String USUARIO) {
        this.USUARIO = USUARIO;
    }

    public Date getFECHA() {
        return FECHA;
    }

    public void setFECHA(Date FECHA) {
        this.FECHA = FECHA;
    }

    public Date getULTIMA_ACTUALIZACIO() {
        return ULTIMA_ACTUALIZACIO;
    }

    public void setULTIMA_ACTUALIZACIO(Date ULTIMA_ACTUALIZACIO) {
        this.ULTIMA_ACTUALIZACIO = ULTIMA_ACTUALIZACIO;
    }

}
