package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import modelo.Repartidor;
import modelo.Venta;

public class RepartidorDAO {
    private Connection con;

    public RepartidorDAO() {
        try {
            con = Conexion.conectar();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Registrar repartidor
    public void registrarRepartidor(Repartidor r) throws Exception {
        String sql = "INSERT INTO repartidor (repTelefono, tipodevehi, numplaca, NombreRepar, Correo, Usuario, contrasena, rol) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, r.getRepTelefono());
            ps.setString(2, r.getTipoDeVehi());
            ps.setString(3, r.getNumPlaca());
            ps.setString(4, r.getNombreRepar());
            ps.setString(5, r.getCorreo());
            ps.setString(6, r.getUsuario());
            ps.setString(7, r.getContrasena());
            ps.setString(8, "repartidor"); // siempre guardamos rol repartidor
            ps.executeUpdate();
        } finally {
            if (ps != null) ps.close();
        }
    }

    // Listar repartidores
    public List<Repartidor> listarRepartidores() throws Exception {
        List<Repartidor> lista = new ArrayList<>();
        String sql = "SELECT * FROM repartidor";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Repartidor r = new Repartidor();
                r.setIdRepartidor(rs.getInt("pk_idRepartidor"));
                r.setRepTelefono(rs.getString("repTelefono"));
                r.setTipoDeVehi(rs.getString("tipodevehi"));
                r.setNumPlaca(rs.getString("numplaca"));
                r.setNombreRepar(rs.getString("NombreRepar"));
                r.setCorreo(rs.getString("Correo"));
                r.setUsuario(rs.getString("Usuario"));
                r.setContrasena(rs.getString("contrasena"));
                r.setRol(rs.getString("rol")); // nuevo campo
                lista.add(r);
            }
        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
        }
        return lista;
    }
    
    public Repartidor validarRepartidor(String usuario, String contrasena) throws Exception {
    String sql = "SELECT * FROM repartidor WHERE Usuario=? AND contrsena=?";
    PreparedStatement ps = con.prepareStatement(sql);
    ps.setString(1, usuario);
    ps.setString(2, contrasena);
    ResultSet rs = ps.executeQuery();
    if (rs.next()) {
        Repartidor r = new Repartidor();
        r.setIdRepartidor(rs.getInt("pk_idRepartidor"));
        r.setUsuario(rs.getString("Usuario"));
        r.setContrasena(rs.getString("contrsena")); // nombre real de la columna
        r.setRol(rs.getString("rol"));
        return r;
    }
    return null;
}
public List<Venta> listarPedidosPendientes() throws Exception {
    List<Venta> lista = new ArrayList<>();
    String sql = "SELECT v.id, c.nombre, c.direccion, c.telefono " +
                 "FROM venta v " +
                 "JOIN cliente c ON v.cliente_id = c.id " +
                 "WHERE v.estado = 'PENDIENTE'";
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
        ps = con.prepareStatement(sql);
        rs = ps.executeQuery();
        while (rs.next()) {
            Venta v = new Venta();
            v.setId(rs.getInt("id"));
            v.getCliente().setNombre(rs.getString("nombre"));
            v.getCliente().setDireccion(rs.getString("direccion"));
            v.getCliente().setTelefono(rs.getString("telefono"));
            lista.add(v);
        }
    } finally {
        if (rs != null) rs.close();
        if (ps != null) ps.close();
    }
    return lista;
}
public void asignarPedido(String usuarioRepartidor, int idVenta) throws Exception {
    String sql = "UPDATE venta SET repartidor=?, estado='ASIGNADO' WHERE id=?";
    PreparedStatement ps = null;
    try {
        ps = con.prepareStatement(sql);
        ps.setString(1, usuarioRepartidor);
        ps.setInt(2, idVenta);
        ps.executeUpdate();
    } finally {
        if (ps != null) ps.close();
    }
}

}
