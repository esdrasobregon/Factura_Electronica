/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package data;

import entitys.Usuario;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.logging.Logger;
import java.sql.Connection;
import java.sql.Statement;

public class crudUsuario {
    
    public static Usuario getUsuario(String id) {
        //Logger.getAnonymousLogger().info("Getusuario: "+id + " "+usserName);
        Usuario usuario = null;
        
        try {
            
            Connection conn = DbPoolHandler.connectionPool.getConnection();
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery("CALL `getUsuarioPlanilla`('" + id + "')");
            if (rs == null) {
                Logger.getAnonymousLogger().info("ResultSet ");
            } else {
                rs.last();
                Logger.getAnonymousLogger().info("Lineas encontradas:  " + rs.getRow());
                rs.beforeFirst();
            }
            while (rs != null && rs.next()) {
                
                usuario = new Usuario();
                
                rs.getString("IdEmpleado");
                
                usuario.setIdEmpleado(rs.getString("IdEmpleado"));
                
                usuario.setNombre(rs.getString("Nombre"));
                
                usuario.setPrimer_Apellido(rs.getString("Primer_Apellido"));
                
                usuario.setSegundo_Apellido(rs.getString("Segundo_Apellido"));
                
                usuario.setDepartamento(rs.getInt("Departamento"));
                
                usuario.setIdPuesto(rs.getInt("Puesto"));
                
                usuario.setActivo(rs.getInt("Activo"));
                
                usuario.setIdTarjeta(rs.getString("IdTarjeta"));
                
                usuario.setFechaIngreso(rs.getDate("FechaIngreso"));
                
                usuario.setId(rs.getInt("Idempleado"));
                
                System.out.println("nombre " + usuario.getNombre());
                
            }
            DbPoolHandler.connectionPool.releaseConnection(conn);
            rs.close();
            statement.close();
            return usuario;
        } catch (Exception e) {
            System.err.println("data.crudUsuario.getUsuario()error: " + e.getMessage());
            return null;
        }
    }
    
    public static Usuario Login(String userName, String contrasenya) {
        Usuario usuario = null;
        
        try {
            Connection conn = DbPoolHandler.connectionPool.getConnection();
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery("CALL `loginUsuario`('" + userName + "','" + contrasenya + "')");   //modulo.ListarSQL("CALL `loginUsuario`('" + usserName + "','" + contrasenya + "')");

            while (rs != null && rs.next()) {
                
                usuario = new Usuario();
                
                usuario.setIdUsuario(rs.getInt(2));
                usuario.setTipoUsuario(rs.getInt(3));
                usuario.setIdEmpleado(rs.getString(4));
                usuario.setNombre(rs.getString(5));
                usuario.setPrimer_Apellido(rs.getString(6));
                usuario.setSegundo_Apellido(rs.getString(7));
                usuario.setDepartamento(rs.getInt(8));
                usuario.setIdPuesto(rs.getInt(9));
                usuario.setActivo(rs.getInt(10));
                //there are some idtarjeta null and there is not actual use to it
                //usuario.setIdTarjeta(rs.getString("IdTarjeta"));
                usuario.setFechaIngreso(rs.getDate(15));
                usuario.setId(rs.getInt(16));
                usuario.setNombreUsuario(userName);
                usuario.setContrasenya(contrasenya);
            }
            DbPoolHandler.connectionPool.releaseConnection(conn);
            rs.close();
            statement.close();
        } catch (Exception e) {
            System.out.println("data.crudUsuario.Login() error " + e.getMessage());
        }
        return usuario;
    }
    
