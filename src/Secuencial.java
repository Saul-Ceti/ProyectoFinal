public class Secuencial {

    public Cliente[] generarCuentaClabe(Cliente[] clientes) {
        for (Cliente cliente : clientes) {
            // Extraer los datos necesarios
            String codigo = cliente.getCodigo();
            String nombre = cliente.getNombre();
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
        return clientes;
    }
}
