package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import modelo.Cliente;
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

    // ================================
    // REGISTRAR REPARTIDOR
    // ================================
    public void registrarRepartidor(Repartidor r) throws Exception {
        String sql = "INSERT INTO repartidor (repTelefono, tipodevehi, numplaca, NombreRepar, Correo, Usuario, contrasena, rol) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, r.getRepTelefono());
        ps.setString(2, r.getTipoDeVehi());
        ps.setString(3, r.getNumPlaca());
        ps.setString(4, r.getNombreRepar());
        ps.setString(5, r.getCorreo());
        ps.setString(6, r.getUsuario());
        ps.setString(7, r.getContrasena());
        ps.setString(8, "repartidor");
        ps.executeUpdate();
        ps.close();
    }

    // ================================
    // VALIDAR LOGIN DEL REPARTIDOR
    // ================================
    public Repartidor validarRepartidor(String usuario, String contrasena) throws Exception {
        String sql = "SELECT * FROM repartidor WHERE Usuario=? AND contrasena=?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, usuario);
        ps.setString(2, contrasena);

        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            Repartidor r = new Repartidor();
            r.setIdRepartidor(rs.getInt("pk_idRepartidor"));
            r.setUsuario(rs.getString("Usuario"));
            r.setContrasena(rs.getString("contrasena"));
            r.setRol(rs.getString("rol"));
            rs.close();
            ps.close();
            return r;
        }

        rs.close();
        ps.close();
        return null;
    }

    // ================================
    // LISTAR PEDIDOS PENDIENTES
    // ================================
    public List<Venta> listarPedidosPendientes() throws Exception {
        List<Venta> lista = new ArrayList<>();

        String sql = "SELECT v.pk_idVenta, v.direccionEnvio, v.telefonoContacto, "
                   + "c.idCliente, c.nombre, c.direccion, c.telefono "
                   + "FROM venta v "
                   + "JOIN cliente c ON v.idCliente = c.idCliente "
                   + "WHERE v.estado = 'Pendiente'";

        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Venta v = new Venta();
            v.setIdVenta(rs.getInt("pk_idVenta"));
            v.setDireccionEnvio(rs.getString("direccionEnvio"));
            v.setTelefonoContacto(rs.getString("telefonoContacto"));

            Cliente c = new Cliente();
            c.setIdCliente(rs.getInt("id_cliente"));
            c.setNombre(rs.getString("nombre"));
            c.setDireccion(rs.getString("direccion"));
            c.setTelefono(rs.getString("telefono"));

            v.setCliente(c);

            lista.add(v);
        }

        rs.close();
        ps.close();
        return lista;
    }

    // ================================
    // ASIGNAR PEDIDO A REPARTIDOR
    // ================================
    public void asignarPedido(String usuarioRepartidor, int idVenta) throws Exception {
        String sql = "UPDATE venta SET repartidor=?, estado='ASIGNADO' WHERE pk_idVenta=?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, usuarioRepartidor);
        ps.setInt(2, idVenta);
        ps.executeUpdate();
        ps.close();
    }
}
