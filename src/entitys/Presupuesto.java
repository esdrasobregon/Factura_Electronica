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
public class Presupuesto {
    
    public static Presupuesto getPresupuestoGen(String ctaGeneral, ArrayList<Presupuesto> listaPresupuesto) {
        Presupuesto p = null;
        boolean found = false;
        int count = 0;
        while (!found && count < listaPresupuesto.size()) {
            Presupuesto pres = listaPresupuesto.get(count);
            if (pres.getCTAPRESUPUESTO().equals(ctaGeneral)) {
                p = pres;
                found = true;
            }
            count++;
        }
        return p;
    }
    
    public static ArrayList< Presupuesto> getPresupuestoFilt(int idDepartamento, ArrayList<Presupuesto> listaPresupuesto) {
        ArrayList< Presupuesto> lista = new ArrayList<>();
        int count = 0;
        while (count < listaPresupuesto.size()) {
            Presupuesto pres = listaPresupuesto.get(count);
            int idd = Integer.parseInt(pres.getCODDEPA());
            if (idd == idDepartamento) {
                lista.add(pres);
            }
            count++;
        }
        return lista;
    }

    public static ArrayList< Presupuesto> getPresupuestoFilt(String idDepartamento, ArrayList<Presupuesto> listaPresupuesto) {
        ArrayList< Presupuesto> lista = new ArrayList<>();
        int count = 0;
        while (count < listaPresupuesto.size()) {
            Presupuesto pres = listaPresupuesto.get(count);
            
            if (pres.getCODDEPA().equalsIgnoreCase(idDepartamento)) {
                lista.add(pres);
            }
            count++;
        }
        return lista;
    }
    
    public static Presupuesto getPresupuesto(String ctaGeneral, ArrayList<Presupuesto> listaPresupuesto) {
        Presupuesto p = null;
        boolean found = false;
        int count = 0;
        try {
            while (!found && count < listaPresupuesto.size()) {
                Presupuesto pres = listaPresupuesto.get(count);
                String cta = pres.getCTAPRESUPUESTO().trim().substring(3, pres.getCTAPRESUPUESTO().length());
                if (cta.equals(ctaGeneral)) {
                    p = pres;
                    found = true;
                }
                count++;
            }
        } catch (Exception e) {
            return null;
        }
        return p;
    }
    public Presupuesto getPresupuestoPorCtaGeneral(String ctaGeneral, ArrayList<Presupuesto> listaPresupuesto) {
        Presupuesto p = null;
        boolean found = false;
        int count = 0;
        try {
            while (!found && count < listaPresupuesto.size()) {
                Presupuesto pres = listaPresupuesto.get(count);
                if (pres.getCTAPRESUPUESTO().equals(ctaGeneral)) {
                    p = pres;
                    found = true;
                }
                count++;
            }
        } catch (Exception e) {
            return null;
        }
        return p;
    }
    
    private String CIA, PERIODO, CODDEPA, DEPTADETALLE, CODAREA, AREADETALLE, CODCONCEPTO, CONCEPTOADETALLE, CTAPRESUPUESTO, CTAREGULATORIA, DETALLE_REGULA;
    
    public String getCTAPRESUPUESTO() {
        return CTAPRESUPUESTO;
    }
    
    public void setCTAPRESUPUESTO(String CTAPRESUPUESTO) {
        this.CTAPRESUPUESTO = CTAPRESUPUESTO;
    }
    
    public String getCTAREGULATORIA() {
        return CTAREGULATORIA;
    }
    
    public void setCTAREGULATORIA(String CTAREGULATORIA) {
        this.CTAREGULATORIA = CTAREGULATORIA;
    }
    
    public String getDEPTADETALLE() {
        return DEPTADETALLE;
    }
    
    public void setDEPTADETALLE(String DEPTADETALLE) {
        this.DEPTADETALLE = DEPTADETALLE;
    }
    
    public String getCIA() {
        return CIA;
    }
    
    public void setCIA(String CIA) {
        this.CIA = CIA;
    }
    
    public String getPERIODO() {
        return PERIODO;
    }
    
    public void setPERIODO(String PERIODO) {
        this.PERIODO = PERIODO;
    }
    
    public String getCODDEPA() {
        return CODDEPA;
    }
    
    public void setCODDEPA(String CODDEPA) {
        this.CODDEPA = CODDEPA;
    }
    
    public String getCODAREA() {
        return CODAREA;
    }
    
    public void setCODAREA(String CODAREA) {
        this.CODAREA = CODAREA;
    }
    
    public String getAREADETALLE() {
        return AREADETALLE;
    }
    
    public void setAREADETALLE(String AREADETALLE) {
        this.AREADETALLE = AREADETALLE;
    }
    
    public String getCODCONCEPTO() {
        return CODCONCEPTO;
    }
    
    public void setCODCONCEPTO(String CODCONCEPTO) {
        this.CODCONCEPTO = CODCONCEPTO;
    }
    
