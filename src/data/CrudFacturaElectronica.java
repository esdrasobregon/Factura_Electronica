/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package data;

import entitys.CodigoMoneda;
import entitys.Emisor;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import entitys.Receips;
import entitys.Receptor;
import entitys.ResumenFactura;
import java.util.logging.Level;
import logic.AppLogger;

/**
 *
 * @author eobregon
 */
public class CrudFacturaElectronica {

    public static boolean addUpdateFacturaElectronica(Receips c) {
        boolean res = false;
        try {
            //here sonoo is database name, root is username and password 
            String n = c.getCodigoActividad() == null ? "no aplica" : c.getCodigoActividad();
            String sentencia = "call addUpdateFacturaElectronica('"
                    + c.getNombreArchivo() + "','"
                    + c.getNumeroConsecutivo() + "','"
                    + c.getIdDepartamento() + "','"
                    + new java.sql.Date(c.getFechaEmision().getTime()) + "','"
                    + n + "','"
                    + c.getClave() + "','"
                    + c.getResumenFactura().getCodigoMoneda().getCodigoMoneda() + "','"
                    + c.getResumenFactura().getCodigoMoneda().getTipoCambio() + "','"
                    + c.getResumenFactura().getTotalServGravados() + "','"
                    + c.getResumenFactura().getTotalServExentos() + "','"
                    + c.getResumenFactura().getTotalServExonerado() + "','"
                    + c.getResumenFactura().getTotalMercanciasGravadas() + "','"
                    + c.getResumenFactura().getTotalMercanciasExentas() + "','"
                    + c.getResumenFactura().getTotalMercExonerada() + "','"
                    + c.getResumenFactura().getTotalGravado() + "','"
                    + c.getResumenFactura().getTotalExento() + "','"
                    + c.getResumenFactura().getTotalExonerado() + "','"
                    + c.getResumenFactura().getTotalVenta() + "','"
                    + c.getResumenFactura().getTotalDescuentos() + "','"
                    + c.getResumenFactura().getTotalVentaNeta() + "','"
                    + c.getResumenFactura().getTotalImpuesto() + "','"
                    + c.getResumenFactura().getTotalIVADevuelto() + "','"
                    + c.getResumenFactura().getTotalOtrosCargos() + "','"
                    + c.getResumenFactura().getTotalComprobante() + "','"
                    + c.getExactus() + "','"
                    + c.getEstado() + "','"
                    + c.getEmailIndex() + "','"
                    + c.getReceptor().getCorreoElectronico() + "','"
                    + c.getEmisor().getCorreoElectronico() + "','"
                    + c.getEmisor().getNombre() + "','"
                    + c.getReceptor().getNombre() + "','"
                    + DataUser.username + "','"
                    + c.getReceptor().getIdentificacion() + "','"
                    + c.getAprobadoDirector() + "','"
                    + c.getCuentaGeneral() + "','"
                    + c.getCuentaPresupuesto() + "','"
                    + c.getRechazado() + "','"
                    + c.getEmisor().getIdentificacion() + "','"
                    + c.getPDFAsociated() + "','"
                    + c.getPropietario() + "','"
                    + c.esCajaChica() + "')";
            System.out.println("data.CrudFacturaElectronica.addUpdateFacturaElectronica() sentencia " + "\n sentencia " + sentencia);
            AppLogger.appLogger.log(Level.WARNING, "sentencia " + sentencia);
            Connection conn = DbPoolHandler.connectionPool.getConnection();
            Statement statement = conn.createStatement();
            statement.execute(sentencia);
            res = true;
            DbPoolHandler.connectionPool.releaseConnection(conn);
            statement.close();
        } catch (Exception e) {
            DbPoolHandler.connectionPool.initializeConnections();
            System.err.println("data.CrudFacturaElectronica.addUpdateFacturaElectronica() error " + e.getMessage());
            AppLogger.appLogger.log(Level.SEVERE, "data.CrudFacturaElectronica.addUpdateFacturaElectronica() error " + e.getMessage());
        }
        return res;
    }

