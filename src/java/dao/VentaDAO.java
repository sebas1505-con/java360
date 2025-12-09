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
    ps.setInt(1, venta.getIdDetalleVenta()); // si no tienes detalle, poner 0 o ajustar
    ps.setInt(2, venta.getCantProducto());
    ps.setString(3, venta.getMetodoEnvio());
    ps.setDouble(4, venta.getTotalVenta());
    ps.setString(5, venta.getMetodoPago());

    // --- CORRECCIÓN: usar objeto Cliente ---
    ps.setInt(6, venta.getCliente().getIdCliente());

    ps.setString(7, venta.getDireccionEnvio());
    ps.setString(8, venta.getTelefonoContacto());
    ps.setString(9, venta.getObservaciones());
    ps.executeUpdate();
    ps.close();
}


    // Registrar venta versión booleana
    public boolean registrar(Venta venta) {
        try {
            registrarVenta(venta);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Listar todas las ventas
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
            v.setIdCliente(rs.getInt("idCliente"));
            v.setDireccionEnvio(rs.getString("direccionEnvio"));
            v.setTelefonoContacto(rs.getString("telefonoContacto"));
            v.setObservaciones(rs.getString("observaciones"));
            v.setEstado(rs.getString("estado"));
            v.setFechaVenta(rs.getTimestamp("Fecha_de_venta"));
            lista.add(v);
        }

        rs.close();
        st.close();
        return lista;
    }

}
