package beans;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dao.AdministradorDAO;
import modelo.Administrador;
import modelo.Pedido;
import modelo.Producto;
import modelo.Usuario;

@ManagedBean(name="adminBean")
@SessionScoped
public class AdminBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private Administrador administrador = new Administrador();   
    private Administrador administradorLogueado;                
    private String contrasenaConfirmacion;                       

    // --- NUEVAS PROPIEDADES PARA EL DASHBOARD ---
    private double ventasHoy;
    private int productosVendidosHoy;
    private int clientesNuevosHoy;
    private List<Integer> datosGraficaSemana;

    private List<Pedido> ultimosPedidos;
    private List<Producto> inventario;
    private List<Usuario> usuarios;

    // --- CONSTRUCTOR ---
    public AdminBean() {
        // Valores de prueba (luego los llenas con DAO)
        this.ventasHoy = 150000;
        this.productosVendidosHoy = 25;
        this.clientesNuevosHoy = 3;
        this.datosGraficaSemana = Arrays.asList(120000, 90000, 150000, 110000, 130000, 80000, 95000);

        this.ultimosPedidos = new ArrayList<>();
        this.inventario = new ArrayList<>();
        this.usuarios = new ArrayList<>();
    }

    // --- GETTERS & SETTERS ---
    public Administrador getAdministrador() { return administrador; }
    public void setAdministrador(Administrador administrador) { this.administrador = administrador; }

    public Administrador getAdministradorLogueado() { return administradorLogueado; }
    public void setAdministradorLogueado(Administrador administradorLogueado) { this.administradorLogueado = administradorLogueado; }

    public String getContrasenaConfirmacion() { return contrasenaConfirmacion; }
    public void setContrasenaConfirmacion(String contrasenaConfirmacion) { this.contrasenaConfirmacion = contrasenaConfirmacion; }

    public double getVentasHoy() { return ventasHoy; }
    public void setVentasHoy(double ventasHoy) { this.ventasHoy = ventasHoy; }

    public int getProductosVendidosHoy() { return productosVendidosHoy; }
    public void setProductosVendidosHoy(int productosVendidosHoy) { this.productosVendidosHoy = productosVendidosHoy; }

    public int getClientesNuevosHoy() { return clientesNuevosHoy; }
    public void setClientesNuevosHoy(int clientesNuevosHoy) { this.clientesNuevosHoy = clientesNuevosHoy; }

    public List<Integer> getDatosGraficaSemana() { return datosGraficaSemana; }
    public void setDatosGraficaSemana(List<Integer> datosGraficaSemana) { this.datosGraficaSemana = datosGraficaSemana; }

    public List<Pedido> getUltimosPedidos() { return ultimosPedidos; }
    public void setUltimosPedidos(List<Pedido> ultimosPedidos) { this.ultimosPedidos = ultimosPedidos; }

    public List<Producto> getInventario() { return inventario; }
    public void setInventario(List<Producto> inventario) { this.inventario = inventario; }

    public List<Usuario> getUsuarios() { return usuarios; }
    public void setUsuarios(List<Usuario> usuarios) { this.usuarios = usuarios; }

    // --- REGISTRAR ---
    public String registrar() {
        if (!administrador.getContraseña().equals(contrasenaConfirmacion)) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                                 "Error", "Las contraseñas no coinciden"));
            return null;
        }
        try {
            AdministradorDAO dao = new AdministradorDAO();
            boolean guardado = dao.registrar(administrador);

            if (guardado) {
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, 
                                     "Éxito", "Administrador registrado correctamente"));

                administrador = new Administrador();
                contrasenaConfirmacion = null;

                return "/login.xhtml?faces-redirect=true";
            } else {
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                                     "Error", "No se pudo registrar en la base de datos"));
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_FATAL, 
                                 "Error", "Ocurrió un problema en el registro"));
            return null;
        }
    }

    // --- LOGIN ---
    public String iniciarSesion() {
        try {
            AdministradorDAO dao = new AdministradorDAO();
            administradorLogueado = dao.login(administrador.getUsuario(), administrador.getContraseña());

            if (administradorLogueado != null) {
                return "/admin/dashboard.xhtml?faces-redirect=true";
            } else {
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                                     "Error", "Usuario o contraseña incorrectos"));
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_FATAL, 
                                 "Error", "Ocurrió un problema al iniciar sesión"));
            return null;
        }
    }

    // --- CERRAR SESIÓN ---
    public String cerrarSesion() {
        administradorLogueado = null;
        return "/login.xhtml?faces-redirect=true";
    }
}
