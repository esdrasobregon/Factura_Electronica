/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

import entitys.Emisor;
import entitys.Impuesto;
import entitys.LineaDetalle;
import entitys.Receips;
import entitys.Receptor;
import entitys.ResumenFactura;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author eobregon
 */
//https://github.com/TshRahul/xml_parser/blob/master/src/XMLParser.java
public class ReceipFromXML {

    private String path;

    public ReceipFromXML(String path) {
        this.path = path;

    }

    public Receips getReceip() {
        Receips receip = new Receips();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();

            // Get Document
            //Document document = builder.parse(new File("C:\\Users\\eobregon\\Documents\\edras\\factura.xml"));
            Document document = builder.parse(new File(this.path));

            // Normalize the xml structure
            document.getDocumentElement().normalize();
            getReceipHeaders(document, receip);
            // Get all the element by the tag name
            NodeList laptopList = document.getElementsByTagName("Emisor");
            Emisor e = new Emisor();
            getEmisor(laptopList, e);
            receip.setEmisor(e);

            NodeList ReceptorNode = document.getElementsByTagName("Receptor");
            Receptor r = new Receptor();
            getReceptor(ReceptorNode, r);
            receip.setReceptor(r);

            NodeList ResumenFacturaNode = document.getElementsByTagName("ResumenFactura");
            ResumenFactura ref = new ResumenFactura();
            getResumenFactura(ResumenFacturaNode, ref);
            receip.setResumenFactura(ref);

            receip.setListLineaDetalle(getListaDetallesReceip());

            System.out.println("Nombre empresa " + e.getNombre() + ", Identificacion " + e.getIdentificacion() + ", NombreComercial " + e.getNombreComercial() + ", Ubicacion " + e.getUbicacion() + ", CorreoElectronico " + e.getCorreoElectronico());
            System.out.println("Nombre receptor " + r.getNombre() + ", Identificacion " + r.getIdentificacion() + ", Ubicacion " + r.getUbicacion());
            System.out.println("Total Impuesto " + ref.getTotalImpuesto() + ", TotalVentaNeta " + ref.getTotalVentaNeta() + ", TotalComprobante " + ref.getTotalComprobante());
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return receip;
    }

    private void getReceipHeaders(Document document, Receips receip) {
        try {
            NodeList claveList = document.getElementsByTagName("Clave");
            NodeList CodigoActividadList = document.getElementsByTagName("CodigoActividad");
            NodeList NumeroConsecutivoList = document.getElementsByTagName("NumeroConsecutivo");
            NodeList FechaEmisionList = document.getElementsByTagName("FechaEmision");
            Node laptop = claveList.item(0);
            Element laptopElement = (Element) laptop;
            receip.setClave(laptopElement.getTextContent());
            Node CodigoActividadNode = CodigoActividadList.item(0);
            laptopElement = (Element) CodigoActividadNode;
            receip.setCodigoActividad(laptopElement.getTextContent());
            Node FechaEmisionNode = FechaEmisionList.item(0);
            laptopElement = (Element) FechaEmisionNode;
            //receip.setFechaEmision(laptopElement.getTextContent());
            Node NumeroConsecutivoNode = NumeroConsecutivoList.item(0);
            laptopElement = (Element) NumeroConsecutivoNode;
            receip.setNumeroConsecutivo(laptopElement.getTextContent());
        } catch (Exception e) {
            System.out.println("services.ReceipFromXML.getReceipHeaders() error " + e.getMessage());
        }

    }