    public static Usuario Login(String userName) {
        Usuario usuario = null;
        
        try {
            Connection conn = DbPoolHandler.connectionPool.getConnection();
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery("CALL `loginUsuarioConNombreUsuario`('" + userName + "')");   //modulo.ListarSQL("CALL `loginUsuario`('" + usserName + "','" + contrasenya + "')");

            while (rs != null && rs.next()) {
                
                usuario = new Usuario();
                
                usuario.setIdUsuario(rs.getInt(2));
                usuario.setTipoUsuario(rs.getInt(3));
                usuario.setIdEmpleado(rs.getString(4));
                usuario.setNombre(rs.getString(5));
                usuario.setPrimer_Apellido(rs.getString(6));
                usuario.setSegundo_Apellido(rs.getString(7));
                usuario.setDepartamento(rs.getInt(8));
                usuario.setIdPuesto(rs.getInt(9));
                usuario.setActivo(rs.getInt(10));
                //there are some idtarjeta null and there is not actual use to it
                //usuario.setIdTarjeta(rs.getString("IdTarjeta"));
                usuario.setFechaIngreso(rs.getDate(15));
                usuario.setId(rs.getInt(16));
                usuario.setNombreUsuario(userName);
                usuario.setContrasenya(rs.getString("contrasenya"));
            }
            DbPoolHandler.connectionPool.releaseConnection(conn);
            rs.close();
            statement.close();
        } catch (Exception e) {
            System.out.println("data.crudUsuario.Login() error " + e.getMessage());
        }
        return usuario;
    }
    
    public static Usuario getUsuario(String usserName, String password) {
        Usuario usuario = null;
        try {
            Usuario us = getIdEmpleado();
            usuario = getUsuario(us.getIdEmpleado(), usserName);
            if (usuario == null) {
                Logger.getAnonymousLogger().info("Usuario null");
                return null;
            }
            usuario.setIdEmpleado(us.getIdEmpleado());
            usuario.setIdUsuario(us.getIdUsuario());
            usuario.setContrasenya(DataUser.password);
            usuario.setNombreUsuario(usserName);
            usuario.setTipoUsuario(us.getTipoUsuario());
        } catch (Exception e) {
            System.err.println("error getting usser " + e.getMessage() + "  " + e.getCause().toString());
        }
        return usuario;
    }
    
    private static Usuario getIdEmpleado() {
        Usuario usu = new Usuario();
        
        try {
            Connection conn = DbPoolHandler.connectionPool.getConnection();
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery("call login('" + DataUser.username + "','" + DataUser.password + "')");
            while (rs.next()) {
                Logger.getAnonymousLogger().info(rs.getString(1) + "  " + rs.getInt(2));
                usu.setIdEmpleado(rs.getString(1));
                usu.setIdUsuario(rs.getInt(2));
                usu.setTipoUsuario(rs.getInt(3));
                System.out.println("idempleado " + usu.getIdEmpleado() + " idusuario " + usu.getIdUsuario());
                
            }
            DbPoolHandler.connectionPool.releaseConnection(conn);
            rs.close();
            statement.close();
            
        } catch (Exception e) {
            System.err.println("error getting usser idempleado " + e.getMessage() + " Cause: " + e.getCause().toString());
            
        }
        return usu;
    }
    
    public static ArrayList<Usuario> getAllUsuariosActivosPorDepartamento(int idDepartamento) {
        ArrayList<Usuario> usuarios = new ArrayList<>();
        
        try {
            
            Connection conn = DbPoolHandler.connectionPool.getConnection();
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery("call getUsuarioPorDepartamento('" + idDepartamento + "')");
            while (rs.next()) {
                Usuario us = new Usuario();
                us.setIdEmpleado(rs.getString(1));
                us.setNombre(rs.getString(2));
                us.setPrimer_Apellido(rs.getString(3));
                us.setSegundo_Apellido(rs.getString(4));
                us.setDepartamento(rs.getInt(5));
                us.setIdPuesto(rs.getInt(6));
                us.setActivo(rs.getInt(7));
                us.setIdTarjeta(rs.getString(8));
                us.setFechaIngreso(rs.getDate(12));
                us.setId(rs.getInt(13));
                us.setJornada(rs.getInt(14));
                us.setCentroCosto(rs.getString(15));
                //us.setDiaLibre(data.CrudDias.getIdDiaLibre(usarioActual, us.getIdEmpleado()));
                us.setDiaLibre(rs.getInt("diaLibre"));
                us.setEmpresa(rs.getInt(16));
                usuarios.add(us);
                
            }
            DbPoolHandler.connectionPool.releaseConnection(conn);
            rs.close();
            statement.close();
            
            return usuarios;
        } catch (Exception e) {
            System.out.println("error in the query result " + e.getMessage());
            return null;
        }
        
    }
    
