/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entitys;

import java.util.ArrayList;

/**
 *
 * @author eobregon
 */
public class SubtiposExactus {

    private String TIPO;
    private String SUBTIPO;
    private String DESCRIPCION;
    private String Nombre_Proveedor;
    private String DOCUMENTO;
    private java.util.Date FECHA_DOCUMENTO;
    private String APLICACION;
    private double MONTO;
    private double MONTO_DOLAR;
    private String ASIENTO;
    private String USUARIO_ULT_MOD;
    private String IdRow;
    private String sociedad;
    private String anteriorSubtipo = "";
    private String moneda;
    private int tipoCuenta; //cp 0, cb 1

    public int getTipoCuenta() {
        return tipoCuenta;
    }

    public void setTipoCuenta(int tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
    }

    public String getAnteriorSubtipo() {
        return anteriorSubtipo;
    }

    public void setAnteriorSubtipo(String anteriorSubtipo) {
        this.anteriorSubtipo = anteriorSubtipo;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public String getanteriorSubtipo() {
        return anteriorSubtipo;
    }

    public void setanteriorSubtipo(String nuevoSubtipo) {
        this.anteriorSubtipo = nuevoSubtipo;
    }

    public String getSociedad() {
        return sociedad;
    }

    public void setSociedad(String sociedad) {
        this.sociedad = sociedad;
    }

    public String getTIPO() {
        return TIPO;
    }

    public void setTIPO(String TIPO) {
        this.TIPO = TIPO;
    }

    public String getSUBTIPO() {
        return SUBTIPO;
    }

    public void setSUBTIPO(String SUBTIPO) {
        this.SUBTIPO = SUBTIPO;
    }

    public String getDESCRIPCION() {
        return DESCRIPCION;
    }

    public void setDESCRIPCION(String DESCRIPCION) {
        this.DESCRIPCION = DESCRIPCION;
    }

    public String getNombre_Proveedor() {
        return Nombre_Proveedor;
    }

    public void setNombre_Proveedor(String Nombre_Proveedor) {
        this.Nombre_Proveedor = Nombre_Proveedor;
    }

    public String getDOCUMENTO() {
        return DOCUMENTO;
    }

    public void setDOCUMENTO(String DOCUMENTO) {
        this.DOCUMENTO = DOCUMENTO;
    }

    public java.util.Date getFECHA_DOCUMENTO() {
        return FECHA_DOCUMENTO;
    }

    public void setFECHA_DOCUMENTO(java.util.Date FECHA_DOCUMENTO) {
        this.FECHA_DOCUMENTO = FECHA_DOCUMENTO;
    }

    public String getAPLICACION() {
        return APLICACION;
    }

    public void setAPLICACION(String APLICACION) {
        this.APLICACION = APLICACION;
    }

    public double getMONTO() {
        return MONTO;
    }

    public void setMONTO(double MONTO) {
        this.MONTO = MONTO;
    }

    public double getMONTO_DOLAR() {
        return MONTO_DOLAR;
    }

    public void setMONTO_DOLAR(double MONTO_DOLAR) {
        this.MONTO_DOLAR = MONTO_DOLAR;
    }

    public String getASIENTO() {
        return ASIENTO;
    }

    public void setASIENTO(String ASIENTO) {
        this.ASIENTO = ASIENTO;
    }

    public String getUSUARIO_ULT_MOD() {
        return USUARIO_ULT_MOD;
    }

    public void setUSUARIO_ULT_MOD(String USUARIO_ULT_MOD) {
        this.USUARIO_ULT_MOD = USUARIO_ULT_MOD;
    }

    public String getIdRow() {
        return IdRow;
    }

    public void setIdRow(String IdRow) {
        this.IdRow = IdRow;
    }

    public static SubtiposExactus getSubtipoPorDesc(String selectedsubtipo, ArrayList<SubtiposExactus> listaSubtipos) {
        SubtiposExactus res = null;
        int count = 0;
        boolean flag = false;
        while (!flag && count < listaSubtipos.size()) {
            SubtiposExactus s = listaSubtipos.get(count);
            if (s.DESCRIPCION.equalsIgnoreCase(selectedsubtipo)) {
                res = s;
                flag = true;
            }
            count++;
        }
        return res;
    }

    public static SubtiposExactus getSubtipoPorIdRow(String idrow, ArrayList<SubtiposExactus> listaSubtipos) {
        SubtiposExactus res = null;
        int count = 0;
        boolean flag = false;
        while (!flag && count < listaSubtipos.size()) {
            SubtiposExactus s = listaSubtipos.get(count);
            if (s.getIdRow().equalsIgnoreCase(idrow)) {
                res = s;
                flag = true;
            }
            count++;
        }
        return res;
    }

    public static SubtiposExactus getSubtipoPorIdRow(String idrow, int tipoCuenta, ArrayList<SubtiposExactus> listaSubtipos) {
        SubtiposExactus res = null;
        int count = 0;
        boolean flag = false;
        while (!flag && count < listaSubtipos.size()) {
            SubtiposExactus s = listaSubtipos.get(count);
            if (s.getIdRow().equalsIgnoreCase(idrow) && s.getTipoCuenta() == tipoCuenta) {
                res = s;
                flag = true;
            }
            count++;
        }
        return res;
    }

    public static ArrayList<SubtiposExactus> getSubtipoPorAsiento(String asiento, ArrayList<SubtiposExactus> listaSubtipos) {
        ArrayList<SubtiposExactus> lista = new ArrayList<>();
        listaSubtipos.forEach(e -> {
            if (e.getASIENTO().toUpperCase().contains(asiento.toUpperCase())) {
                lista.add(e);
            }
        });
        return lista;
    }

    public static ArrayList<SubtiposExactus> getSubtipoPorProvedor(String prov, ArrayList<SubtiposExactus> listaSubtipos) {
        ArrayList<SubtiposExactus> lista = new ArrayList<>();
        listaSubtipos.forEach(e -> {
            if (e.getNombre_Proveedor().toUpperCase().contains(prov.toUpperCase())) {
                lista.add(e);
            }
        });
        return lista;
    }

    public static ArrayList<SubtiposExactus> getSubListTipoPorFactura(String factura, ArrayList<SubtiposExactus> listaSubtipos) {
        ArrayList<SubtiposExactus> lista = new ArrayList<>();
        listaSubtipos.forEach(e -> {
            if (e.getDOCUMENTO().contains(factura.toUpperCase())) {
                lista.add(e);
            }
        });
        return lista;
    }

    public static SubtiposExactus getSubtipoFromOther(SubtiposExactus rs, String selectedsubtipo, String anteriorSubtio) {
        SubtiposExactus p = new SubtiposExactus();
        p.setTIPO(rs.getTIPO());
        p.setSUBTIPO(rs.getSUBTIPO());
        p.setanteriorSubtipo(anteriorSubtio);
        p.setDESCRIPCION(selectedsubtipo);
        p.setNombre_Proveedor(rs.getNombre_Proveedor());
        p.setDOCUMENTO(rs.getDOCUMENTO());
        p.setFECHA_DOCUMENTO(rs.getFECHA_DOCUMENTO());
        p.setMONTO(rs.getMONTO());
        p.setMONTO_DOLAR(rs.getMONTO_DOLAR());
        p.setASIENTO(rs.getASIENTO());
        p.setSUBTIPO(rs.getSUBTIPO());
        p.setAPLICACION(rs.getAPLICACION());
        p.setUSUARIO_ULT_MOD(rs.getUSUARIO_ULT_MOD());
        p.setIdRow(rs.getIdRow());
        p.setSociedad(rs.getSociedad());

        return p;
    }

}
