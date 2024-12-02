/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entitys;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import logic.JsonCommonFunctions;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;

/**
 *
 * @author eobregon
 */
public class Receips {

    String nombreArchivo;
    Emisor emisor;
    Receptor receptor;
    boolean bienes = true;
    double difereniciaXmlExactus = 0;
    String propietario;
    int cajaChica;
    int subTipoAsiento = -1;
    int idDepartamento, estado, emailIndex, AprobadoDirector, rechazado;
    String cuentaPresupuesto, NumeroConsecutivo, CodigoActividad, Clave, consecutivo, Exactus, cuentaGeneral, PDFAsociated;
    java.util.Date FechaEmision;
    ResumenFactura resumenFactura;
    ArrayList<LineaDetalle> listLineaDetalle;
    String cia;

    public String getCia() {
        return cia;
    }

    public void setCia(String cia) {
        this.cia = cia;
    }

    //Impuesto impuesto;
    public int getSubTipoAsiento() {
        return subTipoAsiento;
    }

    public void setSubTipoAsiento(int subTipoAsiento) {
        this.subTipoAsiento = subTipoAsiento;
    }

    public int esCajaChica() {
        return cajaChica;
    }

    public void setCajaChica(int cajaChica) {
        this.cajaChica = cajaChica;
    }

    public String getPropietario() {
        return propietario;
    }

    public void setPropietario(String propietario) {
        this.propietario = propietario;
    }

    public double getDifereniciaXmlExactus() {
        return difereniciaXmlExactus;
    }

    public void setDifereniciaXmlExactus(double difereniciaXmlExactus) {
        this.difereniciaXmlExactus = difereniciaXmlExactus;
    }

    public boolean isBienes() {
        return bienes;
    }

    public void setBienes(boolean bienes) {
        this.bienes = bienes;
    }

    public int getRechazado() {
        return rechazado;
    }

    public void setRechazado(int rechazado) {
        this.rechazado = rechazado;
    }

    public String getPDFAsociated() {
        return PDFAsociated;
    }

    public void setPDFAsociated(String PDFAsociated) {
        this.PDFAsociated = PDFAsociated;
    }

    public String getCuentaGeneral() {
        return cuentaGeneral;
    }

    public void setCuentaGeneral(String cuentaGeneral) {
        this.cuentaGeneral = cuentaGeneral;
    }

    public int getAprobadoDirector() {
        return AprobadoDirector;
    }

    public void setAprobadoDirector(int AprobadoDirector) {
        this.AprobadoDirector = AprobadoDirector;
    }

    public String getCuentaPresupuesto() {
        return cuentaPresupuesto;
    }

    public void setCuentaPresupuesto(String cuentaPresupuesto) {
        this.cuentaPresupuesto = cuentaPresupuesto;
    }

    public int getEmailIndex() {
        return emailIndex;
    }