    public static Usuario getUsuarioPorIdEmpleado(String id) {
        Usuario us = null;
        
        try {
            
            Connection conn = DbPoolHandler.connectionPool.getConnection();
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery("call getUsuarioPorId('" + id + "')");
            while (rs.next()) {
                us = new Usuario();
                us.setIdEmpleado(rs.getString(1));
                us.setNombre(rs.getString(2));
                us.setPrimer_Apellido(rs.getString(3));
                us.setSegundo_Apellido(rs.getString(4));
                us.setDepartamento(rs.getInt(5));
                us.setIdPuesto(rs.getInt(6));
                us.setActivo(rs.getInt(7));
                us.setIdTarjeta(rs.getString(8));
                us.setFechaIngreso(rs.getDate(12));
                us.setId(rs.getInt(13));
                us.setJornada(rs.getInt(14));
                us.setCentroCosto(rs.getString(15));
                us.setEmpresa(rs.getInt(16));
                //us.setDiaLibre(data.CrudDias.getIdDiaLibre(usarioActual, us.getIdEmpleado()));

            }
            DbPoolHandler.connectionPool.releaseConnection(conn);
            rs.close();
            statement.close();
            return us;
        } catch (Exception e) {
            System.err.println("data.crudUsuario.getUsuarioPorIdEmpleado()");
            System.out.println("error in the query result " + e.getMessage());
            return null;
        }
    }
    
    public static ArrayList<Usuario> getAllUsuariosActivosPorDepartamentoMenosChoferes(int idDepartamento) {
        ArrayList<Usuario> usuarios = new ArrayList<>();
        
        try {
            
            Connection conn = DbPoolHandler.connectionPool.getConnection();
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery("call getUsuarioPorDepartamento('" + idDepartamento + "')");
            while (rs.next()) {
                
                Usuario us = new Usuario();
                us.setIdEmpleado(rs.getString(1));
                us.setNombre(rs.getString(2));
                us.setPrimer_Apellido(rs.getString(3));
                us.setSegundo_Apellido(rs.getString(4));
                us.setDepartamento(rs.getInt(5));
                us.setIdPuesto(rs.getInt(6));
                us.setActivo(rs.getInt(7));
                us.setIdTarjeta(rs.getString(8));
                us.setFechaIngreso(rs.getDate(12));
                us.setId(rs.getInt(13));
                us.setJornada(rs.getInt(14));
                us.setCentroCosto(rs.getString(15));
                us.setEmpresa(rs.getInt(16));
                us.setDiaLibre(rs.getInt("diaLibre"));
                us.setKeyEmpleado(rs.getString("KeyEmpleado"));
                usuarios.add(us);
                
            }
            DbPoolHandler.connectionPool.releaseConnection(conn);
            rs.close();
            statement.close();
            return usuarios;
        } catch (Exception e) {
            System.out.println("error in the query result " + e.getMessage());
            return null;
        }
        
    }
    
    public static ArrayList<Usuario> getAllEmpAdmin() {
        ArrayList<Usuario> usuarios = new ArrayList<>();
        
        try {
            
            Connection conn = DbPoolHandler.connectionPool.getConnection();
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery("call getEmpleadosAdministrativos()");
            while (rs.next()) {
                
                Usuario us = new Usuario();
                us.setIdEmpleado(rs.getString(1));
                us.setNombre(rs.getString(2));
                us.setPrimer_Apellido(rs.getString(3));
                us.setSegundo_Apellido(rs.getString(4));
                us.setDepartamento(rs.getInt(5));
                us.setIdPuesto(rs.getInt(6));
                us.setActivo(rs.getInt(7));
                us.setIdTarjeta(rs.getString(8));
                us.setFechaIngreso(rs.getDate(12));
                us.setId(rs.getInt(13));
                us.setJornada(rs.getInt(14));
                us.setCentroCosto(rs.getString(15));
                us.setEmpresa(rs.getInt(16));
                us.setDiaLibre(rs.getInt("diaLibre"));
                us.setKeyEmpleado(rs.getString("KeyEmpleado"));
                usuarios.add(us);
                
            }
            DbPoolHandler.connectionPool.releaseConnection(conn);
            rs.close();
            statement.close();
            return usuarios;
        } catch (Exception e) {
            System.out.println("error in the query result " + e.getMessage());
            return null;
        }
        
    }
    
}
