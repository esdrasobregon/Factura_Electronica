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
public class AsientoFactura {
    
    String CIA, PROVEEDOR, CEDJUR, NOMBRE, DOCUMENTO, MONEDA, ASIENTO, TIPODOC, DES_TIPO;
    int SUBTIPO;
    
    public int getSUBTIPO() {
        return SUBTIPO;
    }
    
    public void setSUBTIPO(int SUBTIPO) {
        this.SUBTIPO = SUBTIPO;
    }
    
    public String getDES_TIPO() {
        return DES_TIPO;
    }
    
    public void setDES_TIPO(String DES_TIPO) {
        this.DES_TIPO = DES_TIPO;
    }
    
    public String getTIPODOC() {
        return TIPODOC;
    }
    
    public void setTIPODOC(String TIPODOC) {
        this.TIPODOC = TIPODOC;
    }
    java.util.Date FECHA_DOCUMENTO;
    double MONTO;
    
    public Date getFECHA_DOCUMENTO() {
        return FECHA_DOCUMENTO;
    }
    
    public void setFECHA_DOCUMENTO(Date FECHA_DOCUMENTO) {
        this.FECHA_DOCUMENTO = FECHA_DOCUMENTO;
    }
    
    public String getCIA() {
        return CIA;
    }
    
    public void setCIA(String CIA) {
        this.CIA = CIA;
    }
    
    public String getPROVEEDOR() {
        return PROVEEDOR;
    }
    
    public void setPROVEEDOR(String PROVEEDOR) {
        this.PROVEEDOR = PROVEEDOR;
    }
    
    public String getCEDJUR() {
        return CEDJUR;
    }
    
    public void setCEDJUR(String CEDJUR) {
        this.CEDJUR = CEDJUR;
    }
    
    public String getNOMBRE() {
        return NOMBRE;
    }
    
    public void setNOMBRE(String NOMBRE) {
        this.NOMBRE = NOMBRE;
    }
    
    public String getDOCUMENTO() {
        return DOCUMENTO;
    }
    
    public void setDOCUMENTO(String DOCUMENTO) {
        this.DOCUMENTO = DOCUMENTO;
    }
    
    public String getMONEDA() {
        return MONEDA;
    }
    
    public void setMONEDA(String MONEDA) {
        this.MONEDA = MONEDA;
    }
    
    public String getASIENTO() {
        return ASIENTO;
    }
    
    public void setASIENTO(String ASIENTO) {
        this.ASIENTO = ASIENTO;
    }
    
    public double getMONTO() {
        return MONTO;
    }
    
    public void setMONTO(double MONTO) {
        this.MONTO = MONTO;
    }
    
    public static String getAsientoPorCosecutivo(Receips r, ArrayList<AsientoFactura> listaAsientos, String tipoDoc) {
        String res = "";
        boolean found = false;
        int count = 0;
        while (!found && count < listaAsientos.size()) {
            AsientoFactura a = listaAsientos.get(count);
            double totalExactus = a.getMONTO();
            double totalXML = r.getResumenFactura().getTotalComprobante();
            boolean diffAc = Math.abs((totalXML - totalExactus)) <= 1;
            int length = r.getNumeroConsecutivo().length();
            String documento = r.getNumeroConsecutivo().substring(length - 8, length).replaceFirst("^0+(?!$)", "");
            int idlength = r.getEmisor().getIdentificacion().length();
            String cedjur = r.getEmisor().getIdentificacion().substring(3, idlength);
            int rcdjlength = r.getReceptor().getIdentificacion().length();
            String rcdj = r.getReceptor().getIdentificacion().substring(3, rcdjlength);
            String asDoc = a.getDOCUMENTO().replaceAll("[^0-9]", "").replaceFirst("^0+(?!$)", "");
            if (documento.equalsIgnoreCase(asDoc)
                    && a.getCEDJUR().equalsIgnoreCase(cedjur)
                    && a.getCIA().equalsIgnoreCase(rcdj)
                    && diffAc
                    && a.getTIPODOC().equalsIgnoreCase(tipoDoc)) {
                res = a.getASIENTO();
                r.setSubTipoAsiento(a.getSUBTIPO());
                r.setDifereniciaXmlExactus(Math.abs(r.getResumenFactura().getTotalComprobante() - a.getMONTO()));
                found = true;
            }
            count++;
        }
        return res;
    }
}
//cedjur prob + documento + monto
