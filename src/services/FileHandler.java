/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

/**
 *
 * @author eobregon
 */
import entitys.CodigoComercial;
import entitys.Descuento;
import entitys.DetalleServicio;
import entitys.Emisor;
import entitys.Impuesto;
import entitys.LineaDetalle;
import entitys.Receips;
import entitys.Receptor;
import entitys.ResumenFactura;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import logic.JsonCommonFunctions;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;

public class FileHandler {

    public static void main(String[] args) {
        //FileHandler fh = new FileHandler();
        //
        String path = "C:\\Users\\eobregon\\Documents\\edras\\BackUpFacturas";
        readReceipFiles(path);
        //System.out.println(getJsonStringFromFile("C:\\Users\\eobregon\\Documents\\edras\\facturas\\003101485434-FE-00200001010000000337.xml"));
    }

    /**
     * this method permits the user to choose the folder receipt location from
     * where the receipts can be load
     */
    public static ArrayList<Receips> readReceipFiles(String path) {
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();
        ArrayList<Receips> receiptList = new ArrayList<>();
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile() && (listOfFiles[i].getName().contains(".xml") || listOfFiles[i].getName().contains(".XML"))) {
                System.out.println("reading file" + listOfFiles[i].getName());
                String filePath = path + "\\" + listOfFiles[i].getName();
                Receips r = getJsonStringFromFile(filePath);
                if (r != null) {
                    r.setNombreArchivo(listOfFiles[i].getName());
                    receiptList.add(r);
                } else {
                    System.out.println("services.FileHandler.readReceipFiles() nulo " + i);
                }
            }
        }
        return receiptList;
    }

    public static ArrayList<Receips> readReceipNotaCreditoFiles(String path) {
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();
        ArrayList<Receips> receiptList = new ArrayList<>();
        if (listOfFiles != null) {

            for (int i = 0; i < listOfFiles.length; i++) {
                if (listOfFiles[i].isFile() && (listOfFiles[i].getName().contains(".xml") || listOfFiles[i].getName().contains(".XML"))) {
                    System.out.println("reading file" + listOfFiles[i].getName());
                    String filePath = path + "\\" + listOfFiles[i].getName();
                    Receips r = getJsonStringNotaCreditoFromFile(filePath);
                    if (r != null) {
                        r.setNombreArchivo(listOfFiles[i].getName());
                        receiptList.add(r);
                    } else {
                        System.out.println("services.FileHandler.readReceipFiles() nulo " + i);
                    }
                }
            }
        }
        return receiptList;
    }

    public static Receips getJsonStringFromFile(String filePath) {

        //String filePath = "C:\\Users\\eobregon\\Documents\\edras\\facturas\\Factura electrónica Nº00100001010000045149.XML";
        String fileContent = null;
        Receips receip = null;
        JSONObject fe = null;
        try {
            // Read the file as bytes
            byte[] fileBytes = Files.readAllBytes(Paths.get(filePath));

            // Convert the bytes to a string
            fileContent = new String(fileBytes);
            //System.out.println(fileContent);

            fe = XML.toJSONObject(fileContent);

            JSONObject json = JsonCommonFunctions.getJsonObject(fe, "FacturaElectronica"); //fe.getJSONObject("FacturaElectronica");
            if (json != null) {
                receip = new Receips();
                System.out.println("json " + json.toString());
                JSONObject jsonEmisor = json.getJSONObject("Emisor");
                Emisor em = Emisor.getEmisorFromJson(jsonEmisor);
                em.print();
                receip.setEmisor(em);
                //System.out.println("Emisor " + jsonEmisor);
                JSONObject jsonReceptor = json.getJSONObject("Receptor");
                Receptor r = Receptor.getReceptorFromJson(jsonReceptor);
                r.print();
                receip.setReceptor(r);
                //System.out.println("Receptor " + jsonReceptor);
                JSONObject generals = getGenerals(json);
                receip.setGenerals(generals, receip);
                JSONObject jsonResumenFactura = json.getJSONObject("ResumenFactura");
                ResumenFactura rf = ResumenFactura.getResumenFacturaFromJson(jsonResumenFactura);
                rf.print();
                receip.setResumenFactura(rf);
                //System.out.println("Resumen factura " + jsonResumenFactura);
                JSONObject jsonDetalleServicio = json.getJSONObject("DetalleServicio");
                //System.out.println("Detalle servicio " + jsonDetalleServicio);
                ArrayList<LineaDetalle> lista = getArrayLineadetalle(jsonDetalleServicio);

                if (lista == null) {
                    receip = null;
                } else {
                    receip.setListLineaDetalle(lista);
                }
            }
        } catch (Exception e) {
            System.err.println("services.FileHandler.getJsonStringFromFile() error " + e.getMessage() + " factura " + filePath);
            receip = null;
        }
        System.out.println("Receipt result " + receip);
        return receip;
    }

    public static Receips getJsonStringNotaCreditoFromFile(String filePath) {

        //String filePath = "C:\\Users\\eobregon\\Documents\\edras\\facturas\\Factura electrónica Nº00100001010000045149.XML";
        String fileContent = null;
        Receips receip = null;
        JSONObject fe = null;
        try {
            // Read the file as bytes
            byte[] fileBytes = Files.readAllBytes(Paths.get(filePath));

            // Convert the bytes to a string
            fileContent = new String(fileBytes);
            //System.out.println(fileContent);

            fe = XML.toJSONObject(fileContent);

            JSONObject json = JsonCommonFunctions.getJsonObject(fe, "NotaCreditoElectronica"); //fe.getJSONObject("FacturaElectronica");
            if (json != null) {
                receip = new Receips();
                System.out.println("json " + json.toString());
                JSONObject jsonEmisor = json.getJSONObject("Emisor");
                Emisor em = Emisor.getEmisorFromJson(jsonEmisor);
                em.print();
                receip.setEmisor(em);
                //System.out.println("Emisor " + jsonEmisor);
                JSONObject jsonReceptor = json.getJSONObject("Receptor");
                Receptor r = Receptor.getReceptorFromJson(jsonReceptor);
                r.print();
                receip.setReceptor(r);
                //System.out.println("Receptor " + jsonReceptor);
                JSONObject generals = getGenerals(json);
                receip.setGenerals(generals, receip);
                JSONObject jsonResumenFactura = json.getJSONObject("ResumenFactura");
                ResumenFactura rf = ResumenFactura.getResumenFacturaFromJson(jsonResumenFactura);
                rf.print();
                receip.setResumenFactura(rf);
                //System.out.println("Resumen factura " + jsonResumenFactura);
                JSONObject jsonDetalleServicio = json.getJSONObject("DetalleServicio");
                //System.out.println("Detalle servicio " + jsonDetalleServicio);
                ArrayList<LineaDetalle> lista = getArrayLineadetalle(jsonDetalleServicio);

                if (lista == null) {
                    receip = null;
                } else {
                    receip.setListLineaDetalle(lista);
                }
            }
        } catch (Exception e) {
            System.err.println("services.FileHandler.getJsonStringFromFile() error " + e.getMessage() + " factura " + filePath);
            receip = null;
        }
        System.out.println("Receipt result " + receip);
        return receip;
    }

    private static JSONObject getGenerals(JSONObject json) {
        JSONObject jsonReceipGenerals = null;
        try {
            String jsonFechaEmision = json.getString("FechaEmision");
            String jsonClave = json.get("Clave").toString();
            //String jsonCodigoActividad = json.getInt("CodigoActividad") + "";
            String jsonNumeroConsecutivo = json.getString("NumeroConsecutivo");
            String consecutivo = jsonClave.substring(jsonClave.length() - 8, jsonClave.length());
            //String jsonCondicionVenta = json.getString("CondicionVenta");
            //String jsonPlazoCredito = json.getInt("PlazoCredito") + "";
            //String jsonMedioPago = json.getInt("MedioPago") + "";
            jsonReceipGenerals = new JSONObject();
            jsonReceipGenerals.put("Clave", jsonClave);
            //jsonReceipGenerals.put("CodigoActividad", jsonCodigoActividad);
            jsonReceipGenerals.put("NumeroConsecutivo", jsonNumeroConsecutivo);
            jsonReceipGenerals.put("FechaEmision", jsonFechaEmision);
            jsonReceipGenerals.put("consecutivo", consecutivo);
            // jsonReceipGenerals.put("CondicionVenta", jsonCondicionVenta);
            //jsonReceipGenerals.put("PlazoCredito", jsonPlazoCredito);
            //jsonReceipGenerals.put("MedioPago", jsonMedioPago);
        } catch (Exception e) {
            System.out.println("services.FileHandler.getGenerals() error " + e.getMessage());
        }
        return jsonReceipGenerals;
    }

    private static ArrayList<LineaDetalle> getArrayLineadetalle(JSONObject jsonDetalleServicio) {
        ArrayList<LineaDetalle> lista = new ArrayList<>();
        try {
            JSONArray array = JsonCommonFunctions.getJsonArray(jsonDetalleServicio, "LineaDetalle");

            if (array == null) {
                array = new JSONArray();
                JSONObject jsonObj = jsonDetalleServicio.getJSONObject("LineaDetalle");
                array.put(jsonObj);
            }
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                LineaDetalle ld = LineaDetalle.getLineaDetalleFromJson(object);
                ld.print(ld);
                if (ld != null) {
                    lista.add(ld);
                }
            }
        } catch (Exception e) {
            System.err.println("services.FileHandler.getArrayLineadetalle() error " + e.getMessage());
            lista = getlineadetalle(jsonDetalleServicio);
        }
        return lista;
    }

    private static ArrayList<LineaDetalle> getlineadetalle(JSONObject jsonDetalleServicio) {
        ArrayList<LineaDetalle> lista = new ArrayList<>();
        try {
            JSONObject jsonObj = JsonCommonFunctions.getJsonObject(jsonDetalleServicio, "LineaDetalle");
            LineaDetalle ld = LineaDetalle.getLineaDetalleFromJson(jsonObj);
            if (ld != null) {
                lista.add(ld);
            }
        } catch (Exception e) {
            System.err.println("services.FileHandler.getlineadetalle() error " + e.getMessage());
        }
        return lista;
    }

}
