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
public class HistoricoCP {

    String Forma_Pago, comentario, Desc_Cta_Pres, Tipo_Documento, Creador, aplicacion, notas, CIA, ACTIVO, CATEGORIA, NOMBRE, CIA_PROV, ESTAD_MORA, MONEDA, CONTA_CRED, DOCUMENTO, PROVEEDOR, TIPOPROV, CTA_PRESUPUESTO, UsuarioRevision, Exactus_Doc;
    int DIAS_CREDITO, id;
    double SALDOLOCAL, SALDO_DOLAR, saldo, MONTO, monto_colones, saldo_colones, abono, abono_colones;
    int RevisadoConta;
    java.util.Date FECHA_DOCUMENTO, FECHA_VENCE, fecha_corte, Fecha_Creacion, Fecha_Revision_Conta;
    ArrayList<AbonoSugerido> sugeridos;
    int adelanto, credito_proveedor;

    public double getAbono() {
        return abono;
    }

    public void setAbono(double abono) {
        this.abono = abono;
    }

    public double getAbono_colones() {
        return abono_colones;
    }

    public void setAbono_colones(double abono_colones) {
        this.abono_colones = abono_colones;
    }

    public int getCredito_proveedor() {
        return credito_proveedor;
    }

    public void setCredito_proveedor(int credito_proveedor) {
        this.credito_proveedor = credito_proveedor;
    }

    public int getAdelanto() {
        return adelanto;
    }

    public void setAdelanto(int adelanto) {
        this.adelanto = adelanto;
    }

    

    public String getForma_Pago() {
        return Forma_Pago;
    }

