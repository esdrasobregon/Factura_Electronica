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
public class UsuariosPresupuesto {

    String DETA_CIA, COD_USER, DETA_USER, COD_DEPA, DETA_DEPA, ACCESO, COD_CIA;
    boolean activo;
    int usuarioConta, id, administracionUsuarios;
    int HistoricoAbonos,Exactus,ReportePagoAplicados, Exactus_TC, Exactus_CB, Exactus_CP, Exactus_Subtipos, ReportePagos, MantenimientoPagos, HistoricoCP, AdminFactSub, AdministradorGestionGastosPer, MantenimientoCompromisos;

    public int getHistoricoAbonos() {
        return HistoricoAbonos;
    }

    public void setHistoricoAbonos(int HistoricoAbonos) {
        this.HistoricoAbonos = HistoricoAbonos;
    }

    public int getReportePagoAplicados() {
        return ReportePagoAplicados;
    }

    public void setReportePagoAplicados(int ReportePagoAplicados) {
        this.ReportePagoAplicados = ReportePagoAplicados;
    }

    public int getMantenimientoCompromisos() {
        return MantenimientoCompromisos;
    }

    public void setMantenimientoCompromisos(int MantenimientoCompromisos) {
        this.MantenimientoCompromisos = MantenimientoCompromisos;
    }

    public int getAdministracionUsuarios() {
        return administracionUsuarios;
    }

    public void setAdministracionUsuarios(int administracionUsuarios) {
        this.administracionUsuarios = administracionUsuarios;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCOD_CIA() {
        return COD_CIA;
    }

    public void setCOD_CIA(String COD_CIA) {
        this.COD_CIA = COD_CIA;
    }

    public int getAdministradorGestionGastosPer() {
        return AdministradorGestionGastosPer;
    }

    public void setAdministradorGestionGastosPer(int AdministradorGestionGastosPer) {
        this.AdministradorGestionGastosPer = AdministradorGestionGastosPer;
    }

    public int getAdminFactSub() {
        return AdminFactSub;
    }

    public void setAdminFactSub(int AdminFactSub) {
        this.AdminFactSub = AdminFactSub;
    }

    public int getHistoricoCP() {
        return HistoricoCP;
    }

    public void setHistoricoCP(int HistoricoCP) {
        this.HistoricoCP = HistoricoCP;
    }

    public int getReportePagos() {
        return ReportePagos;
    }

    public void setReportePagos(int ReportePagos) {
        this.ReportePagos = ReportePagos;
    }

    public int getMantenimientoPagos() {
        return MantenimientoPagos;
    }

    public void setMantenimientoPagos(int MantenimientoPagos) {
        this.MantenimientoPagos = MantenimientoPagos;
    }

    public int getExactus_Subtipos() {
        return Exactus_Subtipos;
    }

    public void setExactus_Subtipos(int Exactus_Subtipos) {
        this.Exactus_Subtipos = Exactus_Subtipos;
    }

    public int getExactus() {
        return Exactus;
    }

    public void setExactus(int Exactus) {
        this.Exactus = Exactus;
    }

    public int getExactus_TC() {
        return Exactus_TC;
    }

    public void setExactus_TC(int Exactus_TC) {
        this.Exactus_TC = Exactus_TC;
    }

    public int getExactus_CB() {
        return Exactus_CB;
    }

    public void setExactus_CB(int Exactus_CB) {
        this.Exactus_CB = Exactus_CB;
    }

    public int getExactus_CP() {
        return Exactus_CP;
    }

    public void setExactus_CP(int Exactus_CP) {
        this.Exactus_CP = Exactus_CP;
    }

    public int getUsuarioConta() {
        return usuarioConta;
    }

    public void setUsuarioConta(int usuarioConta) {
        this.usuarioConta = usuarioConta;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public String getDETA_CIA() {
        return DETA_CIA;
    }

    public void setDETA_CIA(String DETA_CIA) {
        this.DETA_CIA = DETA_CIA;
    }

    public String getCOD_USER() {
        return COD_USER;
    }

    public void setCOD_USER(String COD_USER) {
        this.COD_USER = COD_USER;
    }

    public String getDETA_USER() {
        return DETA_USER;
    }

    public void setDETA_USER(String DETA_USER) {
        this.DETA_USER = DETA_USER;
    }

    public String getCOD_DEPA() {
        return COD_DEPA;
    }

    public void setCOD_DEPA(String COD_DEPA) {
        this.COD_DEPA = COD_DEPA;
    }

    public String getDETA_DEPA() {
        return DETA_DEPA;
    }

    public void setDETA_DEPA(String DETA_DEPA) {
        this.DETA_DEPA = DETA_DEPA;
    }

    public String getACCESO() {
        return ACCESO;
    }

    public void setACCESO(String ACCESO) {
        this.ACCESO = ACCESO;
    }

    public static ArrayList<UsuariosPresupuesto> getUsuariosPorNombreYUsuario(String buscar, ArrayList<UsuariosPresupuesto> usuarios) {
        ArrayList<UsuariosPresupuesto> lista = new ArrayList<>();
        usuarios.forEach(e -> {
            if (e.getDETA_USER().toUpperCase().contains(buscar) || e.getCOD_USER().toUpperCase().contains(buscar)) {
                lista.add(e);
            }
        });

        return lista;
    }
    public static ArrayList<UsuariosPresupuesto> getUsuariosPorNombre(String buscar, ArrayList<UsuariosPresupuesto> usuarios) {
        ArrayList<UsuariosPresupuesto> lista = new ArrayList<>();
        usuarios.forEach(e -> {
            if (e.getCOD_USER().toUpperCase().contains(buscar)) {
                lista.add(e);
            }
        });

        return lista;
    }

}
