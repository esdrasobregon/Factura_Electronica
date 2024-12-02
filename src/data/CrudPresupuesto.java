/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package data;

import entitys.Presupuesto;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.sql.Statement;
import logic.AppLogger;

/**
 *
 * @author programador1
 */
public class CrudPresupuesto {

    public CrudPresupuesto() {
        System.out.println("data.CrudPresupuesto.<init>()");
        sqlPoolInstance.initPool();
    }

    public ArrayList<Presupuesto> obtenerPresupuestoPorPeriodo(String periodo) {
        ArrayList<Presupuesto> listaPresupuesto = new ArrayList<>();
        try {
            Connection connection = sqlPoolInstance.pool.getConnection();
            Statement sta = connection.createStatement();
            String Sql = "exec obtenerPresupuestoPorPeriodo '" + periodo + "';";
            AppLogger.appLogger.info(Sql);
            ResultSet rs = sta.executeQuery(Sql);
            while (rs.next()) {
                Presupuesto p = new Presupuesto();
                p.setCIA(rs.getString("CIA").trim());
                p.setPERIODO(rs.getString("PERIODO").trim());
                p.setCODDEPA(rs.getString("CODDEPA").trim());
                p.setDEPTADETALLE(rs.getString("DEPTADETALLE").trim());
                p.setCODAREA(rs.getString("CODAREA").trim());
                p.setAREADETALLE(rs.getString("AREADETALLE").trim());
                p.setCODCONCEPTO(rs.getString("CODCONCEPTO").trim());
                p.setCONCEPTOADETALLE(rs.getString("CONCEPTOADETALLE").trim());
                p.setCTAPRESUPUESTO(rs.getString("CTAPRESUPUESTO").trim());
                p.setCTAREGULATORIA(rs.getString("CTAREGULATORIA"));
                p.setDETALLE_REGULA(rs.getString("DETALLE_REGULA"));
                BigDecimal enero = rs.getBigDecimal("ENERO");
                BigDecimal febrero = rs.getBigDecimal("FEBRERO");
                BigDecimal marzo = rs.getBigDecimal("MARZO");
                BigDecimal abril = rs.getBigDecimal("ABRIL");
                BigDecimal mayo = rs.getBigDecimal("MAYO");
                BigDecimal junio = rs.getBigDecimal("JUNIO");
                BigDecimal julio = rs.getBigDecimal("JULIO");
                BigDecimal agosto = rs.getBigDecimal("AGOSTO");
                BigDecimal septiembre = rs.getBigDecimal("SEPTIEMBRE");
                BigDecimal octubre = rs.getBigDecimal("OCTUBRE");
                BigDecimal noviembre = rs.getBigDecimal("NOVIEMBRE");
                BigDecimal diciembre = rs.getBigDecimal("DICIEMBRE");
                p.setENERO(enero != null ? enero : new BigDecimal(0));
                p.setENERO(febrero != null ? enero : new BigDecimal(0));
                p.setFEBRERO(marzo != null ? enero : new BigDecimal(0));
                p.setMARZO(abril != null ? enero : new BigDecimal(0));
                p.setABRIL(mayo != null ? enero : new BigDecimal(0));
                p.setMAYO(junio != null ? enero : new BigDecimal(0));
                p.setJUNIO(julio != null ? enero : new BigDecimal(0));
                p.setJULIO(agosto != null ? enero : new BigDecimal(0));
                p.setAGOSTO(septiembre != null ? enero : new BigDecimal(0));
                p.setSEPTIEMBRE(octubre != null ? enero : new BigDecimal(0));
                p.setOCTUBRE(enero != null ? enero : new BigDecimal(0));
                p.setNOVIEMBRE(noviembre != null ? enero : new BigDecimal(0));
                p.setDICIEMBRE(diciembre != null ? enero : new BigDecimal(0));
                listaPresupuesto.add(p);
            }
            rs.close();
            sqlPoolInstance.pool.releaseConnection(connection);

        } catch (Exception e) {
            logic.AppLogger.appLogger.info("data.CrudPresupuesto.obtenerPresupuestoPorPeriodo() error " + e.getMessage());
            System.out.println("data.CrudPresupuesto.obtenerPresupuestoPorPeriodo() error " + e.getMessage());
        }
        return listaPresupuesto;
    }