    public static boolean UpdateFacturaElectronicaAsientos(Receips c) {
        boolean res = false;
        try {
            //here sonoo is database name, root is username and password 
            String n = c.getCodigoActividad() == null ? "no aplica" : c.getCodigoActividad();
            String sentencia = "call UpdateFacturaElectronicaAsientos('"
                    + c.getNombreArchivo() + "','"
                    + c.getNumeroConsecutivo() + "','"
                    + c.getIdDepartamento() + "','"
                    + new java.sql.Date(c.getFechaEmision().getTime()) + "','"
                    + n + "','"
                    + c.getClave() + "','"
                    + c.getResumenFactura().getCodigoMoneda().getCodigoMoneda() + "','"
                    + c.getResumenFactura().getCodigoMoneda().getTipoCambio() + "','"
                    + c.getResumenFactura().getTotalServGravados() + "','"
                    + c.getResumenFactura().getTotalServExentos() + "','"
                    + c.getResumenFactura().getTotalServExonerado() + "','"
                    + c.getResumenFactura().getTotalMercanciasGravadas() + "','"
                    + c.getResumenFactura().getTotalMercanciasExentas() + "','"
                    + c.getResumenFactura().getTotalMercExonerada() + "','"
                    + c.getResumenFactura().getTotalGravado() + "','"
                    + c.getResumenFactura().getTotalExento() + "','"
                    + c.getResumenFactura().getTotalExonerado() + "','"
                    + c.getResumenFactura().getTotalVenta() + "','"
                    + c.getResumenFactura().getTotalDescuentos() + "','"
                    + c.getResumenFactura().getTotalVentaNeta() + "','"
                    + c.getResumenFactura().getTotalImpuesto() + "','"
                    + c.getResumenFactura().getTotalIVADevuelto() + "','"
                    + c.getResumenFactura().getTotalOtrosCargos() + "','"
                    + c.getResumenFactura().getTotalComprobante() + "','"
                    + c.getExactus() + "','"
                    + c.getEstado() + "','"
                    + c.getEmailIndex() + "','"
                    + c.getReceptor().getCorreoElectronico() + "','"
                    + c.getEmisor().getCorreoElectronico() + "','"
                    + c.getEmisor().getNombre() + "','"
                    + c.getReceptor().getNombre() + "','"
                    + DataUser.username + "','"
                    + c.getReceptor().getIdentificacion() + "','"
                    + c.getAprobadoDirector() + "','"
                    + c.getCuentaGeneral() + "','"
                    + c.getCuentaPresupuesto() + "','"
                    + c.getRechazado() + "','"
                    + c.getEmisor().getIdentificacion() + "','"
                    + c.getPDFAsociated() + "')";
            System.out.println("data.CrudFacturaElectronica.UpdateFacturaElectronicaAsientos() \n" + sentencia);
            AppLogger.appLogger.log(Level.WARNING, "sentencia " + sentencia);
            Connection conn = DbPoolHandler.connectionPool.getConnection();
            Statement statement = conn.createStatement();
            statement.execute(sentencia);
            res = true;
            DbPoolHandler.connectionPool.releaseConnection(conn);
            statement.close();
        } catch (Exception e) {
            AppLogger.appLogger.log(Level.SEVERE, "data.CrudFacturaElectronica.UpdateFacturaElectronicaAsientos() error " + e.getMessage());
            DbPoolHandler.connectionPool.initializeConnections();
            System.err.println("data.CrudFacturaElectronica.UpdateFacturaElectronicaAsientos() error " + e.getMessage());
        }
        return res;
    }

    public static boolean UpdateFacturaElectronicaPDF(Receips c) {
        boolean res = false;
        try {
            //here sonoo is database name, root is username and password 
            String n = c.getCodigoActividad() == null ? "no aplica" : c.getCodigoActividad();
            String sentencia = "call UpdateFacturaElectronicaPDF('"
                    + c.getNombreArchivo() + "','"
                    + c.getNumeroConsecutivo() + "','"
                    + c.getIdDepartamento() + "','"
                    + new java.sql.Date(c.getFechaEmision().getTime()) + "','"
                    + n + "','"
                    + c.getClave() + "','"
                    + c.getResumenFactura().getCodigoMoneda().getCodigoMoneda() + "','"
                    + c.getResumenFactura().getCodigoMoneda().getTipoCambio() + "','"
                    + c.getResumenFactura().getTotalServGravados() + "','"
                    + c.getResumenFactura().getTotalServExentos() + "','"
                    + c.getResumenFactura().getTotalServExonerado() + "','"
                    + c.getResumenFactura().getTotalMercanciasGravadas() + "','"
                    + c.getResumenFactura().getTotalMercanciasExentas() + "','"
                    + c.getResumenFactura().getTotalMercExonerada() + "','"
                    + c.getResumenFactura().getTotalGravado() + "','"
                    + c.getResumenFactura().getTotalExento() + "','"
                    + c.getResumenFactura().getTotalExonerado() + "','"
                    + c.getResumenFactura().getTotalVenta() + "','"
                    + c.getResumenFactura().getTotalDescuentos() + "','"
                    + c.getResumenFactura().getTotalVentaNeta() + "','"
                    + c.getResumenFactura().getTotalImpuesto() + "','"
                    + c.getResumenFactura().getTotalIVADevuelto() + "','"
                    + c.getResumenFactura().getTotalOtrosCargos() + "','"
                    + c.getResumenFactura().getTotalComprobante() + "','"
                    + c.getExactus() + "','"
                    + c.getEstado() + "','"
                    + c.getEmailIndex() + "','"
                    + c.getReceptor().getCorreoElectronico() + "','"
                    + c.getEmisor().getCorreoElectronico() + "','"
                    + c.getEmisor().getNombre() + "','"
                    + c.getReceptor().getNombre() + "','"
                    + DataUser.username + "','"
                    + c.getReceptor().getIdentificacion() + "','"
                    + c.getAprobadoDirector() + "','"
                    + c.getCuentaGeneral() + "','"
                    + c.getCuentaPresupuesto() + "','"
                    + c.getRechazado() + "','"
                    + c.getEmisor().getIdentificacion() + "','"
                    + c.getPDFAsociated() + "')";
            System.out.println("data.CrudFacturaElectronica.addUpdateFacturaElectronica() sentencia " + "\n sentencia " + sentencia);
            AppLogger.appLogger.info("sentencia " + sentencia);
            Connection conn = DbPoolHandler.connectionPool.getConnection();
            Statement statement = conn.createStatement();
            statement.execute(sentencia);
            res = true;
            DbPoolHandler.connectionPool.releaseConnection(conn);
            statement.close();
        } catch (Exception e) {
            AppLogger.appLogger.info("data.CrudFacturaElectronica.addUpdateFacturaElectronica() error " + e.getMessage());
            DbPoolHandler.connectionPool.initializeConnections();
            System.err.println("data.CrudFacturaElectronica.addUpdateFacturaElectronica() error " + e.getMessage());
        }
        return res;
    }

