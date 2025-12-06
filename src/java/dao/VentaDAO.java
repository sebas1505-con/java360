package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import modelo.Venta;

public class VentaDAO {

    private Connection con;

    public VentaDAO() {
        try {
            con = Conexion.conectar(); 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

public void registrarVenta(Venta venta) throws Exception {
    String sql = "INSERT INTO venta (fk_id_detalle_venta, cantProducto, metodoEnvio, totalVenta, metodo_de_pago, id_cliente, direccionEnvio, telefonoContacto, observaciones, Fecha_de_venta) "
               + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, NOW())";
    PreparedStatement ps = con.prepareStatement(sql);
    ps.setInt(1, venta.getIdDetalleVenta());
    ps.setInt(2, venta.getCantProducto());
    ps.setString(3, venta.getMetodoEnvio());
    ps.setDouble(4, venta.getTotalVenta());
    ps.setString(5, venta.getMetodoPago());
    ps.setInt(6, venta.getIdCliente());
    ps.setString(7, venta.getDireccionEnvio());
    ps.setString(8, venta.getTelefonoContacto());
    ps.setString(9, venta.getObservaciones());
    ps.executeUpdate();
}


    // Listar ventas
    public List<Venta> listarVentas() throws Exception {
        List<Venta> lista = new ArrayList<>();
        String sql = "SELECT * FROM venta";
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(sql);

        while (rs.next()) {
    Venta v = new Venta();
    v.setIdVenta(rs.getInt("pk_idVenta"));
    v.setIdDetalleVenta(rs.getInt("fk_id_detalle_venta"));
    v.setCantProducto(rs.getInt("cantProducto"));
    v.setMetodoEnvio(rs.getString("metodoEnvio"));
    v.setTotalVenta(rs.getDouble("totalVenta"));
    v.setMetodoPago(rs.getString("metodo_de_pago"));
    v.setIdCliente(rs.getInt("id_cliente"));
    v.setDireccionEnvio(rs.getString("direccionEnvio"));
    v.setTelefonoContacto(rs.getString("telefonoContacto"));
    v.setObservaciones(rs.getString("observaciones"));
    v.setFechaVenta(rs.getTimestamp("Fecha_de_venta"));
    lista.add(v);
}

        return lista;
    }

    // Contar ventas
    public int contarVentas() throws Exception {
        String sql = "SELECT COUNT(*) FROM venta";
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(sql);
        if (rs.next()) {
            return rs.getInt(1);
        }
        return 0;
    }

    // Registrar (versión booleana)
    public boolean registrar(Venta venta) {
        try {
            registrarVenta(venta);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void asignarAPedido(int idPedido, String usuario) throws Exception {
    String sql = "UPDATE pedido SET repartidorUsuario = ?, estado = ? WHERE idPedido = ?";
    PreparedStatement ps = null;
    try {
        ps = con.prepareStatement(sql);
        ps.setString(1, usuario);        // el usuario del repartidor
        ps.setString(2, "EN_CURSO");     // nuevo estado del pedido
        ps.setInt(3, idPedido);          // id del pedido
        ps.executeUpdate();
    } finally {
        if (ps != null) ps.close();
 
    }

    }
    
}
