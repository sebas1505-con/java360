package beans;

import dao.RepartidorDAO;
import dao.VentaDAO;
import modelo.Repartidor;
import modelo.Venta;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.view.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ManagedBean(name="repartidorBean")
@ViewScoped
public class RepartidorBean implements Serializable {
    private static final long serialVersionUID = 1L;

    // --- PROPIEDADES QUE USA EL FORMULARIO ---
    private String nombre;
    private String correo;
    private String usuario;
    private String contrasena;
    private String contrasenaConfirmacion;
    private String placa;
    private String telefono;
    private String vehiculo;
    private String fecha; // opcional

    // --- LISTA DE PEDIDOS PENDIENTES ---
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

    

    // --- MÉTODO PARA CERRAR SESIÓN ---
    public String logout() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "/login.xhtml?faces-redirect=true";
    }
}