    public void setEmailIndex(int emailIndex) {
        this.emailIndex = emailIndex;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public String getExactus() {
        return Exactus;
    }

    public void setExactus(String Exactus) {
        this.Exactus = Exactus;
    }

    public String getConsecutivo() {
        return consecutivo;
    }

    public void setConsecutivo(String Clave) {
        int length = Clave.length();
        this.consecutivo = Clave.substring(length - 8, length);
    }

    public int getIdDepartamento() {
        return idDepartamento;
    }

    public void setIdDepartamento(int idDepartamento) {
        this.idDepartamento = idDepartamento;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public String getNumeroConsecutivo() {
        return NumeroConsecutivo;
    }

    public void setNumeroConsecutivo(String NumeroConsecutivo) {
        this.NumeroConsecutivo = NumeroConsecutivo;
    }

    public java.util.Date getFechaEmision() {
        return FechaEmision;
    }

    public void setFechaEmision(java.util.Date FechaEmision) {
        this.FechaEmision = FechaEmision;
    }

    public String getCodigoActividad() {
        return CodigoActividad;
    }

    public void setCodigoActividad(String CodigoActividad) {
        this.CodigoActividad = CodigoActividad;
    }

    public String getClave() {
        return Clave;
    }

    public void setClave(String Clave) {
        this.Clave = Clave;
    }

    public ArrayList<LineaDetalle> getListLineaDetalle() {
        return listLineaDetalle;
    }

    public void setListLineaDetalle(ArrayList<LineaDetalle> listLineaDetalle) {
        this.listLineaDetalle = listLineaDetalle;
    }

    public ResumenFactura getResumenFactura() {
        return resumenFactura;
    }

    public void setResumenFactura(ResumenFactura resumenFactura) {
        this.resumenFactura = resumenFactura;
    }

    public Emisor getEmisor() {
        return emisor;
    }

    public void setEmisor(Emisor emisor) {
        this.emisor = emisor;
    }

    public Receptor getReceptor() {
        return receptor;
    }

    public void setReceptor(Receptor receptor) {
        this.receptor = receptor;
    }

    public static Receips getReceipsFromNumeroConsecutivo(ArrayList<Receips> receipsList, String NumeroConsecutivo) {
        int count = 0;
        boolean flag = false;
        Receips r = null;
        while (!flag && count < receipsList.size()) {
            Receips receip = receipsList.get(count);
            if (receip.getNumeroConsecutivo().equalsIgnoreCase(NumeroConsecutivo)) {
                r = receip;
                flag = true;
            }
            count++;
        }
        return r;
    }

    public static ArrayList<Receips> getLisReceipsFromNumeroConsecutivo(ArrayList<Receips> receipsList, String NumeroConsecutivo) {
        ArrayList<Receips> lista = new ArrayList<>();
        for (Receips r : receipsList) {
            if (r.getNumeroConsecutivo().contains(NumeroConsecutivo)) {
                lista.add(r);
            }
        }
        return lista;
    }

    public static Receips getReceipsFromJson(String filePath) {

        //String filePath = "C:\\Users\\eobregon\\Documents\\edras\\facturas\\Factura electrónica Nº00100001010000045149.XML";
        System.out.println("entitys.Receips.getReceipsFromJson() file path " + filePath);
        Receips receip = null;
        JSONObject fe = null;
        try {
            // Read the file as bytes
            byte[] fileBytes = Files.readAllBytes(Paths.get(filePath));

            // Convert the bytes to a string
            String fileContent = new String(fileBytes);
            //System.out.println(fileContent);

            fe = XML.toJSONObject(fileContent);

            JSONObject json = JsonCommonFunctions.getJsonObject(fe, "FacturaElectronica");// fe.getJSONObject("FacturaElectronica");
            if (json != null) {
                receip = new Receips();
                System.out.println(json.toString());
                JSONObject jsonReceipGenerals = getGenerals(json);
                JSONObject jsonDetalleServicio = json.getJSONObject("DetalleServicio");
                JSONArray jsonLineasDetalle = JsonCommonFunctions.getJsonArray(jsonDetalleServicio, "LineaDetalle");
                if (jsonLineasDetalle == null) {
                    jsonLineasDetalle = new JSONArray();
                    jsonLineasDetalle.put(jsonDetalleServicio.getJSONObject("LineaDetalle"));
                }
                JSONObject jsonEmisor = json.getJSONObject("Emisor");
                Emisor emisor = Emisor.getEmisorFromJson(jsonEmisor);
                emisor.print();
                receip.setEmisor(emisor);
                JSONObject jsonReceptor = json.getJSONObject("Receptor");
                Receptor r = Receptor.getReceptorFromJson(jsonReceptor);
                r.print();
                receip.setReceptor(r);
                JSONObject jsonResumenFactura = json.getJSONObject("ResumenFactura");
                ResumenFactura rf = ResumenFactura.getResumenFacturaFromJson(jsonResumenFactura);
                receip.setResumenFactura(rf);
                //System.out.println("resumen de factura "+jsonResumenFactura);
                rf.print();
                //System.out.println("Reseptor " + jsonReceptor.toString());
                System.out.println("Generales " + jsonReceipGenerals.toString());
                //System.out.println("DetalleServicio " + jsonDetalleServicio.toString());
                receip.setGenerals(jsonReceipGenerals, receip);
                ArrayList<LineaDetalle> lista = new ArrayList<>();
                for (int i = 0; i < jsonLineasDetalle.length(); i++) {
                    JSONObject jsonLinea = jsonLineasDetalle.getJSONObject(i);
                    LineaDetalle li = LineaDetalle.getLineaDetalleFromJson(jsonLinea);
                    li.print(li);
                    System.out.println("entitys.Receips.getReceipsFromJson() impuesto " + li.getImpuesto().getCodigoTarifa());
                    lista.add(li);
                }

                receip.setListLineaDetalle(lista);
            }
        } catch (IOException e) {
            System.out.println("entitys.Receips.getReceipsFromJson() error " + e.getMessage());
            receip = null;
        }
        return receip;
    }

    public static Receips getReceipsFromJson01(String filePath) {

        //String filePath = "C:\\Users\\eobregon\\Documents\\edras\\facturas\\Factura electrónica Nº00100001010000045149.XML";
        System.out.println("entitys.Receips.getReceipsFromJson() file path " + filePath);
        Receips receip = null;
        JSONObject fe = null;
        try {
            // Read the file as bytes
            byte[] fileBytes = Files.readAllBytes(Paths.get(filePath));

            // Convert the bytes to a string
            String fileContent = new String(fileBytes);
            //System.out.println(fileContent);

            fe = XML.toJSONObject(fileContent);

            JSONObject json = JsonCommonFunctions.getJsonObject(fe, "FacturaElectronica");// fe.getJSONObject("FacturaElectronica");
            if (json != null) {
                receip = new Receips();
                System.out.println(json.toString());
                JSONObject jsonReceipGenerals = getGenerals(json);
                JSONObject jsonDetalleServicio = json.getJSONObject("DetalleServicio");
                JSONArray jsonLineasDetalle = JsonCommonFunctions.getJsonArray(jsonDetalleServicio, "LineaDetalle");
                if (jsonLineasDetalle == null) {
                    jsonLineasDetalle = new JSONArray();
                    jsonLineasDetalle.put(jsonDetalleServicio.getJSONObject("LineaDetalle"));
                }
                JSONObject jsonEmisor = json.getJSONObject("Emisor");
                Emisor emisor = Emisor.getEmisorFromJson(jsonEmisor);
                emisor.print();
                receip.setEmisor(emisor);
                JSONObject jsonReceptor = json.getJSONObject("Receptor");
                Receptor r = Receptor.getReceptorFromJson(jsonReceptor);
                r.print();
                receip.setReceptor(r);
                JSONObject jsonResumenFactura = json.getJSONObject("ResumenFactura");
                ResumenFactura rf = ResumenFactura.getResumenFacturaFromJson(jsonResumenFactura);
                receip.setResumenFactura(rf);
                //System.out.println("resumen de factura "+jsonResumenFactura);
                rf.print();
                //System.out.println("Reseptor " + jsonReceptor.toString());
                System.out.println("Generales " + jsonReceipGenerals.toString());
                //System.out.println("DetalleServicio " + jsonDetalleServicio.toString());
                receip.setGenerals(jsonReceipGenerals, receip);
                ArrayList<LineaDetalle> lista = new ArrayList<>();
                for (int i = 0; i < jsonLineasDetalle.length(); i++) {
                    JSONObject jsonLinea = jsonLineasDetalle.getJSONObject(i);
                    LineaDetalle li = LineaDetalle.getLineaDetalleFromJson(jsonLinea);
                    li.print(li);
                    System.out.println("entitys.Receips.getReceipsFromJson() impuesto " + li.getImpuesto().getCodigoTarifa());
                    lista.add(li);
                }

                receip.setListLineaDetalle(lista);
            }
        } catch (IOException e) {
            System.out.println("entitys.Receips.getReceipsFromJson() error " + e.getMessage());
            receip = null;
        }
        return receip;
    }

    private static JSONObject getGenerals(JSONObject json) {
        JSONObject jsonReceipGenerals = null;
        try {
            String jsonClave = json.get("Clave").toString();
            String jsonCodigoActividad = json.getInt("CodigoActividad") + "";
            String jsonNumeroConsecutivo = json.getString("NumeroConsecutivo");
            String jsonFechaEmision = json.getString("FechaEmision");
            String jsonCondicionVenta = json.getString("CondicionVenta");
            String jsonPlazoCredito = JsonCommonFunctions.getJsonInt(json, "PlazoCredito") + "";//json.getInt("PlazoCredito") + "";
            String jsonMedioPago = json.getInt("MedioPago") + "";
            jsonReceipGenerals = new JSONObject();
            jsonReceipGenerals.put("Clave", jsonClave);
            jsonReceipGenerals.put("CodigoActividad", jsonCodigoActividad);
            jsonReceipGenerals.put("NumeroConsecutivo", jsonNumeroConsecutivo);
            jsonReceipGenerals.put("FechaEmision", jsonFechaEmision);
            jsonReceipGenerals.put("CondicionVenta", jsonCondicionVenta);
            jsonReceipGenerals.put("PlazoCredito", jsonPlazoCredito);
            jsonReceipGenerals.put("MedioPago", jsonMedioPago);
        } catch (Exception e) {
            System.out.println("entitys.Receips.getGenerals() error " + e.getMessage());
        }
        return jsonReceipGenerals;
    }

    public void setGenerals(JSONObject jsonReceipGenerals, Receips r) {
        try {
            r.setClave(jsonReceipGenerals.getString("Clave"));
            //this.MedioPago = jsonReceipGenerals.getString("MedioPago");
            //r.setCodigoActividad(jsonReceipGenerals.getString("CodigoActividad"));
            SimpleDateFormat dateFormat01 = new SimpleDateFormat("yyyy-MM-dd");
            String dateString = jsonReceipGenerals.getString("FechaEmision");
            String substring = dateString.substring(0, 10);
            java.util.Date date = dateFormat01.parse(substring);
            r.setFechaEmision(date);
            r.setNumeroConsecutivo(jsonReceipGenerals.getString("NumeroConsecutivo"));
            r.setConsecutivo(jsonReceipGenerals.getString("consecutivo"));
            //r.setMedioPago(jsonReceipGenerals.getString("MedioPago"));
            //r.setPlazoCredito(jsonReceipGenerals.getString("PlazoCredito"));
            //r.setCondicionVenta(jsonReceipGenerals.getString("CondicionVenta"));
            //r.setCodigoActividad = jsonReceipGenerals.getString("CodigoActividad");
        } catch (Exception e) {
            System.out.println("entitys.Receips.setGenerals() error " + e.getMessage());
        }
    }

    /*public static Receips getReceipFromListByNumeroConsecutivo(String numeroConsecutivo, ArrayList<Receips> receipList) {
        Receips r = null;
        int count = 0;
        boolean found = false;
        while (!found && count < receipList.size()) {
            Receips rec = receipList.get(count);
            if (rec.getNumeroConsecutivo().equalsIgnoreCase(numeroConsecutivo)) {
                found = true;
                r = rec;
            }
            count++;
        }
        return r;
    }
     */
    public static Receips getReceipByClave(String clave, ArrayList<Receips> receipList) {
        Receips r = null;
        int count = 0;
        boolean found = false;
        while (!found && count < receipList.size()) {
            Receips rec = receipList.get(count);
            if (rec.getClave().equalsIgnoreCase(clave)) {
                found = true;
                r = rec;
            }
            count++;
        }
        return r;
    }

    public static ArrayList<Receips> getSubReceipListFromListByNumeroConsecutivo(String numConsecutivo, ArrayList<Receips> receipList) {
        ArrayList<Receips> res = new ArrayList<>();
        for (int i = 0; i < receipList.size(); i++) {
            Receips rec = receipList.get(i);
            if (rec.getNumeroConsecutivo().contains(numConsecutivo)) {
                res.add(rec);
            }
        }
        return res;
    }

}
/*<Clave>50605122300310117322100300001010000001983177297705</Clave>
<CodigoActividad>523205</CodigoActividad>
<NumeroConsecutivo>00300001010000001983</NumeroConsecutivo>
<FechaEmision>2023-12-05T15:28:09-06:00</FechaEmision>*/
