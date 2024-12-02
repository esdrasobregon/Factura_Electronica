/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entitys;

import java.util.ArrayList;
import org.json.JSONObject;

import java.util.ArrayList;
import org.json.JSONObject;

/**
 *
 * @author programador1
 */
public class Departamento {

    boolean activar;
    String Descripcion;
    int Id_Departamento;
    String Observaciones;
    String DEPARTAMENTO;
    String JEFE;

    public String getJEFE() {
        return JEFE;
    }

    public void setJEFE(String JEFE) {
        this.JEFE = JEFE;
    }

    public String getDEPARTAMENTO() {
        return DEPARTAMENTO;
    }

    public void setDEPARTAMENTO(String DEPARTAMENTO) {
        this.DEPARTAMENTO = DEPARTAMENTO;
    }
    public ArrayList<Puesto> puestos;

    public Departamento() {
        this.puestos = new ArrayList<>();
    }

    public Departamento(String Descripcion, int Id_Departamento, String Observaciones) {
        this.Descripcion = Descripcion;
        this.Id_Departamento = Id_Departamento;
        this.Observaciones = Observaciones;
    }

    public boolean isActivar() {
        return activar;
    }

    public void setActivar(boolean activar) {
        this.activar = activar;
    }

    public String getObservaciones() {
        return Observaciones;
    }

    public void setObservaciones(String Observaciones) {
        this.Observaciones = Observaciones;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String Descripcion) {
        this.Descripcion = Descripcion;
    }

    public int getId_Departamento() {
        return Id_Departamento;
    }

    public void setId_Departamento(int Id_Departamento) {
        this.Id_Departamento = Id_Departamento;
    }

    public static void loadPuestos(ArrayList<Puesto> p, ArrayList<Departamento> d) {
        d.forEach(e -> {
            p.forEach(pe -> {
                if (pe.getId_Departamento() == e.getId_Departamento()) {

                    e.puestos.add(pe);
                }
            });
        });
    }

    public static boolean departamentoExist(ArrayList<Departamento> departamentos, int d) {
        boolean found = false;
        int count = 0;
        while (count < departamentos.size() && !found) {
            if (departamentos.get(count).getId_Departamento() == d) {
                found = true;
            }
            count++;
        }
        return found;
    }

    public static Departamento getDepartamento(ArrayList<Departamento> departamentos, String descripcion) {
        Departamento dep = null;
        boolean found = false;
        int count = 0;
        while (count < departamentos.size() && !found) {
            if (departamentos.get(count).getDescripcion().equalsIgnoreCase(descripcion)) {
                dep = departamentos.get(count);
                found = true;
            }
            count++;
        }
        return dep;
    }

    public static Departamento getDepartamentoByCodDepa(ArrayList<Departamento> departamentos, String codeDepa) {
        Departamento dep = null;
        boolean found = false;
        int count = 0;

        while (count < departamentos.size() && !found) {
            try {
                String id = departamentos.get(count).getDEPARTAMENTO();
                if (codeDepa.equalsIgnoreCase(id)) {
                    dep = departamentos.get(count);
                    found = true;
                }
            } catch (Exception e) {
                System.out.println("entitys.Departamento.getDepartamentoByCodDepa() error " + e.getMessage());
            }
            count++;
        }
        return dep;
    }
    
    public static Departamento getDepartamentoById(ArrayList<Departamento> departamentos, int id) {
        Departamento dep = null;
        boolean found = false;
        int count = 0;

        while (count < departamentos.size() && !found) {
            try {
                int iddep = Integer.parseInt(departamentos.get(count).getDEPARTAMENTO());
                
                if (iddep == id) {
                    dep = departamentos.get(count);
                    found = true;
                }
            } catch (Exception e) {
                System.out.println("entitys.Departamento.getDepartamentoByCodDepa() error " + e.getMessage());
            }
            count++;
        }
        return dep;
    }
    
    public static Departamento getDepartamentoByStringIdDesc(ArrayList<Departamento> departamentos, String d) {
        Departamento dep = null;
        boolean found = false;
        int count = 0;
        while (count < departamentos.size() && !found) {
            if (departamentos.get(count).getDescripcion().equalsIgnoreCase(d)) {
                dep = departamentos.get(count);
                found = true;
            }
            count++;
        }
        return dep;
    }

    public static Departamento getDepartamentoPorStringId(ArrayList<Departamento> departamentos, int id) {
        Departamento dep = null;
        boolean found = false;
        int count = 0;
        while (count < departamentos.size() && !found) {
            Departamento d = departamentos.get(count);
            int id1 = Integer.parseInt(d.getDEPARTAMENTO());
            if (id1 == id) {
                dep = d;
                found = true;
            }
            count++;
        }
        return dep;
    }

    /**
     * this function takes a departamento id and then look for it in the list
     * departamentos
     *
     * @param idDepartamento is the id of the departamento to look for
     * @param departamentos its a departamnento list
     * @return a string descripcion of the departamento
     */
    public static String getNombreDepartamento(ArrayList<Departamento> departamentos, int idDepartamento) {
        String result = "";
        int count = 0;
        boolean stop = false;
        while (count < departamentos.size() && !stop) {
            if (departamentos.get(count).getId_Departamento() == idDepartamento) {
                result = departamentos.get(count).getDescripcion();
                stop = true;

            }
            count++;
        }
        return result;
    }

    public Departamento getDepartamentoFromJson(JSONObject jsonObj) {
        Departamento d = new Departamento();

        d.setId_Departamento(jsonObj.getInt("idDepartamento"));
        d.setObservaciones(jsonObj.getString("observaciones"));
        d.setDescripcion(jsonObj.getString("descripcion"));
        return d;

    }

    public JSONObject getJsonDepartamento(Departamento d) {
        JSONObject departamentoJson = new JSONObject();

        departamentoJson.put("idDepartamento", d.getId_Departamento());
        departamentoJson.put("observaciones", d.getObservaciones());
        departamentoJson.put("descripcion", d.getDescripcion());
        return departamentoJson;

    }

}
