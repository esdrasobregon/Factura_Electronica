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
public class Proveedor {

    String CIA_PROV, proveedor, nombre, alias, activo;

    public String getCIA_PROV() {
        return CIA_PROV;
    }

    public void setCIA_PROV(String CIA_PROV) {
        this.CIA_PROV = CIA_PROV;
    }

    public String getProveedor() {
        return proveedor;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getActivo() {
        return activo;
    }

    public void setActivo(String activo) {
        this.activo = activo;
    }

    public static Proveedor obtProvPorCiaId(ArrayList<Proveedor> listaProv, String cia_prov) {
        Proveedor res = null;
        int count = 0;
        boolean found = false;
        while (!found && count < listaProv.size()) {

            Proveedor p = listaProv.get(count);
            if (p.getCIA_PROV().equals(cia_prov)) {
                res = p;
                found = true;
            }
            count++;

        }
        return res;
    }
}
