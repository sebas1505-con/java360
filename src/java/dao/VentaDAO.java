package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import modelo.Cliente;
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

    // -----------------------------
    // REGISTRAR VENTA (tu método)
    // -----------------------------
    public void registrarVenta(Venta venta) throws Exception {
        String sql = "INSERT INTO venta (fk_id_detalle_venta, cantProducto, metodoEnvio, totalVenta, metodo_de_pago, id_cliente, direccionEnvio, telefonoContacto, observaciones, Fecha_de_venta, estado) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, NOW(), ?)";

        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, venta.getIdDetalleVenta()); // si no tienes detalle, poner 0 o ajustar
        ps.setInt(2, venta.getCantProducto());
        ps.setString(3, venta.getMetodoEnvio());
        ps.setDouble(4, venta.getTotalVenta());
        ps.setString(5, venta.getMetodoPago());

        // --- Asegurar ID cliente (si no viene el objeto, usar el campo idCliente) ---
        Integer idCli = null;
        if (venta.getCliente() != null) {
            idCli = venta.getCliente().getIdCliente();
        } else if (venta.getIdCliente() != 0) {
            idCli = venta.getIdCliente();
        }
        if (idCli == null) {
            throw new IllegalArgumentException("El id_cliente es obligatorio en la venta");
        }
        ps.setInt(6, idCli);

        ps.setString(7, venta.getDireccionEnvio());
        ps.setString(8, venta.getTelefonoContacto());
        ps.setString(9, venta.getObservaciones());

        // Si no estableces estado antes, por defecto 'Pendiente'
        String estado = (venta.getEstado() != null && !venta.getEstado().isEmpty()) ? venta.getEstado() : "Pendiente";
        ps.setString(10, estado);

        ps.executeUpdate();
        ps.close();
    }

    // -----------------------------
    // Registrar venta versión booleana (tu método)
    // -----------------------------
    public boolean registrar(Venta venta) {
        try {
            registrarVenta(venta);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // -----------------------------
    // Listar todas las ventas (tu método, con correcciones)
    // -----------------------------
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

            // CORRECCIÓN: nombre de columna es id_cliente (no idCliente)
            v.setIdCliente(rs.getInt("id_cliente"));

            v.setDireccionEnvio(rs.getString("direccionEnvio"));
            v.setTelefonoContacto(rs.getString("telefonoContacto"));
            v.setObservaciones(rs.getString("observaciones"));
            v.setEstado(rs.getString("estado"));
            v.setFechaVenta(rs.getTimestamp("Fecha_de_venta"));

            // Opcional: setear objeto Cliente básico para vistas
            Cliente c = new Cliente();
            c.setIdCliente(v.getIdCliente());
            v.setCliente(c);

            lista.add(v);
        }

        rs.close();
        st.close();
        return lista;
    }

    // -----------------------------
    // NUEVO: Listar ventas por estado (para pendientes e historial)
    // -----------------------------
    public List<Venta> listarPorEstado(String estado) {
        List<Venta> lista = new ArrayList<>();
        String sql = "SELECT v.*, c.nombre, c.direccion, c.telefono " +
                     "FROM venta v " +
                     "LEFT JOIN cliente c ON v.id_cliente = c.idCliente " +
                     "WHERE v.estado = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, estado);
            ResultSet rs = ps.executeQuery();
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
                v.setEstado(rs.getString("estado"));
                v.setFechaVenta(rs.getTimestamp("Fecha_de_venta"));

                // Cliente para mostrar en panel
                Cliente c = new Cliente();
                c.setIdCliente(rs.getInt("id_cliente"));
                c.setNombre(rs.getString("nombre"));
                c.setDireccion(rs.getString("direccion"));
                c.setTelefono(rs.getString("telefono"));
                v.setCliente(c);

                lista.add(v);
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    // -----------------------------
    // NUEVO: Actualizar estado de venta (para tomar/entregar)
    // -----------------------------
    public boolean actualizarEstado(int idVenta, String nuevoEstado) {
        String sql = "UPDATE venta SET estado=? WHERE pk_idVenta=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nuevoEstado);
            ps.setInt(2, idVenta);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