    public ArrayList<Presupuesto> obtenerPresupuestoPorDep(String dep) {
        ArrayList<Presupuesto> listaPresupuesto = new ArrayList<>();
        try {
            Connection connection = sqlPoolInstance.pool.getConnection();
            Statement sta = connection.createStatement();
            String Sql = "select * from [INDICADORES].[dbo].[vista_Presupuesto] "
                    + "where coddepa ='" + dep + "'"
                    + " order by CTAPRESUPUESTO ASC;";
            AppLogger.appLogger.info("data.CrudPresupuesto.obtenerPresupuestoPorDep() sentencia \n" + Sql);
            ResultSet rs = sta.executeQuery(Sql);
            while (rs.next()) {
                Presupuesto p = new Presupuesto();
                p.setCIA(rs.getString("CIA").trim());
                p.setPERIODO(rs.getString("PERIODO").trim());
                p.setCODDEPA(rs.getString("CODDEPA").trim());
                p.setDEPTADETALLE(rs.getString("DEPTADETALLE").trim());
                p.setCODAREA(rs.getString("CODAREA").trim());
                p.setAREADETALLE(rs.getString("AREADETALLE").trim());
                p.setCODCONCEPTO(rs.getString("CODCONCEPTO").trim());
                p.setCONCEPTOADETALLE(rs.getString("CONCEPTOADETALLE").trim());
                p.setCTAPRESUPUESTO(rs.getString("CTAPRESUPUESTO").trim());
                p.setCTAREGULATORIA(rs.getString("CTAREGULATORIA"));
                p.setDETALLE_REGULA(rs.getString("DETALLE_REGULA"));
                BigDecimal enero = rs.getBigDecimal("ENERO");
                BigDecimal febrero = rs.getBigDecimal("FEBRERO");
                BigDecimal marzo = rs.getBigDecimal("MARZO");
                BigDecimal abril = rs.getBigDecimal("ABRIL");
                BigDecimal mayo = rs.getBigDecimal("MAYO");
                BigDecimal junio = rs.getBigDecimal("JUNIO");
                BigDecimal julio = rs.getBigDecimal("JULIO");
                BigDecimal agosto = rs.getBigDecimal("AGOSTO");
                BigDecimal septiembre = rs.getBigDecimal("SEPTIEMBRE");
                BigDecimal octubre = rs.getBigDecimal("OCTUBRE");
                BigDecimal noviembre = rs.getBigDecimal("NOVIEMBRE");
                BigDecimal diciembre = rs.getBigDecimal("DICIEMBRE");
                p.setENERO(enero != null ? enero : new BigDecimal(0));
                p.setENERO(febrero != null ? enero : new BigDecimal(0));
                p.setFEBRERO(marzo != null ? enero : new BigDecimal(0));
                p.setMARZO(abril != null ? enero : new BigDecimal(0));
                p.setABRIL(mayo != null ? enero : new BigDecimal(0));
                p.setMAYO(junio != null ? enero : new BigDecimal(0));
                p.setJUNIO(julio != null ? enero : new BigDecimal(0));
                p.setJULIO(agosto != null ? enero : new BigDecimal(0));
                p.setAGOSTO(septiembre != null ? enero : new BigDecimal(0));
                p.setSEPTIEMBRE(octubre != null ? enero : new BigDecimal(0));
                p.setOCTUBRE(enero != null ? enero : new BigDecimal(0));
                p.setNOVIEMBRE(noviembre != null ? enero : new BigDecimal(0));
                p.setDICIEMBRE(diciembre != null ? enero : new BigDecimal(0));
                listaPresupuesto.add(p);
            }
            rs.close();
            sqlPoolInstance.pool.releaseConnection(connection);

        } catch (Exception e) {
            logic.AppLogger.appLogger.info("data.CrudPresupuesto.obtenerPresupuestoPorPeriodo() error " + e.getMessage());
            System.out.println("data.CrudPresupuesto.obtenerPresupuestoPorPeriodo() error " + e.getMessage());
        }
        return listaPresupuesto;
    }

