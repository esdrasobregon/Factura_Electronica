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
public class CuentaProveedorContado {

    private int idCuentaContado;
    private int idProveedorContado;
    private String banco;
    private String numero;
    private int estado;
    private java.util.Date creado;

    public int getIdCuentaContado() {
        return idCuentaContado;
    }

    public void setIdCuentaContado(int idCuentaContado) {
        this.idCuentaContado = idCuentaContado;
    }

    public int getIdProveedorContado() {
        return idProveedorContado;
    }

    public void setIdProveedorContado(int idProveedorContado) {
        this.idProveedorContado = idProveedorContado;
    }

    public String getBanco() {
        return banco;
    }

    public void setBanco(String banco) {
        this.banco = banco;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public Date getCreado() {
        return creado;
    }

    public void setCreado(Date creado) {
        this.creado = creado;
    }

}
