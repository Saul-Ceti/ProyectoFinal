import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class ServidorRMI {
    public static void main(String[] args) {
        try {
            // Crear un registro RMI en el puerto 1099
            LocateRegistry.createRegistry(1099);

            // Crear una instancia del objeto del servidor RMI
            ProcesadorDatosServidor procesadorDatosServidor = new ProcesadorDatosServidor();

            // Clases necesarias para debuggear
            System.setProperty("java.rmi.server.logCalls", "true");
            System.setProperty("java.rmi.server.logLevel", "VERBOSE");
            System.setProperty("java.rmi.server.logSource", "SERVER_CODEBASE");
            System.getProperties().put("java.rmi.server.logCalls", "true");
            System.getProperties().put("java.rmi.server.logLevel", "VERBOSE");

            // Enlazar el objeto al registro RMI
            Naming.rebind("rmi://localhost:1099/procesar", procesadorDatosServidor);

            System.out.println("Servidor RMI preparado");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