    public Presupuesto obtenerPresupuestoPorCta(String cta) {
        Presupuesto p = null;
        try {
            Connection connection = sqlPoolInstance.pool.getConnection();
            Statement sta = connection.createStatement();
            String Sql = "select * from [INDICADORES].[dbo].[vista_Presupuesto] "
                    + "where ctapresupuesto ='" + cta + "';";
            AppLogger.appLogger.info("data.CrudPresupuesto.obtenerPresupuestoPorCta() sentencia\n" + Sql);
            ResultSet rs = sta.executeQuery(Sql);
            while (rs.next()) {
                p = new Presupuesto();
                p.setCIA(rs.getString("CIA").trim());
                p.setPERIODO(rs.getString("PERIODO").trim());
                p.setCODDEPA(rs.getString("CODDEPA").trim());
                p.setDEPTADETALLE(rs.getString("DEPTADETALLE").trim());
                p.setCODAREA(rs.getString("CODAREA").trim());
                p.setAREADETALLE(rs.getString("AREADETALLE").trim());
                p.setCODCONCEPTO(rs.getString("CODCONCEPTO").trim());
                p.setCONCEPTOADETALLE(rs.getString("CONCEPTOADETALLE").trim());
                p.setCTAPRESUPUESTO(rs.getString("CTAPRESUPUESTO").trim());
                p.setCTAREGULATORIA(rs.getString("CTAREGULATORIA"));
                p.setDETALLE_REGULA(rs.getString("DETALLE_REGULA"));
                BigDecimal enero = rs.getBigDecimal("ENERO");
                BigDecimal febrero = rs.getBigDecimal("FEBRERO");
                BigDecimal marzo = rs.getBigDecimal("MARZO");
                BigDecimal abril = rs.getBigDecimal("ABRIL");
                BigDecimal mayo = rs.getBigDecimal("MAYO");
                BigDecimal junio = rs.getBigDecimal("JUNIO");
                BigDecimal julio = rs.getBigDecimal("JULIO");
                BigDecimal agosto = rs.getBigDecimal("AGOSTO");
                BigDecimal septiembre = rs.getBigDecimal("SEPTIEMBRE");
                BigDecimal octubre = rs.getBigDecimal("OCTUBRE");
                BigDecimal noviembre = rs.getBigDecimal("NOVIEMBRE");
                BigDecimal diciembre = rs.getBigDecimal("DICIEMBRE");
                p.setENERO(enero != null ? enero : new BigDecimal(0));
                p.setENERO(febrero != null ? enero : new BigDecimal(0));
                p.setFEBRERO(marzo != null ? enero : new BigDecimal(0));
                p.setMARZO(abril != null ? enero : new BigDecimal(0));
                p.setABRIL(mayo != null ? enero : new BigDecimal(0));
                p.setMAYO(junio != null ? enero : new BigDecimal(0));
                p.setJUNIO(julio != null ? enero : new BigDecimal(0));
                p.setJULIO(agosto != null ? enero : new BigDecimal(0));
                p.setAGOSTO(septiembre != null ? enero : new BigDecimal(0));
                p.setSEPTIEMBRE(octubre != null ? enero : new BigDecimal(0));
                p.setOCTUBRE(enero != null ? enero : new BigDecimal(0));
                p.setNOVIEMBRE(noviembre != null ? enero : new BigDecimal(0));
                p.setDICIEMBRE(diciembre != null ? enero : new BigDecimal(0));
            }
            rs.close();
            sqlPoolInstance.pool.releaseConnection(connection);

        } catch (Exception e) {
            logic.AppLogger.appLogger.info("data.CrudPresupuesto.obtenerPresupuestoPorCta() error " + e.getMessage());
        }
        return p;
    }

    public Presupuesto obtenerPresupuestoPorCtaNoCia(String cta) {
        Presupuesto p = null;
        try {
            Connection connection = sqlPoolInstance.pool.getConnection();
            Statement sta = connection.createStatement();
            String Sql = "select * from [INDICADORES].[dbo].[vista_Presupuesto] "
                    + "where ctapresupuesto like '%-" + cta + "%';";
            AppLogger.appLogger.info("data.CrudPresupuesto.obtenerPresupuestoPorCta() sentencia\n" + Sql);
            ResultSet rs = sta.executeQuery(Sql);
            while (rs.next()) {
                p = new Presupuesto();
                p.setCIA(rs.getString("CIA").trim());
                p.setPERIODO(rs.getString("PERIODO").trim());
                p.setCODDEPA(rs.getString("CODDEPA").trim());
                p.setDEPTADETALLE(rs.getString("DEPTADETALLE").trim());
                p.setCODAREA(rs.getString("CODAREA").trim());
                p.setAREADETALLE(rs.getString("AREADETALLE").trim());
                p.setCODCONCEPTO(rs.getString("CODCONCEPTO").trim());
                p.setCONCEPTOADETALLE(rs.getString("CONCEPTOADETALLE").trim());
                p.setCTAPRESUPUESTO(rs.getString("CTAPRESUPUESTO").trim());
                p.setCTAREGULATORIA(rs.getString("CTAREGULATORIA"));
                p.setDETALLE_REGULA(rs.getString("DETALLE_REGULA"));
                BigDecimal enero = rs.getBigDecimal("ENERO");
                BigDecimal febrero = rs.getBigDecimal("FEBRERO");
                BigDecimal marzo = rs.getBigDecimal("MARZO");
                BigDecimal abril = rs.getBigDecimal("ABRIL");
                BigDecimal mayo = rs.getBigDecimal("MAYO");
                BigDecimal junio = rs.getBigDecimal("JUNIO");
                BigDecimal julio = rs.getBigDecimal("JULIO");
                BigDecimal agosto = rs.getBigDecimal("AGOSTO");
                BigDecimal septiembre = rs.getBigDecimal("SEPTIEMBRE");
                BigDecimal octubre = rs.getBigDecimal("OCTUBRE");
                BigDecimal noviembre = rs.getBigDecimal("NOVIEMBRE");
                BigDecimal diciembre = rs.getBigDecimal("DICIEMBRE");
                p.setENERO(enero != null ? enero : new BigDecimal(0));
                p.setENERO(febrero != null ? enero : new BigDecimal(0));
                p.setFEBRERO(marzo != null ? enero : new BigDecimal(0));
                p.setMARZO(abril != null ? enero : new BigDecimal(0));
                p.setABRIL(mayo != null ? enero : new BigDecimal(0));
                p.setMAYO(junio != null ? enero : new BigDecimal(0));
                p.setJUNIO(julio != null ? enero : new BigDecimal(0));
                p.setJULIO(agosto != null ? enero : new BigDecimal(0));
                p.setAGOSTO(septiembre != null ? enero : new BigDecimal(0));
                p.setSEPTIEMBRE(octubre != null ? enero : new BigDecimal(0));
                p.setOCTUBRE(enero != null ? enero : new BigDecimal(0));
                p.setNOVIEMBRE(noviembre != null ? enero : new BigDecimal(0));
                p.setDICIEMBRE(diciembre != null ? enero : new BigDecimal(0));
            }
            rs.close();
            sqlPoolInstance.pool.releaseConnection(connection);

        } catch (Exception e) {
            logic.AppLogger.appLogger.info("data.CrudPresupuesto.obtenerPresupuestoPorCta() error " + e.getMessage());
        }
        return p;
    }

