package modelo;

public class Repartidor {
    private int idRepartidor;
    private String repTelefono;
    private int idUsuario; // FK hacia usuario (opcional)
    private String tipoDeVehi;
    private String numPlaca;
    private String nombreRepar;
    private String correo;
    private String usuario;
    private String contrasena;
    private String rol;

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    public int getIdRepartidor() { return idRepartidor; }
    public void setIdRepartidor(int idRepartidor) { this.idRepartidor = idRepartidor; }

    public String getRepTelefono() { return repTelefono; }
    public void setRepTelefono(String repTelefono) { this.repTelefono = repTelefono; }

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public String getTipoDeVehi() { return tipoDeVehi; }
    public void setTipoDeVehi(String tipoDeVehi) { this.tipoDeVehi = tipoDeVehi; }

    public String getNumPlaca() { return numPlaca; }
    public void setNumPlaca(String numPlaca) { this.numPlaca = numPlaca; }

    public String getNombreRepar() { return nombreRepar; }
    public void setNombreRepar(String nombreRepar) { this.nombreRepar = nombreRepar; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }

    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }
}
