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
public class Receptor {

    String Nombre, NombreComercial, Identificacion, Tipo, Ubicacion, Telefono, CorreoElectronico;

    public String getCorreoElectronico() {
        return CorreoElectronico;
    }

    public void setCorreoElectronico(String CorreoElectronico) {
        this.CorreoElectronico = CorreoElectronico;
    }

    public String getTelefono() {
        return Telefono;
    }

    public void setTelefono(String Telefono) {
        this.Telefono = Telefono;
    }

    public String getNombreComercial() {
        return NombreComercial;
    }

    public void setNombreComercial(String NombreComercial) {
        this.NombreComercial = NombreComercial;
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

    public String getTipo() {
        return Tipo;
    }

    public void setTipo(String Tipo) {
        this.Tipo = Tipo;
    }

    public String getUbicacion() {
        return Ubicacion;
    }

    public void setUbicacion(String Ubicacion) {
        this.Ubicacion = Ubicacion;
    }

    public static Receptor getReceptorFromJson(JSONObject jsonReceptor) {
        Receptor r = null;
        try {
            r = new Receptor();
            r.setNombre(jsonReceptor.getString("Nombre"));
            r.setCorreoElectronico(jsonReceptor.getString("CorreoElectronico"));
            JSONObject jsonReceptorIdentificacion = jsonReceptor.getJSONObject("Identificacion");
            r.setIdentificacion(jsonReceptorIdentificacion.getString("Tipo")
                    + "-" + jsonReceptorIdentificacion.get("Numero").toString());
            JSONObject jsonReceptorUbicacion = jsonReceptor.getJSONObject("Ubicacion");

            JSONObject jsonReceptorTelefono = jsonReceptor.getJSONObject("Telefono");
            r.setTelefono("CodigoPais " + jsonReceptorTelefono.get("CodigoPais").toString()
                    + " NumTelefono " + jsonReceptorTelefono.get("NumTelefono").toString());
            r.setNombreComercial(JsonCommonFunctions.getJsonString(jsonReceptor, "NombreComercial"));
            r.setUbicacion("Provincia " + jsonReceptorUbicacion.get("Provincia").toString()
                    + " Canton " + jsonReceptorUbicacion.get("Canton").toString()
                    + " Distrito " + jsonReceptorUbicacion.getString("Distrito")
                    + " OtrasSenas " + jsonReceptorUbicacion.getString("OtrasSenas"));
        } catch (Exception e) {
            System.out.println("entitys.Receptor.getReceptorFromJson() error " + e.getMessage());
        }
        return r;
    }

    public void print() {
        System.out.println("entitys.Receptor.print() Nombre "
                + this.Nombre
                + "\n, NombreComercial " + NombreComercial
                + "\n, Identificacion " + Identificacion
                + "\n, Tipo " + Tipo
                + "\n, Ubicacion " + Ubicacion
                + "\n, Telefono " + Telefono
                + "\n, CorreoElectronico " + CorreoElectronico);
    }

}
/*<Receptor>
<Nombre>
<![CDATA[ EDRAS OBREGON UBEDA ]]>
</Nombre>
<Identificacion>
<Tipo>01</Tipo>
<Numero>801190752</Numero>
</Identificacion>
<Ubicacion>
<Provincia>1</Provincia>
<Canton>10</Canton>
<Distrito>05</Distrito>
<OtrasSenas>
<![CDATA[ SAN FELIPE, AV 12, C 7 ]]>
</OtrasSenas>
</Ubicacion>
</Receptor>*/
