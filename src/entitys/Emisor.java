/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entitys;

import logic.JsonCommonFunctions;
import org.json.JSONObject;

/**
 *
 * @author eobregon
 */
public class Emisor {

    String Nombre, Identificacion, NombreComercial, Ubicacion, Telefono, CorreoElectronico;

    public static Emisor getEmisorFromJson(JSONObject jsonEmisor) {
        Emisor e = null;
        try {
            e = new Emisor();
            e.setNombre(jsonEmisor.getString("Nombre"));
            e.setNombreComercial(JsonCommonFunctions.getJsonString(jsonEmisor, "NombreComercial"));
            e.setCorreoElectronico(jsonEmisor.getString("CorreoElectronico"));
            JSONObject jsonEmisorIdentificacion = jsonEmisor.getJSONObject("Identificacion");
            e.setIdentificacion("Tipo " + jsonEmisorIdentificacion.getString("Tipo")
                    + "Numero " + jsonEmisorIdentificacion.get("Numero").toString());
            JSONObject jsonEmisorUbicacion = jsonEmisor.getJSONObject("Ubicacion");
            e.setUbicacion("Provincia " + jsonEmisorUbicacion.get("Provincia").toString()
                    + "Canton " + jsonEmisorUbicacion.get("Canton").toString()
                    + "Distrito " + jsonEmisorUbicacion.get("Distrito").toString()
                    + "OtrasSenas " + jsonEmisorUbicacion.getString("OtrasSenas"));

            JSONObject jsonEmisorTelefono = jsonEmisor.getJSONObject("Telefono");
            e.setTelefono("CodigoPais " + jsonEmisorTelefono.get("CodigoPais").toString() + "NumTelefono " + jsonEmisorTelefono.get("NumTelefono").toString());

        } catch (Exception ex) {
            System.out.println("entitys.Emisor.getEmisorFromJson() error " + ex.getMessage());
        }
        return e;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    public String getIdentificacion() {
        return Identificacion;
    }

    public void setIdentificacion(String Identificacion) {
        this.Identificacion = Identificacion;
    }

    public String getNombreComercial() {
        return NombreComercial;
    }

    public void setNombreComercial(String NombreComercial) {
        this.NombreComercial = NombreComercial;
    }

    public String getUbicacion() {
        return Ubicacion;
    }

    public void setUbicacion(String Ubicacion) {
        this.Ubicacion = Ubicacion;
    }

    public String getTelefono() {
        return Telefono;
    }

    public void setTelefono(String Telefono) {
        this.Telefono = Telefono;
    }

    public String getCorreoElectronico() {
        return CorreoElectronico;
    }

    public void setCorreoElectronico(String CorreoElectronico) {
        this.CorreoElectronico = CorreoElectronico;
    }

    public void print() {
        System.out.println("entitys.Emisor.print() Nombre " + Nombre
                + "\n Identificacion " + Identificacion
                + "\n NombreComercial " + NombreComercial
                + "\n Telefono " + Telefono
                + "\n Ubicacion " + Ubicacion
                + "\n CorreoElectronico " + CorreoElectronico);
    }
}