    public String getCONCEPATOADETALLE() {
        return CONCEPTOADETALLE;
    }
    
    public void setCONCEPTOADETALLE(String CODCONCEPTOADETALLE) {
        this.CONCEPTOADETALLE = CODCONCEPTOADETALLE;
    }
    
    public String getDETALLE_REGULA() {
        return DETALLE_REGULA;
    }
    
    public void setDETALLE_REGULA(String DETALLE_REGULA) {
        this.DETALLE_REGULA = DETALLE_REGULA;
    }
    
    public Presupuesto() {
    }
    private java.math.BigDecimal ENERO;
    
    public java.math.BigDecimal getENERO() {
        return this.ENERO;
    }
    
    public void setENERO(java.math.BigDecimal value) {
        this.ENERO = value;
    }
    
    private java.math.BigDecimal FEBRERO;
    
    public java.math.BigDecimal getFEBRERO() {
        return this.FEBRERO;
    }
    
    public void setFEBRERO(java.math.BigDecimal value) {
        this.FEBRERO = value;
    }
    
    private java.math.BigDecimal MARZO;
    
    public java.math.BigDecimal getMARZO() {
        return this.MARZO;
    }
    
    public void setMARZO(java.math.BigDecimal value) {
        this.MARZO = value;
    }
    
    private java.math.BigDecimal ABRIL;
    
    public java.math.BigDecimal getABRIL() {
        return this.ABRIL;
    }
    
    public void setABRIL(java.math.BigDecimal value) {
        this.ABRIL = value;
    }
    
    private java.math.BigDecimal MAYO;
    
    public java.math.BigDecimal getMAYO() {
        return this.MAYO;
    }
    
    public void setMAYO(java.math.BigDecimal value) {
        this.MAYO = value;
    }
    
    private java.math.BigDecimal JUNIO;
    
    public java.math.BigDecimal getJUNIO() {
        return this.JUNIO;
    }
    
    public void setJUNIO(java.math.BigDecimal value) {
        this.JUNIO = value;
    }
    
    private java.math.BigDecimal JULIO;
    
    public java.math.BigDecimal getJULIO() {
        return this.JULIO;
    }
    
    public void setJULIO(java.math.BigDecimal value) {
        this.JULIO = value;
    }
    
    private java.math.BigDecimal AGOSTO;
    
    public java.math.BigDecimal getAGOSTO() {
        return this.AGOSTO;
    }
    
    public void setAGOSTO(java.math.BigDecimal value) {
        this.AGOSTO = value;
    }
    
    private java.math.BigDecimal SEPTIEMBRE;
    
    public java.math.BigDecimal getSEPTIEMBRE() {
        return this.SEPTIEMBRE;
    }
    
    public void setSEPTIEMBRE(java.math.BigDecimal value) {
        this.SEPTIEMBRE = value;
    }
    
    private java.math.BigDecimal OCTUBRE;
    
    public java.math.BigDecimal getOCTUBRE() {
        return this.OCTUBRE;
    }
    
    public void setOCTUBRE(java.math.BigDecimal value) {
        this.OCTUBRE = value;
    }
    
    private java.math.BigDecimal NOVIEMBRE;
    
    public java.math.BigDecimal getNOVIEMBRE() {
        return this.NOVIEMBRE;
    }
    
    public void setNOVIEMBRE(java.math.BigDecimal value) {
        this.NOVIEMBRE = value;
    }
    
    private java.math.BigDecimal DICIEMBRE;
    
    public java.math.BigDecimal getDICIEMBRE() {
        return this.DICIEMBRE;
    }
    
    public void setDICIEMBRE(java.math.BigDecimal value) {
        this.DICIEMBRE = value;
    }
    
    public Presupuesto(java.math.BigDecimal ENERO_, java.math.BigDecimal FEBRERO_, java.math.BigDecimal MARZO_, java.math.BigDecimal ABRIL_, java.math.BigDecimal MAYO_, java.math.BigDecimal JUNIO_, java.math.BigDecimal JULIO_, java.math.BigDecimal AGOSTO_, java.math.BigDecimal SEPTIEMBRE_, java.math.BigDecimal OCTUBRE_, java.math.BigDecimal NOVIEMBRE_, java.math.BigDecimal DICIEMBRE_) {
        this.ENERO = ENERO_;
        this.FEBRERO = FEBRERO_;
        this.MARZO = MARZO_;
        this.ABRIL = ABRIL_;
        this.MAYO = MAYO_;
        this.JUNIO = JUNIO_;
        this.JULIO = JULIO_;
        this.AGOSTO = AGOSTO_;
        this.SEPTIEMBRE = SEPTIEMBRE_;
        this.OCTUBRE = OCTUBRE_;
        this.NOVIEMBRE = NOVIEMBRE_;
        this.DICIEMBRE = DICIEMBRE_;
    }
}
