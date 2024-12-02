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
public class Sociedad {

    String nombre, cedula, correo;
    int activo;

    public int getActivo() {
        return activo;
    }

    public void setActivo(int activo) {
        this.activo = activo;
    }

    public Sociedad() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public static Sociedad obtenerSociedadPorDesc(String descripcion, ArrayList<Sociedad> sociedades) {
        Sociedad soc = null;
        int count = 0;
        boolean found = false;
        int length = descripcion.length();
        String ced = descripcion.substring(length - 10, length);
        while (!found && count < sociedades.size()) {
            Sociedad s = sociedades.get(count);

            if (s.getCedula().equalsIgnoreCase(ced)) {
                soc = s;
                found = true;
            }
            count++;
        }
        return soc;
    }

    public ArrayList<Sociedad> quemarSociedades() {
        ArrayList<Sociedad> sociedades = new ArrayList<>();
        Sociedad soc = new Sociedad();
        soc.setActivo(1);
        soc.setNombre("CILT");
        soc.setCedula("3101086411");
        sociedades.add(soc);
        Sociedad soc2 = new Sociedad();
        soc2.setActivo(1);
        soc2.setNombre("RYMSA");
        soc2.setCedula("3101724817");
        sociedades.add(soc2);
        Sociedad soc3 = new Sociedad();
        soc3.setActivo(1);
        soc3.setNombre("IRASA");
        soc3.setCedula("3101119637");
        sociedades.add(soc3);
        Sociedad soc4 = new Sociedad();
        soc4.setActivo(1);
        soc4.setNombre("KATRA");
        soc4.setCedula("3101119531");
        sociedades.add(soc4);
        Sociedad soc5 = new Sociedad();
        soc5.setActivo(1);
        soc5.setNombre("OPYLOG");
        soc5.setCedula("3101466557");
        sociedades.add(soc5);
        Sociedad soc6 = new Sociedad();
        soc6.setActivo(1);
        soc6.setNombre("TURINTEL");
        soc6.setCedula("3101468003");
        sociedades.add(soc6);
        return sociedades;
    }
}
