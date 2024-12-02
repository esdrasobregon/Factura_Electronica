/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entitys.ProveedorContado;

import java.util.Date;

/**
 *
 * @author eobregon
 */
public class TelefonoSinpeContado {
    int idTelefonoSinpeContado;
    String Numero;
    int Estado;
    java.util.Date Creado;
    int ProveedorContado_idProveedorContado;

    public int getIdTelefonoSinpeContado() {
        return idTelefonoSinpeContado;
    }

    public void setIdTelefonoSinpeContado(int idTelefonoSinpeContado) {
        this.idTelefonoSinpeContado = idTelefonoSinpeContado;
    }

    public String getNumero() {
        return Numero;
    }

    public void setNumero(String Numero) {
        this.Numero = Numero;
    }

    public int getEstado() {
        return Estado;
    }

    public void setEstado(int Estado) {
        this.Estado = Estado;
    }

    public Date getCreado() {
        return Creado;
    }

    public void setCreado(Date Creado) {
        this.Creado = Creado;
    }

    public int getProveedorContado_idProveedorContado() {
        return ProveedorContado_idProveedorContado;
    }

    public void setProveedorContado_idProveedorContado(int ProveedorContado_idProveedorContado) {
        this.ProveedorContado_idProveedorContado = ProveedorContado_idProveedorContado;
    }
    
}
