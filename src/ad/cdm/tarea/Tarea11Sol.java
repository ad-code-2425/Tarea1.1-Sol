/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package ad.cdm.tarea;

import ad.cdm.model.Persona;
import ad.cdm.model.exceptions.NotFoundPersonaException;
import ad.cdm.persistencia.RandomAccessPersistencia;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.LogManager;

/**
 *
 * @author mfernandez
 */
public class Tarea11Sol {

    public static final Path PERSONAS_ORIGEN_PATH_RAN = Paths.get("src", "docs", "origen_ran.dat");

    static final Logger logger = Logger.getLogger(Tarea11Sol.class.getName());

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        try {
            setLogger();
            
            int posicion = leerPosicion();

            RandomAccessPersistencia rap = new RandomAccessPersistencia();
            Persona found = rap.leerPersona(posicion, PERSONAS_ORIGEN_PATH_RAN.toString());
            if (found != null) {
                System.out.println("Se ha encontrado en la posición " + posicion + found);
            }

        } catch (NotFoundPersonaException | IOException ex) {
           logger.log(Level.SEVERE, "Ocorreu unha excepción", ex);
        }

    }

    private static int leerPosicion() throws IOException {
        String numeroString ="";
        boolean valida = false;
        int posicion = -1;
        do {
            System.out.println("Introduzca la posición de la persona que quiere leer (del 0 al 5)");

            try {
                // Enter data using BufferReader
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(System.in));

                // Reading data using readLine
                numeroString = reader.readLine();

                posicion = Integer.parseInt(numeroString);

                if (posicion >= 0 && posicion < 6) {
                    valida = true;
                } else {
                    System.out.println("O número introducido non é correcto");
                }

            } catch (NumberFormatException ex) {
                System.out.println("Inténteo de novo");
                 logger.log(Level.SEVERE, "Ocorreu unha excepción convertendo: " + numeroString, ex);
            } catch (IOException ex) {
              logger.log(Level.SEVERE, "Ocorreu unha excepción", ex);
                throw ex;
            }
        } while (!valida);

        return posicion;
    }

    private static void setLogger() {
        try {
            LogManager.getLogManager().readConfiguration(new FileInputStream(Paths.get("src", "mylogging.properties").toString()));
        } catch (SecurityException | IOException e1) {
            e1.printStackTrace();
        }

    }
}
