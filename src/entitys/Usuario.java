/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entitys;

import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author programador1
 */
public class Usuario {

    int idUsuario;
    int empresa;
    int id;
    String IdEmpleado;
    String nombre;
    String Identificacion;
    String Primer_Apellido;
    String Segundo_Apellido;
    int activo;
    String IdTarjeta;
    Date fechaIngreso;
    int departamento;
    int idPuesto;
    int jornada;
    String nombreUsuario;
    String contrasenya;
    int tipoUsuario;
    String centroCosto;
    int diaLibre;
    String KeyEmpleado;

    public String getKeyEmpleado() {
        return KeyEmpleado;
    }

    public void setKeyEmpleado(String KeyEmpleado) {
        this.KeyEmpleado = KeyEmpleado;
    }

    public int getEmpresa() {
        return empresa;
    }

    public void setEmpresa(int empresa) {
        this.empresa = empresa;
    }

    public int getDiaLibre() {
        return diaLibre;
    }

    public void setDiaLibre(int diaLibre) {
        this.diaLibre = diaLibre;
    }

    public String getCentroCosto() {
        return centroCosto;
    }

    public void setCentroCosto(String centroCosto) {
        this.centroCosto = centroCosto;
    }

    public int getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(int tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getContrasenya() {
        return contrasenya;
    }

    public void setContrasenya(String contrasenya) {
        this.contrasenya = contrasenya;
    }

    public int getJornada() {
        return jornada;
    }

    public void setJornada(int jornada) {
        this.jornada = jornada;
    }

    public Usuario() {
    }

    public Usuario(int id, String IdEmpleado, String nombre, String Identificacion, String Primer_Apellido, String Segundo_Apellido, int activo, String IdTarjeta, Date fechaIngreso, int departamento, int idPuesto) {
        this.id = id;
        this.IdEmpleado = IdEmpleado;
        this.nombre = nombre;
        this.Identificacion = Identificacion;
        this.Primer_Apellido = Primer_Apellido;
        this.Segundo_Apellido = Segundo_Apellido;
        this.activo = activo;
        this.IdTarjeta = IdTarjeta;
        this.fechaIngreso = fechaIngreso;
        this.departamento = departamento;
        this.idPuesto = idPuesto;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdEmpleado() {
        return IdEmpleado;
    }

    public void setIdEmpleado(String IdEmpleado) {
        this.IdEmpleado = IdEmpleado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getIdentificacion() {
        return Identificacion;
    }

    public void setIdentificacion(String Identificacion) {
        this.Identificacion = Identificacion;
    }

    public String getPrimer_Apellido() {
        return Primer_Apellido;
    }

    public void setPrimer_Apellido(String Primer_Apellido) {
        this.Primer_Apellido = Primer_Apellido;
    }

    public String getSegundo_Apellido() {
        return Segundo_Apellido;
    }

    public void setSegundo_Apellido(String Segundo_Apellido) {
        this.Segundo_Apellido = Segundo_Apellido;
    }

    public int getActivo() {
        return activo;
    }

    public void setActivo(int activo) {
        this.activo = activo;
    }

    public String getIdTarjeta() {
        return IdTarjeta;
    }

    public void setIdTarjeta(String IdTarjeta) {
        this.IdTarjeta = IdTarjeta;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public int getDepartamento() {
        return departamento;
    }

    public void setDepartamento(int departamento) {
        this.departamento = departamento;
    }

    public int getIdPuesto() {
        return idPuesto;
    }

    public void setIdPuesto(int idPuesto) {
        this.idPuesto = idPuesto;
    }

    public static Usuario getUsuarioPorKey(String KeyEmpleado, ArrayList<Usuario> listaUsuarios) {
        int count = 0;
        boolean flag = false;
        Usuario us = null;
        while (!flag && count < listaUsuarios.size()) {
            Usuario usu = listaUsuarios.get(count);
            if (usu.getKeyEmpleado().equalsIgnoreCase(KeyEmpleado)) {
                us = usu;
                flag = true;
            }
            count++;
        }

        return us;

    }

    public static ArrayList<Usuario> getSubLista(int idDepartamento, ArrayList<Usuario> listaUsuarios) {
        ArrayList<Usuario> lista = new ArrayList<>();
        listaUsuarios.forEach(e -> {

            if (e.getDepartamento() == idDepartamento) {
                lista.add(e);
            }
        });
        return lista;
    }

    public static String getEmpresa(int idEmpresa) {
        String value = "";
        switch (idEmpresa) {
            case 1:
                value = "CILT";
                break;
            case 2:
                value = "Turintel";
                break;
            case 3:
                value = "Rymsa";
                break;
            case 4:
                value = "Irasa";
                break;
            case 5:
                value = "Katrasa";
                break;
        }
        return value;
    }

}