    private void getEmisorProperty(Node detail, Emisor e) {

        if (detail.getNodeType() == Node.ELEMENT_NODE) {
            Element detailElement = (Element) detail;
            System.out.println("     " + detailElement.getTagName() + ": " + detailElement.getTextContent());
            switch (detailElement.getTagName()) {
                case "Nombre":
                    e.setNombre(detailElement.getTextContent());
                    break;
                case "Identificacion":
                    e.setIdentificacion(detailElement.getTextContent());
                    break;
                case "NombreComercial":
                    e.setNombreComercial(detailElement.getTextContent());
                    break;
                case "Ubicacion":
                    e.setUbicacion(detailElement.getTextContent());
                    break;
                case "Telefono":
                    e.setTelefono(detailElement.getTextContent());
                    break;
                case "CorreoElectronico":
                    e.setCorreoElectronico(detailElement.getTextContent());
                    break;

            }
        }
    }

    private void getEmisor(NodeList laptopList, Emisor e) {
        for (int i = 0; i < laptopList.getLength(); i++) {
            Node laptop = laptopList.item(i);
            if (laptop.getNodeType() == Node.ELEMENT_NODE) {

                Element laptopElement = (Element) laptop;
                System.out.println("Laptop Name: " + laptopElement.getAttribute("Nombre"));

                NodeList laptopDetails = laptop.getChildNodes();
                for (int j = 0; j < laptopDetails.getLength(); j++) {
                    Node detail = laptopDetails.item(j);
                    getEmisorProperty(detail, e);

                }

            }
        }
    }

    private void getReceptor(NodeList ReceptorNode, Receptor r) {

        for (int i = 0; i < ReceptorNode.getLength(); i++) {
            Node laptop = ReceptorNode.item(i);
            if (laptop.getNodeType() == Node.ELEMENT_NODE) {

                Element laptopElement = (Element) laptop;
                System.out.println("Laptop Name: " + laptopElement.getAttribute("Nombre"));

                NodeList laptopDetails = laptop.getChildNodes();
                for (int j = 0; j < laptopDetails.getLength(); j++) {
                    Node detail = laptopDetails.item(j);
                    getReceptorProperty(detail, r);

                }

            }
        }

    }

    private void getReceptorProperty(Node detail, Receptor e) {

        if (detail.getNodeType() == Node.ELEMENT_NODE) {
            Element detailElement = (Element) detail;
            System.out.println("     " + detailElement.getTagName() + ": " + detailElement.getTextContent());
            switch (detailElement.getTagName()) {
                case "Nombre":
                    e.setNombre(detailElement.getTextContent());
                    break;
                case "Identificacion":
                    e.setIdentificacion(detailElement.getTextContent());
                    break;
                case "Tipo":
                    e.setTipo(detailElement.getTextContent());
                    break;
                case "Ubicacion":
                    e.setUbicacion(detailElement.getTextContent());
                    break;

            }
        }
    }

    private void getLineaDetalleProperty(Node detail, LineaDetalle e) {

        if (detail.getNodeType() == Node.ELEMENT_NODE) {
            Element detailElement = (Element) detail;
            System.out.println("     " + detailElement.getTagName() + ": " + detailElement.getTextContent());
            switch (detailElement.getTagName()) {
                case "Detalle":
                    e.setDetalle(detailElement.getTextContent());
                    break;
                case "Codigo":
                    e.setCodigo(detailElement.getTextContent());
                    break;
                case "CodigoComercial":
                    //e.setCodigoComercial(detailElement.getTextContent());
                    break;
                case "Tipo":
                    e.setDetalle(detailElement.getTextContent());
                    break;
                case "Cantidad":
                    e.setCantidad(detailElement.getTextContent());
                    break;
                case "UnidadMedida":
                    e.setUnidadMedida(detailElement.getTextContent());
                    break;
                case "PrecioUnitario":
                    e.setPrecioUnitario(Double.parseDouble(detailElement.getTextContent()));
                    break;
                case "MontoTotal":
                    e.setMontoTotal(Double.parseDouble(detailElement.getTextContent()));
                    break;
                case "SubTotal":
                    e.setSubTotal(Double.parseDouble(detailElement.getTextContent()));
                    break;
                case "MontoTotalLinea":
                    e.setMontoTotalLinea(Double.parseDouble(detailElement.getTextContent()));
                    break;
                case "NumeroLinea":
                    e.setNumeroLinea(Integer.parseInt(detailElement.getTextContent()));
                    break;
                default:
                    System.err.println("detalle tag " + detailElement.getTagName() + " " + detailElement.getTextContent());
                    break;

            }
        }
    }