    public static boolean addFacturaElectronicaRepetida(Receips c) {
        boolean res = false;
        try {
            //here sonoo is database name, root is username and password 
            String n = c.getCodigoActividad() == null ? "no aplica" : c.getCodigoActividad();
            String sentencia = "call addFacturaElectronicaRepetida('"
                    + c.getNombreArchivo() + "','"
                    + c.getNumeroConsecutivo() + "','"
                    + c.getIdDepartamento() + "','"
                    + new java.sql.Date(c.getFechaEmision().getTime()) + "','"
                    + n + "','"
                    + c.getClave() + "','"
                    + c.getResumenFactura().getCodigoMoneda().getCodigoMoneda() + "','"
                    + c.getResumenFactura().getCodigoMoneda().getTipoCambio() + "','"
                    + c.getResumenFactura().getTotalServGravados() + "','"
                    + c.getResumenFactura().getTotalServExentos() + "','"
                    + c.getResumenFactura().getTotalServExonerado() + "','"
                    + c.getResumenFactura().getTotalMercanciasGravadas() + "','"
                    + c.getResumenFactura().getTotalMercanciasExentas() + "','"
                    + c.getResumenFactura().getTotalMercExonerada() + "','"
                    + c.getResumenFactura().getTotalGravado() + "','"
                    + c.getResumenFactura().getTotalExento() + "','"
                    + c.getResumenFactura().getTotalExonerado() + "','"
                    + c.getResumenFactura().getTotalVenta() + "','"
                    + c.getResumenFactura().getTotalDescuentos() + "','"
                    + c.getResumenFactura().getTotalVentaNeta() + "','"
                    + c.getResumenFactura().getTotalImpuesto() + "','"
                    + c.getResumenFactura().getTotalIVADevuelto() + "','"
                    + c.getResumenFactura().getTotalOtrosCargos() + "','"
                    + c.getResumenFactura().getTotalComprobante() + "','"
                    + c.getExactus() + "','"
                    + c.getEstado() + "','"
                    + c.getEmailIndex() + "','"
                    + c.getReceptor().getCorreoElectronico() + "','"
                    + c.getEmisor().getCorreoElectronico() + "','"
                    + c.getEmisor().getNombre() + "','"
                    + c.getReceptor().getNombre() + "','"
                    + DataUser.username + "','"
                    + c.getReceptor().getIdentificacion() + "','"
                    + c.getAprobadoDirector() + "','"
                    + c.getCuentaGeneral() + "','"
                    + c.getCuentaPresupuesto() + "','"
                    + c.getRechazado() + "','"
                    + c.getEmisor().getIdentificacion() + "')";
            AppLogger.appLogger.info("sentencia " + sentencia);
            System.out.println("data.CrudFacturaElectronica.addFacturaElectronicaRepetida() sentencia\n" + sentencia);
            Connection conn = DbPoolHandler.connectionPool.getConnection();
            Statement statement = conn.createStatement();
            statement.execute(sentencia);
            res = true;
            DbPoolHandler.connectionPool.releaseConnection(conn);
            statement.close();
        } catch (Exception e) {
            DbPoolHandler.connectionPool.initializeConnections();
            System.err.println("data.CrudFacturaElectronica.addUpdateFacturaElectronica() error " + e.getMessage());
            AppLogger.appLogger.info("data.CrudFacturaElectronica.addUpdateFacturaElectronica() error " + e.getMessage());

        }
        return res;
    }

