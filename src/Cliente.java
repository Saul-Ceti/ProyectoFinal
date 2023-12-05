import java.io.Serializable;

public class Cliente implements Serializable {
    private String codigo;
    private String nombre;
    private int cdgEstadoNacimiento;
    private int edad;
    private String cuentaClabe;

    public Cliente(String codigo, String nombre, int cdgEstadoNacimiento, int edad) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.cdgEstadoNacimiento = cdgEstadoNacimiento;
        this.edad = edad;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCdgEstadoNacimiento() {
        return cdgEstadoNacimiento;
    }

    public void setCdgEstadoNacimiento(int cdgEstadoNacimiento) {
        this.cdgEstadoNacimiento = cdgEstadoNacimiento;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getCuentaClabe() {
        return cuentaClabe;
    }

    public void setCuentaClabe(String cuentaClabe) {
        this.cuentaClabe = cuentaClabe;
    }
}