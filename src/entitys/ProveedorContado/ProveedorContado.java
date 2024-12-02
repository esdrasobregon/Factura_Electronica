/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entitys.ProveedorContado;

import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author eobregon
 */
public class ProveedorContado {

    private int totalCuentas;
    private int sinpes;
    private int idProveedorContado;
    private String Nombre;
    private int Estado;
    private java.util.Date FechaCreacion;
    private java.util.Date UltimaModificacion;
    private String UsuarioCreador;
    private String UsuarioModificador;
    private String Codigo;
    private String CedulaJuridica;
    private ArrayList<entitys.ProveedorContado.CuentaProveedorContado> cuentas;
    private ArrayList<entitys.ProveedorContado.TelefonoSinpeContado> telefonos;

    public int getTotalCuentas() {
        return totalCuentas;
    }

    public void setTotalCuentas(int totalCuentas) {
        this.totalCuentas = totalCuentas;
    }

    public int getSinpes() {
        return sinpes;
    }

    public void setSinpes(int sinpes) {
        this.sinpes = sinpes;
    }

    public ArrayList<CuentaProveedorContado> getCuentas() {
        return cuentas;
    }

    public void setCuentas(ArrayList<CuentaProveedorContado> cuentas) {
        this.cuentas = cuentas;
    }

    public ArrayList<TelefonoSinpeContado> getTelefonos() {
        return telefonos;
    }

    public void setTelefonos(ArrayList<TelefonoSinpeContado> telefonos) {
        this.telefonos = telefonos;
    }

    
    public int getIdProveedorContado() {
        return idProveedorContado;
    }

    public void setIdProveedorContado(int idProveedorContado) {
        this.idProveedorContado = idProveedorContado;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    public int getEstado() {
        return Estado;
    }

    public void setEstado(int Estado) {
        this.Estado = Estado;
    }

    public Date getFechaCreacion() {
        return FechaCreacion;
    }

    public void setFechaCreacion(Date FechaCreacion) {
        this.FechaCreacion = FechaCreacion;
    }

    public Date getUltimaModificacion() {
        return UltimaModificacion;
    }

    public void setUltimaModificacion(Date UltimaModificacion) {
        this.UltimaModificacion = UltimaModificacion;
    }

    public String getUsuarioCreador() {
        return UsuarioCreador;
    }

    public void setUsuarioCreador(String UsuarioCreador) {
        this.UsuarioCreador = UsuarioCreador;
    }

    public String getUsuarioModificador() {
        return UsuarioModificador;
    }

    public void setUsuarioModificador(String UsuarioModificador) {
        this.UsuarioModificador = UsuarioModificador;
    }

    public String getCodigo() {
        return Codigo;
    }

    public void setCodigo(String Codigo) {
        this.Codigo = Codigo;
    }

    public String getCedulaJuridica() {
        return CedulaJuridica;
    }

    public void setCedulaJuridica(String CedulaJuridica) {
        this.CedulaJuridica = CedulaJuridica;
    }

}
