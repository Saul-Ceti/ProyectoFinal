import java.util.concurrent.RecursiveTask;
import java.util.concurrent.ForkJoinPool;

public class ForkJoin {

    private ForkJoinPool pool = new ForkJoinPool();

    public Cliente[] generarCuentaClabe(Cliente[] clientes) {
        // Crear una tarea ForkJoin que procese los clientes
        GenerarCuentaClabeTask task = new GenerarCuentaClabeTask(clientes, 0, clientes.length);
        // Invocar la tarea en el pool ForkJoin
        pool.invoke(task);
        // Devolver el array de clientes procesados
        return task.getResult();
    }

    public static class GenerarCuentaClabeTask extends RecursiveTask<Void> {
        private static final int THRESHOLD = 5;
        private Cliente[] clientes;
        private int start;
        private int end;

        GenerarCuentaClabeTask(Cliente[] clientes, int start, int end) {
            this.clientes = clientes;
            this.start = start;
            this.end = end;
        }

        @Override
        protected Void compute() {
            if (end - start <= THRESHOLD) {
                for (int i = start; i < end; i++) {
                     Cliente cliente = clientes[i];
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
            } else {
                int middle = (start + end) / 2;
                GenerarCuentaClabeTask leftTask = new GenerarCuentaClabeTask(clientes, start, middle);
                GenerarCuentaClabeTask rightTask = new GenerarCuentaClabeTask(clientes, middle, end);
                leftTask.fork();
                rightTask.fork();
                leftTask.join();
                rightTask.join();
            }
            return null;
        }

        // Método para obtener el resultado después de procesar los clientes
        public Cliente[] getResult() {
            return clientes;
        }
    }
}
