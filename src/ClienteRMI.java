import java.rmi.Naming;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ClienteRMI extends JFrame {
    //Variables necesarias para el programa
    Cliente[] clientes;

    public ClienteRMI() {
        //Parámetros para el JFrame o la ventana pricipal
        setTitle("Generaciónde cuentas CLABE para transferencias SPEI");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);
        getContentPane().setLayout(null);
        ////////////////////////////////////////////////////////////////////////

        //Campos de visualización de datos
        //Datos originales
        JTextArea originalData = new JTextArea();
        JScrollPane original = new JScrollPane(originalData);
        original.setBounds(50, 50, 850, 200);

        //Conteo de datos
        JLabel cantidadClientes = new JLabel("Esperando...");
        cantidadClientes.setBounds(450, 265, 100, 20);

        //Datos procesados
        JTextArea processData = new JTextArea();
        JScrollPane process = new JScrollPane(processData);
        process.setBounds(50, 300, 850, 200);

        //Conteo de datos procesados
        JLabel cantidadClientesProcesados = new JLabel("Esperando...");
        cantidadClientesProcesados.setBounds(450, 515, 100, 20);
        ////////////////////////////////////////////////////////////////////////

        //Botones para el archivo
        //Subir archivo crudo
        JButton btnSubir = new JButton("Subir archivo");
        btnSubir.setBounds(950, 50, 200, 50);

        //Descargar archivo procesado
        JButton btnDescargar = new JButton("Descargar archivo");
        btnDescargar.setBounds(950, 110, 200, 50);
        ////////////////////////////////////////////////////////////////////////

        //Botones para procesar los datos y sus etiquetas
        //Botón Secuencial
        JButton btnSecuencial = new JButton("Secuencial");
        btnSecuencial.setBounds(950, 300, 200, 50);
        JLabel timeSecuencial = new JLabel("Esperando");
        timeSecuencial.setBounds(950, 355, 200, 10);

        //Botón ForkJoin
        JButton btnForkJoin = new JButton("ForkJoin");
        btnForkJoin.setBounds(950, 370, 200, 50);
        JLabel timeForkJoin = new JLabel("Esperando");
        timeForkJoin.setBounds(950, 425, 250, 10);

        //Botón ExecutorService
        JButton btnExecutorService = new JButton("ExecutorService");
        btnExecutorService.setBounds(950, 440, 200, 50);
        JLabel timeExecutorService = new JLabel("Esperando");
        timeExecutorService.setBounds(950, 495, 250, 10);
        ////////////////////////////////////////////////////////////////////////

        //Botón para limpiar
        JButton btnLimpiar = new JButton("Limpiar");
        btnLimpiar.setBounds(950, 170, 200, 50);
        ////////////////////////////////////////////////////////////////////////

        //Agrega los componentes al frame
        getContentPane().add(original);
        getContentPane().add(cantidadClientes);
        getContentPane().add(process);
        getContentPane().add(cantidadClientesProcesados);
        getContentPane().add(btnSecuencial);
        getContentPane().add(btnForkJoin);
        getContentPane().add(btnExecutorService);
        getContentPane().add(btnSubir);
        getContentPane().add(btnLimpiar);
        getContentPane().add(timeSecuencial);
        getContentPane().add(timeForkJoin);
        getContentPane().add(timeExecutorService);
        ////////////////////////////////////////////////////////////////////////

        ////////////////////////////////////////////////////////////////////////
        ////////////////////////ACCIONES DE BOTONES/////////////////////////////
        ////////////////////////////////////////////////////////////////////////
        //Limpiar
        btnLimpiar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                originalData.setText("");
                processData.setText("");
                cantidadClientes.setText("0");
                cantidadClientesProcesados.setText("0");
                timeSecuencial.setText("0.0ms");
                timeForkJoin.setText("0.0ms");
                timeExecutorService.setText("0.0ms");

                // Limpiar el arreglo de clientes
                try {
                    ProcesadorDatos miInterfazRMI = (ProcesadorDatos) Naming.lookup("rmi://localhost:1099/procesar");
                    miInterfazRMI.limpiarClientes();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                clientes = null;
            }
        });
        ////////////////////////////////////////////////////////////////////////
        // Subir archivo
        btnSubir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                processData.setText("");

                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(null);

                if (result == JFileChooser.APPROVE_OPTION) {
                    // El usuario seleccionó un archivo
                    java.io.File file = fileChooser.getSelectedFile();
                    String filePath = file.getAbsolutePath();

                    try {
                        ProcesadorDatos miInterfazRMI = (ProcesadorDatos) Naming.lookup("rmi://localhost:1099/procesar");
                        clientes = miInterfazRMI.leerClientes(filePath);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                    cantidadClientes.setText(Integer.toString(clientes.length));

                    imprimirClientes(clientes, originalData);
                } else {
                    // El usuario canceló la selección del archivo
                    JOptionPane.showMessageDialog(null, "Selección de archivo cancelada.", "Información", JOptionPane.INFORMATION_MESSAGE);
                }

            }
        });
        ////////////////////////////////////////////////////////////////////////
        // Proceso secuencial
        btnSecuencial.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (clientes == null) {
                    JOptionPane.showMessageDialog(null, "Primero sube información para ser procesada.", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    processData.setText("");

                    final Cliente[][] clientesProcesados = {null};

                    long tiempo = medirTiempoEjecucion(() -> {
                        try {
                            ProcesadorDatos miInterfazRMI = (ProcesadorDatos) Naming.lookup("rmi://localhost:1099/procesar");
                            // Guardar los clientes procesados
                            clientesProcesados[0] = miInterfazRMI.generarCuentaClabeSecuencial();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    });

                    // Imprimir los clientes procesados
                    cantidadClientesProcesados.setText(Integer.toString(clientesProcesados[0].length));

                    // Imprimir los clientes procesados
                    imprimirClientes(clientesProcesados[0], processData);

                    String tiempoFormateado = formatoTiempo(tiempo);
                    timeSecuencial.setText("Tiempo: " + tiempoFormateado + " ms:ns");
                }
            }
        });
        ////////////////////////////////////////////////////////////////////////
        // ForkJoin
        btnForkJoin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (clientes == null) {
                    JOptionPane.showMessageDialog(null, "Primero sube información para ser procesada.", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    processData.setText("");

                    Cliente[][] clientesProcesados = {null};

                    long tiempo = medirTiempoEjecucion(() -> {
                        try {
                            ProcesadorDatos miInterfazRMI = (ProcesadorDatos) Naming.lookup("rmi://localhost:1099/procesar");
                            // Guardar los clientes procesados
                            clientesProcesados[0] = miInterfazRMI.generarCuentaClabeForkJoin();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    });

                    cantidadClientesProcesados.setText(Integer.toString(clientesProcesados[0].length));
                    imprimirClientes(clientesProcesados[0], processData);

                    String tiempoFormateado = formatoTiempo(tiempo);
                    timeForkJoin.setText("Tiempo: " + tiempoFormateado + " ms:ns");
                }
            }
        });
        ////////////////////////////////////////////////////////////////////////
        // ExecutorService
        btnExecutorService.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (clientes == null) {
                    JOptionPane.showMessageDialog(null, "Primero sube información para ser procesada.", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    processData.setText("");

                    Cliente[][] clientesProcesados = {null};
                    final long tiempoFinal[] = {0};

                    long tiempo = medirTiempoEjecucion(() -> {
                        try {
                            ProcesadorDatos miInterfazRMI = (ProcesadorDatos) Naming.lookup("rmi://localhost:1099/procesar");
                            clientesProcesados[0] = miInterfazRMI.generarCuentaClabeExecutorService();
                            tiempoFinal[0] = miInterfazRMI.tiempoDeExecutor();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    });

                    cantidadClientesProcesados.setText(Integer.toString(clientesProcesados[0].length));
                    imprimirClientes(clientesProcesados[0], processData);

                    String tiempoFormateado = formatoTiempo(tiempoFinal[0]);
                    timeExecutorService.setText("Tiempo: " + tiempoFormateado + " ms:ns");
                }
            }
        });
        ////////////////////////////////////////////////////////////////////////
        setVisible(true);
    }

    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    public static void main(String[] args) {
        try {
            ProcesadorDatos miObjetoRMI = (ProcesadorDatos) Naming.lookup("rmi://localhost:1099/procesar");
            ClienteRMI proyectoApp = new ClienteRMI();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void imprimirClientes(Cliente[] clientes, JTextArea area) {
        // Iterar a través del arreglo de deportistas
        for (Cliente cliente : clientes) {
            if (cliente.getCuentaClabe() != null) {
                area.append("Nombre: " + cliente.getNombre() + ", ");
                area.append("Cuenta CLABE: " + cliente.getCuentaClabe() + "\n");
            } else {
                // Obtén la información del deportista y agrega al JTextArea
                area.append("Código de usuario: " + cliente.getCodigo() + "\n");
                area.append("Nombre: " + cliente.getNombre() + "\n");
                area.append("Código de estado de nacimiento: " + cliente.getCdgEstadoNacimiento() + "\n");
                area.append("Edad: " + cliente.getEdad() + "\n");
                area.append("\n"); // Agregar una línea en blanco entre clientes
            }
        }
    }

    public long medirTiempoEjecucion(Runnable runnable) {
        long startTime = System.nanoTime();
        runnable.run();
        long endTime = System.nanoTime();

        return endTime - startTime;
    }

    public String formatoTiempo(long tiempoNanosegundos) {
        long milliseconds = tiempoNanosegundos / 1_000_000;
        long nanoseconds = tiempoNanosegundos % 1_000_000;

        return String.format("%d:%06d", milliseconds, nanoseconds);
    }
}