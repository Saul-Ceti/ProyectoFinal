import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LeerArchivo {
    private String filePath;

    public LeerArchivo(String filePath) {
        this.filePath = filePath;
    }

    public Cliente[] leerClientes() {
        List<Cliente> clientes = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    String codigo = parts[0];
                    String nombre = parts[1];
                    int cdgEstNacimiento = Integer.parseInt(parts[2]);
                    int edad = Integer.parseInt(parts[3]);

                    Cliente cliente = new Cliente(codigo, nombre, cdgEstNacimiento, edad);
                    clientes.add(cliente);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return clientes.toArray(new Cliente[0]);
    }
}
