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
                    // Lógica de generación de cuenta CLABE, sin cambios
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
