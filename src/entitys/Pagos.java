/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entitys;

import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author eobregon
 */
public class Pagos {

    int sociedad;
    String nombreProveedor, cia, proveedor, tipo_debito, debito, tipo_credito, credito, moneda_debito, createdby, updatedby, rowpointer;
    double monto_credito, monto_debito, monto_prov, monto_local, monto_dolar;
    java.util.Date fecha, recordDate, createDate;

    public Date getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(Date recordDate) {
        this.recordDate = recordDate;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    
    public int getSociedad() {
        return sociedad;
    }

    public String getNombreProveedor() {
        return nombreProveedor;
    }

    public void setNombreProveedor(String nombreProveedor) {
        this.nombreProveedor = nombreProveedor;
    }

    public void setSociedad(int sociedad) {
        this.sociedad = sociedad;
    }

    public String getCia() {
        return cia;
    }

    public void setCia(String cia) {
        this.cia = cia;
    }

    public String getProveedor() {
        return proveedor;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }

    public String getTipo_debito() {
        return tipo_debito;
    }

    public void setTipo_debito(String tipo_debito) {
        this.tipo_debito = tipo_debito;
    }

    public String getDebito() {
        return debito;
    }

    public void setDebito(String debito) {
        this.debito = debito;
    }

    public String getTipo_credito() {
        return tipo_credito;
    }

    public void setTipo_credito(String tipo_credito) {
        this.tipo_credito = tipo_credito;
    }

    public String getCredito() {
        return credito;
    }

    public void setCredito(String credito) {
        this.credito = credito;
    }

    public String getMoneda_debito() {
        return moneda_debito;
    }

    public void setMoneda_debito(String moneda_debito) {
        this.moneda_debito = moneda_debito;
    }

    public String getCreatedby() {
        return createdby;
    }

    public void setCreatedby(String createdby) {
        this.createdby = createdby;
    }

    public String getUpdatedby() {
        return updatedby;
    }

    public void setUpdatedby(String updatedby) {
        this.updatedby = updatedby;
    }

    public String getRowpointer() {
        return rowpointer;
    }

    public void setRowpointer(String rowpointer) {
        this.rowpointer = rowpointer;
    }

    public double getMonto_credito() {
        return monto_credito;
    }

    public void setMonto_credito(double monto_credito) {
        this.monto_credito = monto_credito;
    }

    public double getMonto_debito() {
        return monto_debito;
    }

    public void setMonto_debito(double monto_debito) {
        this.monto_debito = monto_debito;
    }

    public double getMonto_prov() {
        return monto_prov;
    }

    public void setMonto_prov(double monto_prov) {
        this.monto_prov = monto_prov;
    }

    public double getMonto_local() {
        return monto_local;
    }

    public void setMonto_local(double monto_local) {
        this.monto_local = monto_local;
    }

    public double getMonto_dolar() {
        return monto_dolar;
    }

    public void setMonto_dolar(double monto_dolar) {
        this.monto_dolar = monto_dolar;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public static ArrayList<Pagos> getPagosPorDoc(String doc, ArrayList<Pagos> listaPagos) {
        ArrayList<Pagos> lista = new ArrayList<>();
        listaPagos.forEach(e -> {
            if (e.getCredito().contains(doc)) {
                lista.add(e);
            }
        });
        return lista;
    }
}