    public void setForma_Pago(String Forma_Pago) {
        this.Forma_Pago = Forma_Pago;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getUsuarioRevision() {
        return UsuarioRevision;
    }

    public void setUsuarioRevision(String UsuarioRevision) {
        this.UsuarioRevision = UsuarioRevision;
    }

    public String getExactus_Doc() {
        return Exactus_Doc;
    }

    public void setExactus_Doc(String Exactus_Doc) {
        this.Exactus_Doc = Exactus_Doc;
    }

    public Date getFecha_Revision_Conta() {
        return Fecha_Revision_Conta;
    }

    public void setFecha_Revision_Conta(Date Fecha_Revision_Conta) {
        this.Fecha_Revision_Conta = Fecha_Revision_Conta;
    }

    public int getRevisadoConta() {
        return RevisadoConta;
    }

    public void setRevisadoConta(int RevisadoConta) {
        this.RevisadoConta = RevisadoConta;
    }

    public String getDesc_Cta_Pres() {
        return Desc_Cta_Pres;
    }

    public void setDesc_Cta_Pres(String Desc_Cta_Pres) {
        this.Desc_Cta_Pres = Desc_Cta_Pres;
    }

    public String getTipo_Documento() {
        return Tipo_Documento;
    }

    public void setTipo_Documento(String Tipo_Documento) {
        this.Tipo_Documento = Tipo_Documento;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getFecha_Creacion() {
        return Fecha_Creacion;
    }

    public void setFecha_Creacion(Date Fecha_Creacion) {
        this.Fecha_Creacion = Fecha_Creacion;
    }

    public String getAplicacion() {
        return aplicacion;
    }

    public String getCreador() {
        return Creador;
    }

    public void setCreador(String Creador) {
        this.Creador = Creador;
    }

    public void setAplicacion(String aplicacion) {
        this.aplicacion = aplicacion;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    public ArrayList<AbonoSugerido> getSugeridos() {
        return sugeridos;
    }

    public void setSugeridos(ArrayList<AbonoSugerido> sugeridos) {
        this.sugeridos = sugeridos;
    }

    public HistoricoCP() {
        this.sugeridos = new ArrayList<>();
    }

    public String getCTA_PRESUPUESTO() {
        return CTA_PRESUPUESTO;
    }

    public void setCTA_PRESUPUESTO(String CTA_PRESUPUESTO) {
        this.CTA_PRESUPUESTO = CTA_PRESUPUESTO;
    }

    public String getTIPOPROV() {
        return TIPOPROV;
    }

    public void setTIPOPROV(String TIPO_PROV) {
        this.TIPOPROV = TIPO_PROV;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public double getMonto_colones() {
        return monto_colones;
    }

    public void setMonto_colones(double monto_colones) {
        this.monto_colones = monto_colones;
    }

    public double getSaldo_colones() {
        return saldo_colones;
    }

    public void setSaldo_colones(double saldo_colones) {
        this.saldo_colones = saldo_colones;
    }

    public String getCIA() {
        return CIA;
    }

    public Date getFECHA_DOCUMENTO() {
        return FECHA_DOCUMENTO;
    }

    public void setFECHA_DOCUMENTO(Date FECHA_DOCUMENTO) {
        this.FECHA_DOCUMENTO = FECHA_DOCUMENTO;
    }

    public Date getFECHA_VENCE() {
        return FECHA_VENCE;
    }

    public void setFECHA_VENCE(Date FECHA_VENCE) {
        this.FECHA_VENCE = FECHA_VENCE;
    }

    public Date getFecha_corte() {
        return fecha_corte;
    }

    public void setFecha_corte(Date fecha_corte) {
        this.fecha_corte = fecha_corte;
    }

    public void setCIA(String CIA) {
        this.CIA = CIA;
    }

    public String getACTIVO() {
        return ACTIVO;
    }

    public void setACTIVO(String ACTIVO) {
        this.ACTIVO = ACTIVO;
    }

    public String getCATEGORIA() {
        return CATEGORIA;
    }

    public void setCATEGORIA(String CATEGORIA) {
        this.CATEGORIA = CATEGORIA;
    }

    public String getNOMBRE() {
        return NOMBRE;
    }

    public void setNOMBRE(String NOMBRE) {
        this.NOMBRE = NOMBRE;
    }

    public String getCIA_PROV() {
        return CIA_PROV;
    }

    public void setCIA_PROV(String CIA_PROV) {
        this.CIA_PROV = CIA_PROV;
    }

    public String getESTAD_MORA() {
        return ESTAD_MORA;
    }

    public void setESTAD_MORA(String ESTAD_MORA) {
        this.ESTAD_MORA = ESTAD_MORA;
    }

    public String getMONEDA() {
        return MONEDA;
    }

    public void setMONEDA(String MONEDA) {
        this.MONEDA = MONEDA;
    }

    public String getCONTA_CRED() {
        return CONTA_CRED;
    }

    public void setCONTA_CRED(String TIPO_PROVEEDOR) {
        this.CONTA_CRED = TIPO_PROVEEDOR;
    }

    public String getDOCUMENTO() {
        return DOCUMENTO;
    }

    public void setDOCUMENTO(String DOCUMENTO) {
        this.DOCUMENTO = DOCUMENTO;
    }

    public String getPROVEEDOR() {
        return PROVEEDOR;
    }

    public void setPROVEEDOR(String PROVEEDOR) {
        this.PROVEEDOR = PROVEEDOR;
    }

    public int getDIAS_CREDITO() {
        return DIAS_CREDITO;
    }

    public void setDIAS_CREDITO(int DIAS_CREDITO) {
        this.DIAS_CREDITO = DIAS_CREDITO;
    }

    public double getSALDOLOCAL() {
        return SALDOLOCAL;
    }

    public void setSALDOLOCAL(double SALDOLOCAL) {
        this.SALDOLOCAL = SALDOLOCAL;
    }

    public double getSALDO_DOLAR() {
        return SALDO_DOLAR;
    }

    public void setSALDO_DOLAR(double SALDO_DOLAR) {
        this.SALDO_DOLAR = SALDO_DOLAR;
    }

    public double getMONTO() {
        return MONTO;
    }

    public void setMONTO(double MONTO) {
        this.MONTO = MONTO;
    }

    public static HistoricoCP obtenerHistoricoPorDoc(String doc, String cia, String numProv, ArrayList<HistoricoCP> listaHistoricoCP) {
        HistoricoCP h = null;
        int count = 0;
        boolean flag = false;
        while (count < listaHistoricoCP.size() && !flag) {
            HistoricoCP ht = listaHistoricoCP.get(count);
            if (ht.getDOCUMENTO().equals(doc)
                    && ht.getCIA().equals(cia)
                    && ht.getPROVEEDOR().equals(numProv)) {
                flag = true;
                h = ht;
            }
            count++;
        }
        return h;
    }

    public static HistoricoCP obtenerHistoricoPorDocCIAProv(String doc, String cia, String prov, ArrayList<HistoricoCP> listaHistoricoCP) {
        HistoricoCP h = null;
        int count = 0;
        boolean flag = false;
        while (count < listaHistoricoCP.size() && !flag) {
            HistoricoCP ht = listaHistoricoCP.get(count);
            if (ht.getDOCUMENTO().equals(doc)
                    && ht.getCIA().equalsIgnoreCase(cia)
                    && ht.getPROVEEDOR().equals(prov)) {
                flag = true;
                h = ht;
            }
            count++;
        }
        return h;
    }

    public static HistoricoCP obtenerHistoricoPorId(int doc, ArrayList<HistoricoCP> listaHistoricoCP) {
        HistoricoCP h = null;
        int count = 0;
        boolean flag = false;
        while (count < listaHistoricoCP.size() && !flag) {
            HistoricoCP ht = listaHistoricoCP.get(count);
            if (ht.getId() == doc) {
                flag = true;
                h = ht;
            }
            count++;
        }
        return h;
    }

    public static ArrayList<HistoricoCP> getListaFitPorNombreProv(String nombreProv, ArrayList<HistoricoCP> listaHistoricoCP) {
        ArrayList<HistoricoCP> lista = new ArrayList<>();
        listaHistoricoCP.forEach(e -> {
            String np = e.getNOMBRE().toUpperCase();
            if (np.contains(nombreProv)) {
                lista.add(e);
            }
        });
        return lista;
    }

}
