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
public class AbonoSugerido {

    String documentoExactus //si el documento no se encuentra tiene que revisar
            , comentarios, Exactus_Doc, UsuarioRevision, Documento, CIA, Cuenta_Presupuesto, Numero_Proveedor, Moneda, Usuario, Nombre_Proveedor, Tipo_Proveedor, Descripion_Cta_Presupuesto;
    int Semana, Aprobado, RevisadoConta, id;
    double Monto_Original, Saldo_Actuual, Abono, Saldo_Restante, Monto_Colones;
    boolean Abono_Total;
    java.util.Date Fecha_Creacion, Fecha_documento, Fecha_Revision_Conta, fecha_cambio, Fecha_Revision, Fecha_Solicitud;

    public Date getFecha_Revision() {
        return Fecha_Revision;
    }

    public void setFecha_Revision(Date Fecha_Revision) {
        this.Fecha_Revision = Fecha_Revision;
    }

    public Date getFecha_Solicitud() {
        return Fecha_Solicitud;
    }

    public void setFecha_Solicitud(Date Fecha_Solicitud) {
        this.Fecha_Solicitud = Fecha_Solicitud;
    }

    public Date getFecha_cambio() {
        return fecha_cambio;
    }

    public void setFecha_cambio(Date fecha_cambio) {
        this.fecha_cambio = fecha_cambio;
    }

    public String getComentarios() {
        return comentarios;
    }

    public String getDocumentoExactus() {
        return documentoExactus;
    }

