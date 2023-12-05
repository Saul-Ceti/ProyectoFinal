import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class ProcesadorDatosServidor extends UnicastRemoteObject implements ProcesadorDatos {
    private List<Cliente> clientes = new ArrayList<>();
    private long tiempoExecutor;

    protected ProcesadorDatosServidor() throws RemoteException {
        super();
    }

    @Override
    public void limpiarClientes() throws RemoteException {
        clientes.clear();
    }

    @Override
    public Cliente[] leerClientes(String filePath) throws RemoteException {
        List<Cliente> clientesUnicos = new ArrayList<>();

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
                    clientesUnicos.add(cliente);
                    clientes.add(cliente);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return clientesUnicos.toArray(new Cliente[0]);
    }

    @Override
    public Cliente[] generarCuentaClabeSecuencial() throws RemoteException {
        Secuencial secuencial = new Secuencial();
        return secuencial.generarCuentaClabe(clientes.toArray(new Cliente[0]));
    }

    @Override
    public Cliente[] generarCuentaClabeForkJoin() throws RemoteException {
        ForkJoin forkJoin = new ForkJoin();
        return forkJoin.generarCuentaClabe(clientes.toArray(new Cliente[0]));
    }

    @Override
    public Cliente[] generarCuentaClabeExecutorService() throws RemoteException {
        MetodoEjecutor executorService = new MetodoEjecutor();
        Cliente[] clientesProcesados = executorService.generarCuentaClabeParalelo(clientes.toArray(new Cliente[0]));
        tiempoExecutor = executorService.tiempoDeExecutor();
        return clientesProcesados;
    }

    @Override
    public long tiempoDeExecutor() throws RemoteException {
        return tiempoExecutor;
    }
}