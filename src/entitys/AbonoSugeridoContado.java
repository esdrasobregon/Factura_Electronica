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
public class AbonoSugeridoContado {

    private int idAbonoSugeridoContado;      // Corresponds to idAbonoSugeridoContado
    private String documento;                // Corresponds to Documento
    private int id;                          // Corresponds to ID
    private double saldoActual;              // Corresponds to Saldo_Actual (now as double)
    private double saldoActualColones;       // Corresponds to Saldo_Actual_Colones (now as double)
    private double abono;                    // Corresponds to Abono (now as double)
    private double abonoColones;             // Corresponds to Abono_Colones (now as double)
    private String moneda;                   // Corresponds to Moneda
    private String usuario;                  // Corresponds to Usuario
    private int semana;                      // Corresponds to Semana
    private int aprobado;                    // Corresponds to Aprobado (now as int)
    private int revisadoConta;               // Corresponds to RevisadoConta (now as int)
    private String usuarioRevision;          // Corresponds to UsuarioRevision
    private java.util.Date fechaRevisionConta;         // Corresponds to Fecha_Revision_Conta
    private java.util.Date fechaSolicitud;             // Corresponds to Fecha_Solicitud
    private String ctPresupuesto, descCtaPres;
    private String nombreProveedor, forma_pago, sociedad;
    private int proveedor;
    private java.util.Date fechaDocumento;
    private int adelanto;
    private double montoOriginal, montoOriginalColones;

    public double getMontoOriginal() {
        return montoOriginal;
    }

    public void setMontoOriginal(double montoOriginal) {
        this.montoOriginal = montoOriginal;
    }

    public double getMontoOriginalColones() {
        return montoOriginalColones;
    }

    public void setMontoOriginalColones(double montoOriginalColones) {
        this.montoOriginalColones = montoOriginalColones;
    }

    public int getAdelanto() {
        return adelanto;
    }

    public void setAdelanto(int adelanto) {
        this.adelanto = adelanto;
    }

    public String getSociedad() {
        return sociedad;
    }

    public void setSociedad(String sociedad) {
        this.sociedad = sociedad;
    }

    public Date getFechaDocumento() {
        return fechaDocumento;
    }

    public void setFechaDocumento(Date fechaDocumento) {
        this.fechaDocumento = fechaDocumento;
    }

    public String getForma_pago() {
        return forma_pago;
    }

    public void setForma_pago(String forma_pago) {
        this.forma_pago = forma_pago;
    }

    public String getNombreProveedor() {
        return nombreProveedor;
    }

    public void setNombreProveedor(String nombreProveedor) {
        this.nombreProveedor = nombreProveedor;
    }

    public int getProveedor() {
        return proveedor;
    }

    public void setProveedor(int proveedor) {
        this.proveedor = proveedor;
    }

    public String getCtPresupuesto() {
        return ctPresupuesto;
    }

    public void setCtPresupuesto(String ctPresupuesto) {
        this.ctPresupuesto = ctPresupuesto;
    }

    public String getDescCtaPres() {
        return descCtaPres;
    }

    public void setDescCtaPres(String descCtaPres) {
        this.descCtaPres = descCtaPres;
    }

    public int getIdAbonoSugeridoContado() {
        return idAbonoSugeridoContado;
    }

    public void setIdAbonoSugeridoContado(int idAbonoSugeridoContado) {
        this.idAbonoSugeridoContado = idAbonoSugeridoContado;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getSaldoActual() {
        return saldoActual;
    }

    public void setSaldoActual(double saldoActual) {
        this.saldoActual = saldoActual;
    }

    public double getSaldoActualColones() {
        return saldoActualColones;
    }

    public void setSaldoActualColones(double saldoActualColones) {
        this.saldoActualColones = saldoActualColones;
    }

    public double getAbono() {
        return abono;
    }

    public void setAbono(double abono) {
        this.abono = abono;
    }

    public double getAbonoColones() {
        return abonoColones;
    }

    public void setAbonoColones(double abonoColones) {
        this.abonoColones = abonoColones;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public int getSemana() {
        return semana;
    }

    public void setSemana(int semana) {
        this.semana = semana;
    }

    public int getAprobado() {
        return aprobado;
    }

    public void setAprobado(int aprobado) {
        this.aprobado = aprobado;
    }

    public int getRevisadoConta() {
        return revisadoConta;
    }

    public void setRevisadoConta(int revisadoConta) {
        this.revisadoConta = revisadoConta;
    }

    public String getUsuarioRevision() {
        return usuarioRevision;
    }

    public void setUsuarioRevision(String usuarioRevision) {
        this.usuarioRevision = usuarioRevision;
    }

    public Date getFechaRevisionConta() {
        return fechaRevisionConta;
    }

    public void setFechaRevisionConta(Date fechaRevisionConta) {
        this.fechaRevisionConta = fechaRevisionConta;
    }

    public Date getFechaSolicitud() {
        return fechaSolicitud;
    }

    public void setFechaSolicitud(Date fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }

    public static AbonoSugeridoContado obtAbonoSugeridoContadoPorId(int doc, ArrayList<AbonoSugeridoContado> listaAbonosContado) {
        AbonoSugeridoContado res = null;
        int count = 0;
        boolean found = false;
        while (count < listaAbonosContado.size() && !found) {
            AbonoSugeridoContado r = listaAbonosContado.get(count);
            if (r.getIdAbonoSugeridoContado() == doc) {
                res = r;
                found = true;
            }
            count++;
        }
        return res;
    }
}
