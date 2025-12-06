package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import modelo.Usuario;

public class LoginDAO {
    private Connection con;

    public LoginDAO() throws Exception {
        con = Conexion.conectar();
    }

    // ------------------ LOGIN UNIFICADO ------------------
    public Usuario login(String correo, String clave) {
        Usuario u = null;
        try {
            // Normalizar entrada
            String c = correo == null ? "" : correo.trim().toLowerCase();
            String k = clave == null ? "" : clave.trim();

            // 1) Buscar en tabla usuario
            PreparedStatement ps = con.prepareStatement(
                "SELECT pk_idusuario, nombre, usuCorreo, usuario, clave, usutelefono, direccion, fechaNacimiento, barrio, rol " +
                "FROM usuario WHERE LOWER(usuCorreo)=? AND clave=?"
            );
            ps.setString(1, c);
            ps.setString(2, k);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                u = new Usuario();
                u.setId(rs.getInt("pk_idusuario"));
                u.setNombre(rs.getString("nombre"));
                u.setUsuCorreo(rs.getString("usuCorreo"));
                u.setUsuario(rs.getString("usuario"));
                u.setClave(rs.getString("clave")); // columna correcta
                u.setUsuTelefono(rs.getString("usutelefono"));
                u.setDireccion(rs.getString("direccion"));
                u.setFechaNacimiento(rs.getDate("fechaNacimiento"));
                u.setBarrio(rs.getString("barrio"));
                u.setRol(rs.getString("rol")); // normalmente 'cliente'
                return u;
            }

            // 2) Buscar en tabla administrador
            ps = con.prepareStatement(
                "SELECT pk_idAdministrador, admCorreo, usuario, contraseña, telefono, codigo, rol " +
                "FROM administrador WHERE LOWER(admCorreo)=? AND contraseña=?"
            );
            ps.setString(1, c);
            ps.setString(2, k);
            rs = ps.executeQuery();
            if (rs.next()) {
                u = new Usuario(); // reutilizamos el modelo Usuario
                u.setId(rs.getInt("pk_idAdministrador"));
                u.setUsuCorreo(rs.getString("admCorreo"));
                u.setUsuario(rs.getString("usuario"));
                u.setClave(rs.getString("contraseña")); // columna correcta
                u.setUsuTelefono(rs.getString("telefono"));
                u.setBarrio(rs.getString("codigo")); // opcional
                u.setRol(rs.getString("rol")); // rol viene de la tabla
                return u;
            }

            // 3) Buscar en tabla repartidor
            ps = con.prepareStatement(
                "SELECT pk_idRepartidor, NombreRepar, Correo, Usuario, contrasena, repTelefono, tipodevehi, numplaca, rol " +
                "FROM repartidor WHERE LOWER(Correo)=? AND contrasena=?"
            );
            ps.setString(1, c);
            ps.setString(2, k);
            rs = ps.executeQuery();
            if (rs.next()) {
                u = new Usuario(); 
                u.setId(rs.getInt("pk_idRepartidor"));
                u.setNombre(rs.getString("NombreRepar"));
                u.setUsuCorreo(rs.getString("Correo"));
                u.setUsuario(rs.getString("Usuario"));
                u.setClave(rs.getString("contrasena")); // columna correcta
                u.setUsuTelefono(rs.getString("repTelefono"));
                u.setDireccion(rs.getString("tipodevehi"));
                u.setBarrio(rs.getString("numplaca"));   
                u.setRol(rs.getString("rol")); // aquí será 'repartidor'
                return u;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return u;
    }
}