    private void getResumenFactura(NodeList ResumenFacturaNode, ResumenFactura ref) {
        for (int i = 0; i < ResumenFacturaNode.getLength(); i++) {
            Node laptop = ResumenFacturaNode.item(i);
            if (laptop.getNodeType() == Node.ELEMENT_NODE) {

                Element laptopElement = (Element) laptop;
                System.out.println("Laptop Name: " + laptopElement.getAttribute("Nombre"));

                NodeList laptopDetails = laptop.getChildNodes();
                getResumenFacturaElemet(laptopDetails, ref);

            }
        }
    }

    private void getResumenFacturaElemet(NodeList laptopDetails, ResumenFactura ref) {
        for (int j = 0; j < laptopDetails.getLength(); j++) {
            Node detail = laptopDetails.item(j);
            getResumenFacturaProperty(detail, ref);

        }
    }

    private void getResumenFacturaProperty(Node detail, ResumenFactura e) {
        if (detail.getNodeType() == Node.ELEMENT_NODE) {
            Element detailElement = (Element) detail;
            System.out.println("     " + detailElement.getTagName() + ": " + detailElement.getTextContent());
            switch (detailElement.getTagName()) {
                case "TotalServGravados":
                    e.setTotalServGravados(Double.parseDouble(detailElement.getTextContent()));
                    break;
                case "TotalServExentos":
                    e.setTotalServExentos(Double.parseDouble(detailElement.getTextContent()));
                    break;
                case "TotalMercanciasGravadas":
                    e.setTotalMercanciasGravadas(Double.parseDouble(detailElement.getTextContent()));
                    break;
                case "TotalMercanciasExentas":
                    e.setTotalMercanciasExentas(Double.parseDouble(detailElement.getTextContent()));
                    break;
                case "TotalGravado":
                    e.setTotalGravado(Double.parseDouble(detailElement.getTextContent()));
                    break;
                case "TotalExento":
                    e.setTotalExento(Double.parseDouble(detailElement.getTextContent()));
                    break;
                case "TotalVenta":
                    e.setTotalVenta(Double.parseDouble(detailElement.getTextContent()));
                    break;
                case "TotalDescuentos":
                    e.setTotalDescuentos(Double.parseDouble(detailElement.getTextContent()));
                    break;
                case "TotalVentaNeta":
                    e.setTotalVentaNeta(Double.parseDouble(detailElement.getTextContent()));
                    break;
                case "TotalImpuesto":
                    e.setTotalImpuesto(Double.parseDouble(detailElement.getTextContent()));
                    break;
                case "TotalOtrosCargos":
                    e.setTotalOtrosCargos(Double.parseDouble(detailElement.getTextContent()));
                    break;
                case "TotalComprobante":
                    e.setTotalComprobante(Double.parseDouble(detailElement.getTextContent()));
                    break;

            }
        }
    }

    private void getImpuestoProperty(Node detail, Impuesto e) {
        if (detail.getNodeType() == Node.ELEMENT_NODE) {
            Element detailElement = (Element) detail;
            System.out.println("     " + detailElement.getTagName() + ": " + detailElement.getTextContent());
            switch (detailElement.getTagName()) {
                case "Codigo":
                    e.setCodigo(detailElement.getTextContent());
                    break;
                case "CodigoTarifa":
                    e.setCodigoTarifa(detailElement.getTextContent());
                    break;
                case "Tarifa":
                    e.setTarifa(Double.parseDouble(detailElement.getTextContent()));
                    break;
                case "Monto":
                    e.setMonto(Double.parseDouble(detailElement.getTextContent()));
                    break;
                default:
                    System.out.println("Impuesto tag " + detailElement.getTextContent());
                    break;

            }
        }
    }