    public void setDocumentoExactus(String documentoExactus) {
        this.documentoExactus = documentoExactus;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getExactus_Doc() {
        return Exactus_Doc;
    }

    public void setExactus_Doc(String Exactus_Doc) {
        this.Exactus_Doc = Exactus_Doc;
    }

    public String getUsuarioRevision() {
        return UsuarioRevision;
    }

    public void setUsuarioRevision(String UsuarioRevision) {
        this.UsuarioRevision = UsuarioRevision;
    }

    public int getRevisadoConta() {
        return RevisadoConta;
    }

    public void setRevisadoConta(int RevisadoConta) {
        this.RevisadoConta = RevisadoConta;
    }

    public Date getFecha_Revision_Conta() {
        return Fecha_Revision_Conta;
    }

    public void setFecha_Revision_Conta(Date Fecha_Revision_Conta) {
        this.Fecha_Revision_Conta = Fecha_Revision_Conta;
    }

    public double getMonto_Colones() {
        return Monto_Colones;
    }

    public void setMonto_Colones(double Monto_Colones) {
        this.Monto_Colones = Monto_Colones;
    }

    public int getAprobado() {
        return Aprobado;
    }

    public void setAprobado(int Aprobado) {
        this.Aprobado = Aprobado;
    }

    public String getDescripion_Cta_Presupuesto() {
        return Descripion_Cta_Presupuesto;
    }

    public void setDescripion_Cta_Presupuesto(String Descripion_Cta_Presupuesto) {
        this.Descripion_Cta_Presupuesto = Descripion_Cta_Presupuesto;
    }

    public int getSemana() {
        return Semana;
    }

    public void setSemana(int Semana) {
        this.Semana = Semana;
    }

    public String getTipo_Proveedor() {
        return Tipo_Proveedor;
    }

    public void setTipo_Proveedor(String Tipo_Proveedor) {
        this.Tipo_Proveedor = Tipo_Proveedor;
    }

    public Date getFecha_documento() {
        return Fecha_documento;
    }

    public void setFecha_documento(Date Fecha_documento) {
        this.Fecha_documento = Fecha_documento;
    }

    public String getNombre_Proveedor() {
        return Nombre_Proveedor;
    }

    public void setNombre_Proveedor(String Nombre_Proveedor) {
        this.Nombre_Proveedor = Nombre_Proveedor;
    }

    public String getUsuario() {
        return Usuario;
    }

    public void setUsuario(String Usuario) {
        this.Usuario = Usuario;
    }

    public String getMoneda() {
        return Moneda;
    }

    public void setMoneda(String Moneda) {
        this.Moneda = Moneda;
    }

    public String getDocumento() {
        return Documento;
    }

    public void setDocumento(String Documento) {
        this.Documento = Documento;
    }

    public String getCIA() {
        return CIA;
    }

    public void setCIA(String CIA) {
        this.CIA = CIA;
    }

    public String getCuenta_Presupuesto() {
        return Cuenta_Presupuesto;
    }

    public void setCuenta_Presupuesto(String Cuenta_Presupuesto) {
        this.Cuenta_Presupuesto = Cuenta_Presupuesto;
    }

    public String getNumero_Proveedor() {
        return Numero_Proveedor;
    }

    public void setNumero_Proveedor(String Numero_Proveedor) {
        this.Numero_Proveedor = Numero_Proveedor;
    }

    public double getMonto_Original() {
        return Monto_Original;
    }

    public void setMonto_Original(double Monto_Original) {
        this.Monto_Original = Monto_Original;
    }

    public double getSaldo_Actuual() {
        return Saldo_Actuual;
    }

    public void setSaldo_Actuual(double Saldo_Actuual) {
        this.Saldo_Actuual = Saldo_Actuual;
    }

    public double getAbono() {
        return Abono;
    }

    public void setAbono(double Abono) {
        this.Abono = Abono;
    }

    public double getSaldo_Restante() {
        return Saldo_Restante;
    }

    public void setSaldo_Restante(double Saldo_Restante) {
        this.Saldo_Restante = Saldo_Restante;
    }

    public boolean getAbono_Total() {
        return Abono_Total;
    }

    public void setAbono_Total(boolean Abono_Total) {
        this.Abono_Total = Abono_Total;
    }

    public Date getFecha_Creacion() {
        return Fecha_Creacion;
    }

    public void setFecha_Creacion(Date Fecha_Creacion) {
        this.Fecha_Creacion = Fecha_Creacion;
    }

    public static double getSaldoFinal(ArrayList<AbonoSugerido> sugeridos) {
        double res = 0;
        for (AbonoSugerido s : sugeridos) {
            res += s.getAbono();
        }
        return res;
    }

    public static boolean tieneAbonoMismaSemana(String doc, int currentWeek, ArrayList<AbonoSugerido> sugeridos) {
        int count = 0;
        boolean flag = false;
        while (count < sugeridos.size() && !flag) {
            AbonoSugerido ab = sugeridos.get(count);
            int week = logic.CommonDateFunctions.getWeekOfTheYear(ab.getFecha_Creacion());
            if (week == currentWeek) {
                flag = true;
            }
            count++;
        }
        return flag;
    }

    public static AbonoSugerido obtenerAbonoMismaSemana(String doc, int currentWeek, ArrayList<AbonoSugerido> sugeridos) {
        AbonoSugerido a = null;
        int count = 0;
        boolean flag = false;
        while (count < sugeridos.size() && !flag) {
            AbonoSugerido ab = sugeridos.get(count);
            int week = logic.CommonDateFunctions.getWeekOfTheYear(ab.getFecha_Creacion());
            if (week == currentWeek) {
                a = ab;
                flag = true;
            }
            count++;
        }
        return a;
    }

    public static AbonoSugerido obtenerAbonoFromList(String doc, java.util.Date date, int id, ArrayList<AbonoSugerido> sugeridos) {
        AbonoSugerido a = null;
        int count = 0;
        boolean flag = false;
        while (count < sugeridos.size() && !flag) {
            AbonoSugerido ab = sugeridos.get(count);
            if (ab.getDocumento().equals(doc) && ab.getFecha_Creacion().equals(date) && ab.getId() == id) {
                flag = true;
                a = ab;
            }
            count++;
        }
        return a;
    }

    public static AbonoSugerido obtenerAbonoPorId(int pId, ArrayList<AbonoSugerido> sugeridos) {
        AbonoSugerido a = null;
        int count = 0;
        boolean flag = false;
        while (count < sugeridos.size() && !flag) {
            AbonoSugerido ab = sugeridos.get(count);
            if (ab.getId() == pId) {
                flag = true;
                a = ab;
            }
            count++;
        }
        return a;
    }
}
