package beans;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import modelo.Producto;

@ManagedBean(name="productoBean")
@SessionScoped
public class ProductoBean implements Serializable {

    private List<Producto> listaProd;
    private Producto producto;  
    private String imagen;      

    public ProductoBean() {
        listaProd = new ArrayList<>();

        // Datos de prueba para evitar la lista vacía
        Producto p1 = new Producto();
        p1.setCod("P001");
        p1.setNombre("Balón");
        p1.setDescr("Balón profesional");
        p1.setExist(10);
        p1.setPrecio(50.0);
        p1.setFoto("img/ball.jpg");
        listaProd.add(p1);

        producto = new Producto(); 
    }

    public List<Producto> getListaProd() { return listaProd; }
    public void setListaProd(List<Producto> listaProd) { this.listaProd = listaProd; }

    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }

    public String getImagen() { return imagen; }
    public void setImagen(String imagen) { this.imagen = imagen; }

    // -------- MÉTODO PARA GUARDAR ----------
    public void guardar() {
        listaProd.add(producto);
        producto = new Producto();
    }

    // -------- MÉTODO PARA CARGAR PRODUCTO A EDITAR ----------
    public String cargar(String cod) {

        for (Producto p : listaProd) {
            if (p.getCod().equals(cod)) {
                producto = p;  // ← aquí ya NO queda vacío
                return "editar?faces-redirect=true";
            }
        }

        // Si no lo encuentra evita IndexOutOfBounds
        producto = new Producto();
        return "index?faces-redirect=true";
    }

    // -------- MÉTODO ACTUALIZAR ----------
    public String actualizar() {
        // Si deseas guardar la imagen
        if (imagen != null && !imagen.isEmpty()) {
            producto.setFoto("uploads/" + imagen);
        }
        return "index?faces-redirect=true";
    }
}
