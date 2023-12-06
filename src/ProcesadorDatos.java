import java.io.File;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ProcesadorDatos extends Remote {
    void limpiarClientes() throws RemoteException;
    Cliente[] leerClientes(byte[] fileContent) throws RemoteException;
    Cliente[] generarCuentaClabeForkJoin() throws RemoteException;
    Cliente[] generarCuentaClabeExecutorService() throws RemoteException;
    Cliente[] generarCuentaClabeSecuencial() throws RemoteException;
    long tiempoDeExecutor() throws RemoteException;
}