    public boolean agregarPresupuesto(Presupuesto press) {
        boolean res = false;
        try {
            Connection connection = sqlPoolInstance.pool.getConnection();
            Statement sta = connection.createStatement();
            String Sql = "INSERT INTO INDICADORES.dbo.PRESUPUESTO"
                    + "([CIA]"
                    + ",[PERIODO]"
                    + ",[CODDEPA]"
                    + ",[DEPTADETALLE]"
                    + ",[CODAREA]"
                    + ",[AREADETALLE]"
                    + ",[CODCONCEPTO]"
                    + ",[CONCEPTOADETALLE]"
                    + ",[CTAPRESUPUESTO])"
                    + "VALUES"
                    + "('" + press.getCIA() + "'"
                    + ",'" + press.getPERIODO() + "'"
                    + ",'" + press.getCODDEPA() + "'"
                    + ",'" + press.getDEPTADETALLE() + "'"
                    + ",'" + press.getCODAREA() + "'"
                    + ",'" + press.getAREADETALLE() + "'"
                    + ",'" + press.getCODCONCEPTO() + "'"
                    + ",'" + press.getCONCEPATOADETALLE() + "'"
                    + ",'" + press.getCTAPRESUPUESTO() + "');";
            AppLogger.appLogger.info("data.CrudPresupuesto.agregarPresupuesto() sentenpcia +\n" + Sql);
            sta.execute(Sql);
            res = true;
            sqlPoolInstance.pool.releaseConnection(connection);

        } catch (Exception e) {
            logic.AppLogger.appLogger.info("data.CrudPresupuesto.agregarPresupuesto() error " + e.getMessage());
            System.out.println("data.CrudPresupuesto.agregarPresupuesto() error " + e.getMessage());
        }
        return res;
    }

    public boolean actualizarPresupuesto(Presupuesto press) {
        boolean res = false;
        try {
            Connection connection = sqlPoolInstance.pool.getConnection();
            Statement sta = connection.createStatement();
            String Sql = "update INDICADORES.dbo.PRESUPUESTO "
                    + "set [CONCEPTOADETALLE] = '" + press.getCONCEPATOADETALLE() + "'"
                    + " where CTAPRESUPUESTO = '" + press.getCTAPRESUPUESTO() + "';";
            AppLogger.appLogger.info("data.CrudPresupuesto.actualizarPresupuesto() sentenpcia +\n" + Sql);
            sta.execute(Sql);
            res = true;
            sqlPoolInstance.pool.releaseConnection(connection);

        } catch (Exception e) {
            logic.AppLogger.appLogger.info("data.CrudPresupuesto.actualizarPresupuesto() error " + e.getMessage());
            System.out.println("data.CrudPresupuesto.actualizarPresupuesto() error " + e.getMessage());
        }
        return res;
    }

    public boolean testConnection() {
        boolean res = false;
        try {

            sqlPoolInstance.initPool();
            Connection connection = sqlPoolInstance.pool.getConnection();

            res = connection != null;
            sqlPoolInstance.pool.releaseConnection(connection);
        } catch (Exception e) {
            System.err.println("data.CrudPresupuesto.testConnection() error " + e.getMessage());
            res = false;
        }
        return res;
    }

}
