package dao;

import java.sql.*;
import java.util.*;
import modelo.Asignacion;

public class AsignacionDAO {
    private Connection con;

    public AsignacionDAO(Connection con) {
        this.con = con;
    }

    public void asignarVenta(int idVenta, int idRepartidor) throws SQLException {
        String sql = "INSERT INTO asignacion (fk_idVenta, fk_idRepartidor, estado) VALUES (?, ?, 'pendiente')";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, idVenta);
        ps.setInt(2, idRepartidor);
        ps.executeUpdate();
    }

    public List<Asignacion> listarPorRepartidor(int idRepartidor) throws SQLException {
        List<Asignacion> lista = new ArrayList<>();
        String sql = "SELECT * FROM asignacion WHERE fk_idRepartidor=? AND estado='pendiente'";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, idRepartidor);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Asignacion a = new Asignacion();
            a.setIdAsignacion(rs.getInt("pk_idAsignacion"));
            a.setIdVenta(rs.getInt("fk_idVenta"));
            a.setIdRepartidor(rs.getInt("fk_idRepartidor"));
            a.setEstado(rs.getString("estado"));
            lista.add(a);
        }
        return lista;
    }

    public void actualizarEstado(int idAsignacion, String nuevoEstado) throws SQLException {
        String sql = "UPDATE asignacion SET estado=? WHERE pk_idAsignacion=?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, nuevoEstado);
        ps.setInt(2, idAsignacion);
        ps.executeUpdate();
    }
}
