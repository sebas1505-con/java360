package modelo;

import java.util.Date;

public class Producto {
    private String cod;
    private String nombre;
    private String descr;
    private int exist;
    private double precio;
    private Date fven;
    private String foto;

public String getFoto() { return foto; }

public void setFoto(String foto) { this.foto = foto; }

    public String getCod() { return cod; }
    public void setCod(String cod) { this.cod = cod; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescr() { return descr; }
    public void setDescr(String descr) { this.descr = descr; }

    public int getExist() { return exist; }
    public void setExist(int exist) { this.exist = exist; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public Date getFven() { return fven; }
    public void setFven(Date fven) { this.fven = fven; }
}
