import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.List;
import java.util.ArrayList;
import javax.swing.JLabel;

public class MetodoEjecutor {
    public long tiempoExecutor;
    public long tiempoDeExecutor(){
        return tiempoExecutor;
    }

    public long medirTiempoEjecucion(Runnable runnable) {
        long startTime = System.nanoTime();
        runnable.run();
        long endTime = System.nanoTime();

        return endTime - startTime;
    }

    public Cliente[] generarCuentaClabeParalelo(Cliente[] clientes) {
        int numThreads = Runtime.getRuntime().availableProcessors(); // Obtener el número de núcleos disponibles
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);

        List<Cliente> clientesList = new ArrayList<>();
        for (Cliente cliente : clientes) {
            clientesList.add(cliente);
        }

        tiempoExecutor = medirTiempoEjecucion(() -> {
            // Dividir la lista de clientes en subgrupos para que cada hilo maneje una parte
            int chunkSize = clientesList.size() / numThreads;
            for (int i = 0; i < numThreads; i++) {
                int startIndex = i * chunkSize;
                int endIndex = (i == numThreads - 1) ? clientesList.size() : startIndex + chunkSize;
                List<Cliente> subClientes = clientesList.subList(startIndex, endIndex);
                executor.execute(new GenerarCuentaClabeTask(subClientes));
            }
        });

        executor.shutdown();
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return clientes;
    }

    private class GenerarCuentaClabeTask implements Runnable {

        private List<Cliente> clientes;

        GenerarCuentaClabeTask(List<Cliente> clientes) {
            this.clientes = clientes;
        }

        @Override
        public void run() {
            for (Cliente cliente : clientes) {
                // Extraer los datos necesarios
                String codigo = cliente.getCodigo();
                int cdgEstadoNacimiento = cliente.getCdgEstadoNacimiento();
                int edad = cliente.getEdad();

                // Crear la cuenta CLABE
                String cuentaClabe = "8527";

                // Obtener los 2 últimos dígitos del código
                String codigoLastTwoDigits = codigo.substring(codigo.length() - 2);
                cuentaClabe += codigoLastTwoDigits;

                cuentaClabe += "145"; // Agregar "145"

                // Formatear el cdgEstadoNacimiento a 2 dígitos
                String cdgEstadoNacimientoStr = String.format("%02d", cdgEstadoNacimiento);
                cuentaClabe += cdgEstadoNacimientoStr;

                cuentaClabe += "6"; // Agregar "6"

                // Formatear la edad a 2 dígitos
                String edadStr = String.format("%02d", edad);
                cuentaClabe += edadStr;

                // Agregar el primer dígito del código
                cuentaClabe += codigo.substring(0, 1);

                // Verificar si la cuenta CLABE tiene 17 caracteres
                if (cuentaClabe.length() == 15) {
                    // Asignar la cuenta CLABE al cliente
                    cliente.setCuentaClabe(cuentaClabe);
                }
            }
        }
    }
}