    private ArrayList<LineaDetalle> getListaDetallesReceip() {
        ArrayList<LineaDetalle> listLineaDetalle = new ArrayList<>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();

            // Get Document
            //Document document = builder.parse(new File("C:\\Users\\eobregon\\Documents\\edras\\factura.xml"));
            Document document = builder.parse(new File(this.path));

            // Normalize the xml structure
            document.getDocumentElement().normalize();

            // Get all the element by the tag name
            NodeList detalleImpuestos = document.getElementsByTagName("Impuesto");
            ArrayList<Impuesto> listimp = getImpuesto(detalleImpuestos);
            listimp.forEach(imp -> {
                System.out.println("Impuesto codigo " + imp.getCodigo() + ", CodigoTarifa " + ", " + imp.getCodigoTarifa() + ", monto " + imp.getMonto() + ", tarifa " + imp.getTarifa());

            });
            getListDetalle(detalleImpuestos);
            NodeList detalleServicio = document.getElementsByTagName("LineaDetalle");
            ArrayList<LineaDetalle> listaDetalle = getListDetalle(detalleServicio);
            int i = 0;
            for (LineaDetalle element : listaDetalle) {

                if (i < listimp.size()) {
                    element.setImpuesto(listimp.get(i));
                    LineaDetalle.print(element);
                    i++;

                } else {
                    Impuesto imp = new Impuesto();
                    imp.setCodigo("--");
                    imp.setCodigoTarifa("--");
                    imp.setMonto(0);
                    imp.setTarifa(0);
                    element.setImpuesto(imp);
                }
                listLineaDetalle.add(element);
            }

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listLineaDetalle;
    }

    private ArrayList<LineaDetalle> getListDetalle(NodeList detalleServicio) {
        ArrayList<LineaDetalle> listLineaDetalle = new ArrayList<>();
        for (int i = 0; i < detalleServicio.getLength(); i++) {
            LineaDetalle lind = new LineaDetalle();
            Node laptop = detalleServicio.item(i);
            if (laptop.getNodeType() == Node.ELEMENT_NODE) {
                NodeList laptopDetails = laptop.getChildNodes();
                getDetails(lind, laptopDetails);

            }

            listLineaDetalle.add(lind);
        }
        return listLineaDetalle;
    }

    private void getDetails(LineaDetalle lind, NodeList laptopDetails) {

        for (int j = 0; j < laptopDetails.getLength(); j++) {
            Node detail = laptopDetails.item(j);
            if (detail.getNodeType() == Node.ELEMENT_NODE) {
                Element detailElement = (Element) detail;
                //System.out.println("     " + detailElement.getTagName() + ": " + detailElement.getTextContent());
                getLineaDetalleProperty(detail, lind);
            }

        }
    }

    private ArrayList<Impuesto> getImpuesto(NodeList detalleServicio) {
        ArrayList<Impuesto> listLineaDetalle = new ArrayList<>();
        for (int i = 0; i < detalleServicio.getLength(); i++) {
            Impuesto imp = new Impuesto();
            Node laptop = detalleServicio.item(i);
            if (laptop.getNodeType() == Node.ELEMENT_NODE) {
                NodeList laptopDetails = laptop.getChildNodes();
                obtenerDetalleImpuesto(laptopDetails, imp);

            }
            listLineaDetalle.add(imp);
        }

        return listLineaDetalle;
    }

    private void obtenerDetalleImpuesto(NodeList laptopDetails, Impuesto imp) {

        for (int j = 0; j < laptopDetails.getLength(); j++) {
            Node detail = laptopDetails.item(j);
            if (detail.getNodeType() == Node.ELEMENT_NODE) {
                Element detailElement = (Element) detail;
                //System.out.println("     " + detailElement.getTagName() + ": " + detailElement.getTextContent());
                getImpuestoProperty(detail, imp);

            }

        }
    }
}
