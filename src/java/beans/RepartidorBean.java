package beans;

import dao.RepartidorDAO;
import modelo.Repartidor;
import modelo.Venta; 

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.List;

@ManagedBean(name="repartidorBean")
@SessionScoped
public class RepartidorBean implements Serializable {
    private static final long serialVersionUID = 1L;

    // --- PROPIEDADES DEL FORMULARIO ---
    private String nombre;
    private String correo;
    private String usuario;
    private String contrasena;
    private String contrasenaConfirmacion;
    private String placa;
    private String telefono;
    private String vehiculo;
    private String fecha; // opcional

    // --- LISTA DE PEDIDOS ---
    private List<Venta> ventasPendientes;

    // --- GETTERS & SETTERS ---
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }

    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }

    public String getContrasenaConfirmacion() { return contrasenaConfirmacion; }
    public void setContrasenaConfirmacion(String contrasenaConfirmacion) { this.contrasenaConfirmacion = contrasenaConfirmacion; }

    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getVehiculo() { return vehiculo; }
    public void setVehiculo(String vehiculo) { this.vehiculo = vehiculo; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public List<Venta> getVentasPendientes() { return ventasPendientes; }
    public void setVentasPendientes(List<Venta> ventasPendientes) { this.ventasPendientes = ventasPendientes; }

    // --- MÉTODO PARA REGISTRAR ---
    public String registrar() {
        try {
            if (contrasena == null || !contrasena.equals(contrasenaConfirmacion)) {
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Las contraseñas no coinciden"));
                return null;
            }

            Repartidor r = new Repartidor();
            r.setNombreRepar(nombre);
            r.setCorreo(correo);
            r.setUsuario(usuario);
            r.setContrasena(contrasena);
            r.setNumPlaca(placa);
            r.setRepTelefono(telefono);
            r.setTipoDeVehi(vehiculo);
            r.setRol("repartidor");

            RepartidorDAO dao = new RepartidorDAO();
            dao.registrarRepartidor(r);

            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Repartidor registrado correctamente"));

            return "/panelRepartidor.xhtml?faces-redirect=true";
        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", "No se pudo registrar el repartidor"));
            return null;
        }
    }

    // --- MÉTODO PARA LOGIN ---
    public String login() {
        try {
            RepartidorDAO dao = new RepartidorDAO();
            Repartidor r = dao.validarRepartidor(usuario, contrasena);
            if (r != null) {
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("repartidor", r);
                // cargar pedidos pendientes al iniciar sesión
                ventasPendientes = dao.listarPedidosPendientes();
                return "/panelRepartidor.xhtml?faces-redirect=true";
            } else {
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Usuario o contraseña incorrectos"));
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", "No se pudo iniciar sesión"));
            return null;
        }
    }

    // --- MÉTODO PARA TOMAR PEDIDO ---
    public void tomarPedido(int idVenta) {
        try {
            RepartidorDAO dao = new RepartidorDAO();
            dao.asignarPedido(usuario, idVenta);
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Pedido tomado correctamente"));
            // refrescar lista
            ventasPendientes = dao.listarPedidosPendientes();
        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo tomar el pedido"));
        }
    }

    // --- MÉTODO PARA CERRAR SESIÓN ---
    public String logout() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "/login.xhtml?faces-redirect=true";
    }
}