    public static ArrayList<Receips> obtenerFacturasPorFecha(java.util.Date fechaInicio, java.util.Date fechaFin) {
        ArrayList<Receips> res = new ArrayList<>();
        java.sql.Date sqlInicio = new java.sql.Date(fechaInicio.getTime());
        java.sql.Date sqlFin = new java.sql.Date(fechaFin.getTime());
        try {
            String sentencia = "call obtenerFacturasPorFecha('" + sqlInicio.toString() + "','" + sqlFin + "')";
            AppLogger.appLogger.info("data.CrudFacturaElectronica.obtenerFacturasPorFecha() sentencia " + sentencia);
            Connection conn = DbPoolHandler.connectionPool.getConnection();
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sentencia);
            while (rs.next()) {
                Receips n = new Receips();
                n.setNombreArchivo(rs.getString("NombreArchivo"));
                n.setNumeroConsecutivo(rs.getString("NumeroConsecutivo"));
                n.setIdDepartamento(rs.getInt("IdDepartamento"));
                n.setFechaEmision(rs.getDate("FechaEmision"));
                n.setCodigoActividad(rs.getString("CodigoActividad"));
                n.setClave(rs.getString("Clave"));
                ResumenFactura rf = new ResumenFactura();
                CodigoMoneda cm = new CodigoMoneda();
                cm.setCodigoMoneda(rs.getString("CodigoMoneda"));
                cm.setTipoCambio(rs.getInt("TipoCambio"));
                rf.setCodigoMoneda(cm);
                rf.setTotalServGravados(rs.getDouble("TotalServGravados"));
                rf.setTotalServExentos(rs.getDouble("TotalServExentos"));
                rf.setTotalServExonerado(rs.getDouble("TotalServExonerado"));
                rf.setTotalMercanciasGravadas(rs.getDouble("TotalMercanciasGravadas"));
                rf.setTotalMercanciasExentas(rs.getDouble("TotalMercanciasExentas"));
                rf.setTotalMercExonerada(rs.getDouble("TotalMercExonerada"));
                rf.setTotalGravado(rs.getDouble("TotalGravado"));
                rf.setTotalExento(rs.getDouble("TotalExento"));
                rf.setTotalExonerado(rs.getDouble("TotalExonerado"));
                rf.setTotalVenta(rs.getDouble("TotalVenta"));
                rf.setTotalDescuentos(rs.getDouble("TotalDescuentos"));
                rf.setTotalVentaNeta(rs.getDouble("TotalVentaNeta"));
                rf.setTotalImpuesto(rs.getDouble("TotalImpuesto"));
                rf.setTotalIVADevuelto(rs.getDouble("TotalIVADevuelto"));
                rf.setTotalOtrosCargos(rs.getDouble("TotalOtrosCargos"));
                rf.setTotalComprobante(rs.getDouble("TotalComprobante"));
                n.setEstado(rs.getInt("Estado"));
                String Exactus = rs.getString("Exactus") == null ? "" : rs.getString("Exactus");
                n.setExactus(Exactus);
                n.setEmailIndex(rs.getInt("EmailIndex"));
                n.setConsecutivo(rs.getString("Clave"));
                n.setResumenFactura(rf);
                Receptor recep = new Receptor();
                recep.setCorreoElectronico(rs.getString("ReceptorCorreoElectronico"));
                recep.setNombre(rs.getString("ReceptorNombre"));
                recep.setIdentificacion(rs.getString("ReceptorCedulaJuridica"));
                n.setReceptor(recep);
                Emisor em = new Emisor();
                em.setNombre(rs.getString("EmisorNombre"));
                em.setCorreoElectronico(rs.getString("EmisorCorreoElectronico"));
                em.setIdentificacion(rs.getString("EmisorCedulaJuridica"));
                n.setCuentaGeneral(rs.getString("CuentaGeneral"));
                n.setCuentaPresupuesto(rs.getString("CuentaPresupuesto"));
                n.setAprobadoDirector(rs.getInt("AprobadoDirector"));
                n.setRechazado(rs.getInt("Rechazado"));
                n.setPDFAsociated(rs.getString("PDFAsociated"));
                n.setCajaChica(rs.getInt("CajaChica"));
                n.setPropietario(rs.getString("Propietario"));
                String cia = rs.getString("Cia");
                n.setCia(cia == null ? "" : cia);
                n.setEmisor(em);
                if (n.getEmisor().getIdentificacion().contains("3101724817")// rymsa
                        && n.getReceptor().getIdentificacion().contains("3101086411"))//cilt 
                {
                } else {
                    res.add(n);
                }
            }
            rs.close();
            DbPoolHandler.connectionPool.releaseConnection(conn);
            statement.close();
        } catch (Exception e) {
            DbPoolHandler.connectionPool.initializeConnections();
            AppLogger.appLogger.log(Level.SEVERE, "data.CrudFacturaElectronica.obtenerFacturasPorFecha() error " + e.getMessage());
            res = new ArrayList<>();
        }
        return res;
    }

    public static ArrayList<Receips> obtenerFacturaElectronicaView(String cedCia, String cedProv, int registradas, int asignadas, java.util.Date fechaInicio, java.util.Date fechaFin, boolean ciaDesconocidas) {
        ArrayList<Receips> res = new ArrayList<>();
        java.sql.Date sqlInicio = new java.sql.Date(fechaInicio.getTime());
        java.sql.Date sqlFin = new java.sql.Date(fechaFin.getTime());
        try {
            String sentencia = "SELECT *from sos.facturaelectronica_view "
                    + "v WHERE "
                    + "v.ReceptorCedulaJuridica LIKE '%" + cedCia + "%' "
                    + (ciaDesconocidas ? "AND v.ReceptorCedulaJuridica not LIKE '%3101724817%' "
                            + "AND v.ReceptorCedulaJuridica not LIKE '%3101086411%' "
                            + "AND v.ReceptorCedulaJuridica not LIKE '%3101119637%' "
                            + "AND v.ReceptorCedulaJuridica not LIKE '%3101119531%' "
                            + "AND v.ReceptorCedulaJuridica not LIKE '%3101466557%' "
                            + "AND v.ReceptorCedulaJuridica not LIKE '%3101468003%' " : "")
                    + "AND v.EmisorCedulaJuridica LIKE '%" + cedProv + "%' "
                    + (registradas == 0 ? "" : (registradas == 1 ? "AND v.exactus = '' " : "AND v.exactus != '' "))
                    + (asignadas == 0 ? "" : (asignadas == 1 ? "AND v.CuentaPresupuesto != '' " : "AND v.CuentaPresupuesto = '' "))
                    + "and "
                    + "(v.fechaemision BETWEEN '" + sqlInicio + "' "
                    + "AND '" + sqlFin + "')"
                    + "  ORDER BY v.FechaEmision ASC ,v.EmisorNombre asc;";
            AppLogger.appLogger.info("data.CrudFacturaElectronica.obtenerFacturaElectronicaView() sentencia\n" + sentencia);
            System.out.println("sentencia " + sentencia);
            Connection conn = DbPoolHandler.connectionPool.getConnection();
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sentencia);
            while (rs.next()) {
                Receips n = new Receips();
                n.setNombreArchivo(rs.getString("NombreArchivo"));
                n.setNumeroConsecutivo(rs.getString("NumeroConsecutivo"));
                n.setIdDepartamento(rs.getInt("IdDepartamento"));
                n.setFechaEmision(rs.getDate("FechaEmision"));
                n.setCodigoActividad(rs.getString("CodigoActividad"));
                n.setClave(rs.getString("Clave"));
                ResumenFactura rf = new ResumenFactura();
                CodigoMoneda cm = new CodigoMoneda();
                cm.setCodigoMoneda(rs.getString("CodigoMoneda"));
                cm.setTipoCambio(rs.getInt("TipoCambio"));
                rf.setCodigoMoneda(cm);
                rf.setTotalServGravados(rs.getDouble("TotalServGravados"));
                rf.setTotalServExentos(rs.getDouble("TotalServExentos"));
                rf.setTotalServExonerado(rs.getDouble("TotalServExonerado"));
                rf.setTotalMercanciasGravadas(rs.getDouble("TotalMercanciasGravadas"));
                rf.setTotalMercanciasExentas(rs.getDouble("TotalMercanciasExentas"));
                rf.setTotalMercExonerada(rs.getDouble("TotalMercExonerada"));
                rf.setTotalGravado(rs.getDouble("TotalGravado"));
                rf.setTotalExento(rs.getDouble("TotalExento"));
                rf.setTotalExonerado(rs.getDouble("TotalExonerado"));
                rf.setTotalVenta(rs.getDouble("TotalVenta"));
                rf.setTotalDescuentos(rs.getDouble("TotalDescuentos"));
                rf.setTotalVentaNeta(rs.getDouble("TotalVentaNeta"));
                rf.setTotalImpuesto(rs.getDouble("TotalImpuesto"));
                rf.setTotalIVADevuelto(rs.getDouble("TotalIVADevuelto"));
                rf.setTotalOtrosCargos(rs.getDouble("TotalOtrosCargos"));
                rf.setTotalComprobante(rs.getDouble("TotalComprobante"));
                n.setEstado(rs.getInt("Estado"));
                String Exactus = rs.getString("Exactus") == null ? "" : rs.getString("Exactus");
                n.setExactus(Exactus);
                n.setEmailIndex(rs.getInt("EmailIndex"));
                n.setConsecutivo(rs.getString("Clave"));
                n.setResumenFactura(rf);
                Receptor recep = new Receptor();
                recep.setCorreoElectronico(rs.getString("ReceptorCorreoElectronico"));
                recep.setNombre(rs.getString("ReceptorNombre"));
                recep.setIdentificacion(rs.getString("ReceptorCedulaJuridica"));
                n.setReceptor(recep);
                Emisor em = new Emisor();
                em.setNombre(rs.getString("EmisorNombre"));
                em.setCorreoElectronico(rs.getString("EmisorCorreoElectronico"));
                em.setIdentificacion(rs.getString("EmisorCedulaJuridica"));
                n.setCuentaGeneral(rs.getString("CuentaGeneral"));
                n.setCuentaPresupuesto(rs.getString("CuentaPresupuesto"));
                n.setAprobadoDirector(rs.getInt("AprobadoDirector"));
                n.setRechazado(rs.getInt("Rechazado"));
                n.setPDFAsociated(rs.getString("PDFAsociated"));
                n.setCajaChica(rs.getInt("CajaChica"));
                n.setPropietario(rs.getString("Propietario"));
                n.setEmisor(em);
                String cia = rs.getString("Cia");
                n.setCia(cia == null ? "" : cia);
                if (n.getEmisor().getIdentificacion().contains("3101724817")// rymsa
                        && n.getReceptor().getIdentificacion().contains("3101086411"))//cilt 
                {
                } else {
                    res.add(n);
                }
            }
            rs.close();
            DbPoolHandler.connectionPool.releaseConnection(conn);
            statement.close();
        } catch (Exception e) {
            DbPoolHandler.connectionPool.initializeConnections();
            System.err.println("data.CrudFacturaElectronica.obtenerFacturasPorFecha() error " + e.getMessage());
            AppLogger.appLogger.log(Level.SEVERE, "data.CrudFacturaElectronica.obtenerFacturaElectronicaView() error " + e.getMessage());
            res = new ArrayList<>();
        }
        return res;
    }

    public static ArrayList<Receips> obtenerFacturaElectronicaView(String cedCia, String cedProv, int registradas, int asignadas, java.util.Date fechaInicio, java.util.Date fechaFin, boolean ciaDesconocidas, String moneda) {
        ArrayList<Receips> res = new ArrayList<>();
        java.sql.Date sqlInicio = new java.sql.Date(fechaInicio.getTime());
        java.sql.Date sqlFin = new java.sql.Date(fechaFin.getTime());

        String monedaStat = moneda.equals("") ? ""
                : moneda.equals("CRC")
                ? "and v.codigomoneda not like '%USD%'"
                : "and v.codigomoneda like '%USD%'";
        try {
            String sentencia = "SELECT *from sos.facturaelectronica_view "
                    + "v WHERE "
                    + "v.ReceptorCedulaJuridica LIKE '%" + cedCia + "%' "
                    + (ciaDesconocidas ? "AND v.ReceptorCedulaJuridica not LIKE '%3101724817%' "
                            + "AND v.ReceptorCedulaJuridica not LIKE '%3101086411%' "
                            + "AND v.ReceptorCedulaJuridica not LIKE '%3101119637%' "
                            + "AND v.ReceptorCedulaJuridica not LIKE '%3101119531%' "
                            + "AND v.ReceptorCedulaJuridica not LIKE '%3101466557%' "
                            + "AND v.ReceptorCedulaJuridica not LIKE '%3101468003%' " : "")
                    + "AND v.EmisorCedulaJuridica LIKE '%" + cedProv + "%' "
                    + (registradas == 0 ? "" : (registradas == 1 ? "AND v.exactus = '' " : "AND v.exactus != '' "))
                    + (asignadas == 0 ? "" : (asignadas == 1 ? "AND v.CuentaPresupuesto != '' " : "AND v.CuentaPresupuesto = '' "))
                    + "and (v.fechaemision BETWEEN '" + sqlInicio + "' "
                    + "AND '" + sqlFin + "') "
                    + monedaStat
                    + "  ORDER BY v.FechaEmision ASC ,v.EmisorNombre asc;";
            AppLogger.appLogger.info("data.CrudFacturaElectronica.obtenerFacturaElectronicaView() sentencia\n" + sentencia);
            System.out.println("sentencia " + sentencia);
            Connection conn = DbPoolHandler.connectionPool.getConnection();
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sentencia);
            while (rs.next()) {
                Receips n = new Receips();
                n.setNombreArchivo(rs.getString("NombreArchivo"));
                n.setNumeroConsecutivo(rs.getString("NumeroConsecutivo"));
                n.setIdDepartamento(rs.getInt("IdDepartamento"));
                n.setFechaEmision(rs.getDate("FechaEmision"));
                n.setCodigoActividad(rs.getString("CodigoActividad"));
                n.setClave(rs.getString("Clave"));
                ResumenFactura rf = new ResumenFactura();
                CodigoMoneda cm = new CodigoMoneda();
                cm.setCodigoMoneda(rs.getString("CodigoMoneda"));
                cm.setTipoCambio(rs.getInt("TipoCambio"));
                rf.setCodigoMoneda(cm);
                rf.setTotalServGravados(rs.getDouble("TotalServGravados"));
                rf.setTotalServExentos(rs.getDouble("TotalServExentos"));
                rf.setTotalServExonerado(rs.getDouble("TotalServExonerado"));
                rf.setTotalMercanciasGravadas(rs.getDouble("TotalMercanciasGravadas"));
                rf.setTotalMercanciasExentas(rs.getDouble("TotalMercanciasExentas"));
                rf.setTotalMercExonerada(rs.getDouble("TotalMercExonerada"));
                rf.setTotalGravado(rs.getDouble("TotalGravado"));
                rf.setTotalExento(rs.getDouble("TotalExento"));
                rf.setTotalExonerado(rs.getDouble("TotalExonerado"));
                rf.setTotalVenta(rs.getDouble("TotalVenta"));
                rf.setTotalDescuentos(rs.getDouble("TotalDescuentos"));
                rf.setTotalVentaNeta(rs.getDouble("TotalVentaNeta"));
                rf.setTotalImpuesto(rs.getDouble("TotalImpuesto"));
                rf.setTotalIVADevuelto(rs.getDouble("TotalIVADevuelto"));
                rf.setTotalOtrosCargos(rs.getDouble("TotalOtrosCargos"));
                rf.setTotalComprobante(rs.getDouble("TotalComprobante"));
                n.setEstado(rs.getInt("Estado"));
                String Exactus = rs.getString("Exactus") == null ? "" : rs.getString("Exactus");
                n.setExactus(Exactus);
                n.setEmailIndex(rs.getInt("EmailIndex"));
                n.setConsecutivo(rs.getString("Clave"));
                n.setResumenFactura(rf);
                Receptor recep = new Receptor();
                recep.setCorreoElectronico(rs.getString("ReceptorCorreoElectronico"));
                recep.setNombre(rs.getString("ReceptorNombre"));
                recep.setIdentificacion(rs.getString("ReceptorCedulaJuridica"));
                n.setReceptor(recep);
                Emisor em = new Emisor();
                em.setNombre(rs.getString("EmisorNombre"));
                em.setCorreoElectronico(rs.getString("EmisorCorreoElectronico"));
                em.setIdentificacion(rs.getString("EmisorCedulaJuridica"));
                n.setCuentaGeneral(rs.getString("CuentaGeneral"));
                n.setCuentaPresupuesto(rs.getString("CuentaPresupuesto"));
                n.setAprobadoDirector(rs.getInt("AprobadoDirector"));
                n.setRechazado(rs.getInt("Rechazado"));
                n.setPDFAsociated(rs.getString("PDFAsociated"));
                n.setCajaChica(rs.getInt("CajaChica"));
                n.setPropietario(rs.getString("Propietario"));
                n.setEmisor(em);
                String cia = rs.getString("Cia");
                n.setCia(cia == null ? "" : cia);
                if (n.getEmisor().getIdentificacion().contains("3101724817")// rymsa
                        && n.getReceptor().getIdentificacion().contains("3101086411"))//cilt 
                {
                } else {
                    res.add(n);
                }
            }
            rs.close();
            DbPoolHandler.connectionPool.releaseConnection(conn);
            statement.close();
        } catch (Exception e) {
            DbPoolHandler.connectionPool.initializeConnections();
            System.err.println("data.CrudFacturaElectronica.obtenerFacturasPorFecha() error " + e.getMessage());
            AppLogger.appLogger.log(Level.SEVERE, "data.CrudFacturaElectronica.obtenerFacturaElectronicaView() error " + e.getMessage());
            res = new ArrayList<>();
        }
        return res;
    }

    public static ArrayList<Receips> obtenerFacturaElectronicaView(String cedCia, String cedProv, java.util.Date fechaInicio, java.util.Date fechaFin, boolean ciaDesconocidas, int iddepartamento, String cta) {
        ArrayList<Receips> res = new ArrayList<>();
        java.sql.Date sqlInicio = new java.sql.Date(fechaInicio.getTime());
        java.sql.Date sqlFin = new java.sql.Date(fechaFin.getTime());

        try {
            String sentencia = "SELECT *from sos.facturaelectronica_view "
                    + "v WHERE "
                    + "v.ReceptorCedulaJuridica LIKE '%" + cedCia + "%' "
                    + (ciaDesconocidas ? "AND v.ReceptorCedulaJuridica not LIKE '%3101724817%' "
                            + "AND v.ReceptorCedulaJuridica not LIKE '%3101086411%' "
                            + "AND v.ReceptorCedulaJuridica not LIKE '%3101119637%' "
                            + "AND v.ReceptorCedulaJuridica not LIKE '%3101119531%' "
                            + "AND v.ReceptorCedulaJuridica not LIKE '%3101466557%' "
                            + "AND v.ReceptorCedulaJuridica not LIKE '%3101468003%' " : "")
                    + "AND v.EmisorCedulaJuridica LIKE '%" + cedProv + "%' "
                    + (cta == ""
                            ? "AND v.CuentaPresupuesto != '' "
                            : " AND v.CuentaPresupuesto LIKE '%" + cta + "%' ")
                    + (iddepartamento == -1 ? "" : "AND v.iddepartamento = " + iddepartamento)
                    + " and (v.fechaemision BETWEEN '" + sqlInicio + "' "
                    + " AND '" + sqlFin + "') "
                    + "  ORDER BY v.FechaEmision ASC ,v.EmisorNombre asc;";
            AppLogger.appLogger.info("data.CrudFacturaElectronica.obtenerFacturaElectronicaView() sentencia\n" + sentencia);
            System.out.println("sentencia " + sentencia);
            Connection conn = DbPoolHandler.connectionPool.getConnection();
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sentencia);
            while (rs.next()) {
                Receips n = new Receips();
                n.setNombreArchivo(rs.getString("NombreArchivo"));
                n.setNumeroConsecutivo(rs.getString("NumeroConsecutivo"));
                n.setIdDepartamento(rs.getInt("IdDepartamento"));
                n.setFechaEmision(rs.getDate("FechaEmision"));
                n.setCodigoActividad(rs.getString("CodigoActividad"));
                n.setClave(rs.getString("Clave"));
                ResumenFactura rf = new ResumenFactura();
                CodigoMoneda cm = new CodigoMoneda();
                cm.setCodigoMoneda(rs.getString("CodigoMoneda"));
                cm.setTipoCambio(rs.getInt("TipoCambio"));
                rf.setCodigoMoneda(cm);
                rf.setTotalServGravados(rs.getDouble("TotalServGravados"));
                rf.setTotalServExentos(rs.getDouble("TotalServExentos"));
                rf.setTotalServExonerado(rs.getDouble("TotalServExonerado"));
                rf.setTotalMercanciasGravadas(rs.getDouble("TotalMercanciasGravadas"));
                rf.setTotalMercanciasExentas(rs.getDouble("TotalMercanciasExentas"));
                rf.setTotalMercExonerada(rs.getDouble("TotalMercExonerada"));
                rf.setTotalGravado(rs.getDouble("TotalGravado"));
                rf.setTotalExento(rs.getDouble("TotalExento"));
                rf.setTotalExonerado(rs.getDouble("TotalExonerado"));
                rf.setTotalVenta(rs.getDouble("TotalVenta"));
                rf.setTotalDescuentos(rs.getDouble("TotalDescuentos"));
                rf.setTotalVentaNeta(rs.getDouble("TotalVentaNeta"));
                rf.setTotalImpuesto(rs.getDouble("TotalImpuesto"));
                rf.setTotalIVADevuelto(rs.getDouble("TotalIVADevuelto"));
                rf.setTotalOtrosCargos(rs.getDouble("TotalOtrosCargos"));
                rf.setTotalComprobante(rs.getDouble("TotalComprobante"));
                n.setEstado(rs.getInt("Estado"));
                String Exactus = rs.getString("Exactus") == null ? "" : rs.getString("Exactus");
                n.setExactus(Exactus);
                n.setEmailIndex(rs.getInt("EmailIndex"));
                n.setConsecutivo(rs.getString("Clave"));
                n.setResumenFactura(rf);
                Receptor recep = new Receptor();
                recep.setCorreoElectronico(rs.getString("ReceptorCorreoElectronico"));
                recep.setNombre(rs.getString("ReceptorNombre"));
                recep.setIdentificacion(rs.getString("ReceptorCedulaJuridica"));
                n.setReceptor(recep);
                Emisor em = new Emisor();
                em.setNombre(rs.getString("EmisorNombre"));
                em.setCorreoElectronico(rs.getString("EmisorCorreoElectronico"));
                em.setIdentificacion(rs.getString("EmisorCedulaJuridica"));
                n.setCuentaGeneral(rs.getString("CuentaGeneral"));
                n.setCuentaPresupuesto(rs.getString("CuentaPresupuesto"));
                n.setAprobadoDirector(rs.getInt("AprobadoDirector"));
                n.setRechazado(rs.getInt("Rechazado"));
                n.setPDFAsociated(rs.getString("PDFAsociated"));
                n.setCajaChica(rs.getInt("CajaChica"));
                n.setPropietario(rs.getString("Propietario"));
                n.setEmisor(em);
                String cia = rs.getString("Cia");
                n.setCia(cia == null ? "" : cia);
                if (n.getEmisor().getIdentificacion().contains("3101724817")// rymsa
                        && n.getReceptor().getIdentificacion().contains("3101086411"))//cilt 
                {
                } else {
                    res.add(n);
                }
            }
            rs.close();
            DbPoolHandler.connectionPool.releaseConnection(conn);
            statement.close();
        } catch (Exception e) {
            DbPoolHandler.connectionPool.initializeConnections();
            System.err.println("data.CrudFacturaElectronica.obtenerFacturasPorFecha() error " + e.getMessage());
            AppLogger.appLogger.log(Level.SEVERE, "data.CrudFacturaElectronica.obtenerFacturaElectronicaView() error " + e.getMessage());
            res = new ArrayList<>();
        }
        return res;
    }

    public Receips obtenerFacturaElectronicaByClave(String clave) {
        Receips n = null;
        try {
            String sentencia = "SELECT *from sos.facturaelectronica_view v WHERE "
                    + "v.clave = '" + clave + "';";
            AppLogger.appLogger.info("data.CrudFacturaElectronica.obtenerFacturaElectronicaByClave() sentencia\n" + sentencia);
            Connection conn = DbPoolHandler.connectionPool.getConnection();
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sentencia);
            while (rs.next()) {
                n = new Receips();
                n.setNombreArchivo(rs.getString("NombreArchivo"));
                n.setNumeroConsecutivo(rs.getString("NumeroConsecutivo"));
                n.setIdDepartamento(rs.getInt("IdDepartamento"));
                n.setFechaEmision(rs.getDate("FechaEmision"));
                n.setCodigoActividad(rs.getString("CodigoActividad"));
                n.setClave(rs.getString("Clave"));
                ResumenFactura rf = new ResumenFactura();
                CodigoMoneda cm = new CodigoMoneda();
                cm.setCodigoMoneda(rs.getString("CodigoMoneda"));
                cm.setTipoCambio(rs.getInt("TipoCambio"));
                rf.setCodigoMoneda(cm);
                rf.setTotalServGravados(rs.getDouble("TotalServGravados"));
                rf.setTotalServExentos(rs.getDouble("TotalServExentos"));
                rf.setTotalServExonerado(rs.getDouble("TotalServExonerado"));
                rf.setTotalMercanciasGravadas(rs.getDouble("TotalMercanciasGravadas"));
                rf.setTotalMercanciasExentas(rs.getDouble("TotalMercanciasExentas"));
                rf.setTotalMercExonerada(rs.getDouble("TotalMercExonerada"));
                rf.setTotalGravado(rs.getDouble("TotalGravado"));
                rf.setTotalExento(rs.getDouble("TotalExento"));
                rf.setTotalExonerado(rs.getDouble("TotalExonerado"));
                rf.setTotalVenta(rs.getDouble("TotalVenta"));
                rf.setTotalDescuentos(rs.getDouble("TotalDescuentos"));
                rf.setTotalVentaNeta(rs.getDouble("TotalVentaNeta"));
                rf.setTotalImpuesto(rs.getDouble("TotalImpuesto"));
                rf.setTotalIVADevuelto(rs.getDouble("TotalIVADevuelto"));
                rf.setTotalOtrosCargos(rs.getDouble("TotalOtrosCargos"));
                rf.setTotalComprobante(rs.getDouble("TotalComprobante"));
                n.setEstado(rs.getInt("Estado"));
                String Exactus = rs.getString("Exactus") == null ? "" : rs.getString("Exactus");
                n.setExactus(Exactus);
                n.setEmailIndex(rs.getInt("EmailIndex"));
                n.setConsecutivo(rs.getString("Clave"));
                n.setResumenFactura(rf);
                Receptor recep = new Receptor();
                recep.setCorreoElectronico(rs.getString("ReceptorCorreoElectronico"));
                recep.setNombre(rs.getString("ReceptorNombre"));
                recep.setIdentificacion(rs.getString("ReceptorCedulaJuridica"));
                n.setReceptor(recep);
                Emisor em = new Emisor();
                em.setNombre(rs.getString("EmisorNombre"));
                em.setCorreoElectronico(rs.getString("EmisorCorreoElectronico"));
                em.setIdentificacion(rs.getString("EmisorCedulaJuridica"));
                n.setCuentaGeneral(rs.getString("CuentaGeneral"));
                n.setCuentaPresupuesto(rs.getString("CuentaPresupuesto"));
                n.setAprobadoDirector(rs.getInt("AprobadoDirector"));
                n.setRechazado(rs.getInt("Rechazado"));
                n.setPDFAsociated(rs.getString("PDFAsociated"));
                n.setCajaChica(rs.getInt("CajaChica"));
                n.setPropietario(rs.getString("Propietario"));
                n.setEmisor(em);
                String cia = rs.getString("Cia");
                n.setCia(cia == null ? "" : cia);

            }
            rs.close();
            DbPoolHandler.connectionPool.releaseConnection(conn);
            statement.close();
        } catch (Exception e) {
            DbPoolHandler.connectionPool.initializeConnections();
            System.err.println("data.CrudFacturaElectronica.obtenerFacturaElectronicaByClave() error " + e.getMessage());
            AppLogger.appLogger.log(Level.SEVERE, "data.CrudFacturaElectronica.obtenerFacturaElectronicaByClave() error " + e.getMessage());
            n = null;
        }
        return n;
    }

    public boolean updateFacturaElectronica(Receips r) {
        boolean res = false;
        try {
            java.util.Date fecha = new java.util.Date();
            java.sql.Date sqlMod = new java.sql.Date(fecha.getTime());
            String sentencia = "update sos.FacturaElectronica "
                    + "set cuentaGeneral = '" + r.getCuentaGeneral() + "'"
                    + ", cuentaPresupuesto = '" + r.getCuentaPresupuesto() + "'"
                    + ", iddepartamento = '" + r.getIdDepartamento() + "'"
                    + ", ultimaModificacion = '" + sqlMod + "'"
                    + " where clave = '" + r.getClave() + "';";
            System.out.println("data.CrudFacturaElectronica.UpdateFacturaElectronicaAsientos() \n" + sentencia);
            AppLogger.appLogger.log(Level.WARNING, "sentencia " + sentencia);
            Connection conn = DbPoolHandler.connectionPool.getConnection();
            Statement statement = conn.createStatement();
            statement.execute(sentencia);
            res = true;
            DbPoolHandler.connectionPool.releaseConnection(conn);
            statement.close();
        } catch (Exception e) {
            AppLogger.appLogger.log(Level.SEVERE, "data.CrudFacturaElectronica.UpdateFacturaElectronicaAsientos() error " + e.getMessage());
            DbPoolHandler.connectionPool.initializeConnections();
            System.err.println("data.CrudFacturaElectronica.UpdateFacturaElectronicaAsientos() error " + e.getMessage());
        }
        return res;
    }

    public static int getEmailMaxIndex() {
        int res = -1;
        try {
            String sentencia = "select sos.getEmailMaxIndex()";
            System.out.println("sentencia " + sentencia);
            Connection conn = DbPoolHandler.connectionPool.getConnection();
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sentencia);
            while (rs.next()) {
                res = rs.getInt(1);
            }
            rs.close();
            DbPoolHandler.connectionPool.releaseConnection(conn);
            statement.close();
        } catch (Exception e) {
            DbPoolHandler.connectionPool.initializeConnections();
            System.out.println("data.CrudFacturaElectronica.getEmailMaxIndex() error " + e.getMessage());
            res = -1;
        }
        return res;
    }
}
