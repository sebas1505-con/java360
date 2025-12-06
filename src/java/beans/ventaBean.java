package beans;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;

import dao.VentaDAO;
import modelo.Venta;

@ManagedBean(name="ventaBean")
@SessionScoped
public class VentaBean implements Serializable {
    private static final long serialVersionUID = 1L;

    // --- PROPIEDADES QUE USA EL FORMULARIO ---
    private int cantProducto;
    private String metodoEnvio;
    private double totalVenta;
    private String metodoPago;
    private String direccionEnvio;
    private String telefonoContacto;
    private String observaciones;

    // --- GETTERS & SETTERS ---
    public int getCantProducto() { return cantProducto; }
    public void setCantProducto(int cantProducto) { this.cantProducto = cantProducto; }

    public String getMetodoEnvio() { return metodoEnvio; }
    public void setMetodoEnvio(String metodoEnvio) { this.metodoEnvio = metodoEnvio; }

    public double getTotalVenta() { return totalVenta; }
    public void setTotalVenta(double totalVenta) { this.totalVenta = totalVenta; }

    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }

    public String getDireccionEnvio() { return direccionEnvio; }
    public void setDireccionEnvio(String direccionEnvio) { this.direccionEnvio = direccionEnvio; }

    public String getTelefonoContacto() { return telefonoContacto; }
    public void setTelefonoContacto(String telefonoContacto) { this.telefonoContacto = telefonoContacto; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    // --- MÉTODO PARA GUARDAR LA VENTA ---
    public String guardarVenta() {
        try {
            // Crear objeto Venta con los datos del formulario
            Venta venta = new Venta();
            venta.setCantProducto(cantProducto);
            venta.setMetodoEnvio(metodoEnvio);
            venta.setTotalVenta(totalVenta);
            venta.setMetodoPago(metodoPago);
            venta.setDireccionEnvio(direccionEnvio);
            venta.setTelefonoContacto(telefonoContacto);
            venta.setObservaciones(observaciones);
            // Aquí deberías setear también idCliente si lo tienes en sesión

            // Guardar en la BD
            VentaDAO dao = new VentaDAO();
            boolean ok = dao.registrar(venta);

            if (ok) {
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Venta registrada correctamente"));

                // limpiar formulario
                cantProducto = 0;
                metodoEnvio = null;
                totalVenta = 0;
                metodoPago = null;
                direccionEnvio = null;
                telefonoContacto = null;
                observaciones = null;

                return "/confirmacion.xhtml?faces-redirect=true";
            } else {
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo registrar la venta"));
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", "Problema al guardar la venta"));
            return null;
        }
    }
}
