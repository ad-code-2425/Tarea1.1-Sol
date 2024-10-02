/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ad.cdm.persistencia;

import ad.cdm.model.Persona;
import ad.cdm.model.exceptions.NotFoundPersonaException;
import ad.cdm.tarea.Tarea11Sol;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mfernandez
 */
public class RandomAccessPersistencia implements IPersistencia {

    private static final int LONG_BYTES_PERSONA = 35 + Persona.MAX_LENGTH_NOMBRE * 2;
 
  

 



    @Override
    public void escribirPersonas(ArrayList<Persona> personas, String ruta) {
        long longitudBytes = 0;

        if (personas != null) {
            try (
                     RandomAccessFile raf = new RandomAccessFile(ruta, "rw");) {

                longitudBytes = raf.length();
                raf.seek(longitudBytes);
                for (Persona persona : personas) {

                    raf.writeLong(persona.getId());
                    StringBuilder sb = new StringBuilder(persona.getDni());
                    sb.setLength(Persona.MAX_LENGTH_DNI);
                    raf.writeChars(sb.toString());

                    sb = new StringBuilder(persona.getNombre());
                    sb.setLength(Persona.MAX_LENGTH_NOMBRE);
                    raf.writeChars(sb.toString());

                    raf.writeInt(persona.getEdad());
                    raf.writeFloat(persona.getSalario());

                    raf.writeBoolean(persona.isBorrado());

                }

            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
                System.out.println("Se ha producido una excepción: " + ex.getMessage());
            } catch (IOException ex) {
                ex.printStackTrace();
                System.out.println("Se ha producido una excepción: " + ex.getMessage());
            }
        }

    }
@Override
    public ArrayList<Persona> leerTodo(String ruta) {
        long id;
        String dni = "", nombre = "";
        int edad;
        float salario;
        StringBuilder sb =null;
        Persona persona = null;
        boolean borrado = false;
        ArrayList<Persona> personas = new ArrayList<>();
        try (
                 RandomAccessFile raf = new RandomAccessFile(ruta, "r");) {

            do {
                id = raf.readLong();
                sb = new StringBuilder();
                for (int i = 0; i <= (Persona.MAX_LENGTH_DNI -1); i++) {
                    sb.append(raf.readChar());
                }

                dni = sb.toString();

                sb = new StringBuilder();
                for (int i = 0; i < Persona.MAX_LENGTH_NOMBRE; i++) {
                    sb.append(raf.readChar());
                }

                nombre = sb.toString();

                edad = raf.readInt();
                salario = raf.readFloat();

                borrado = raf.readBoolean();

                persona = new Persona(id, dni, edad, salario, nombre);
                persona.setBorrado(borrado);

                personas.add(persona);

            } while (raf.getFilePointer() < raf.length());

        } catch (IOException ex) {
            ex.printStackTrace();
            System.out.println("Se ha producido una excepción: " + ex.getMessage());
        }
        return personas;

    }

    /**
     * Obtén un obxecto Persona do arquivo sinalado en ruta
     * @param posicion indica a posición que ocupa cada persoa comezando no 0: o cero devolverá a primera persoa, o 1 a segunda persoa, etc.
     * @param ruta a ruta ao arquivo
     * @return o obxecto Persoa atopado nesa posición 
     * @throws NotFoundPersonaException en caso de error
     */
    public Persona leerPersona(int posicion, String ruta) throws NotFoundPersonaException{
        long id = 0;
        String dni = "", nombre = "";
        int edad = 0;
        float salario = 0;
        StringBuilder sb = new StringBuilder();
        Persona persona = null;
        boolean borrado = false;

        try (
                 RandomAccessFile raf = new RandomAccessFile(ruta, "r");) {

            raf.seek(converToBytePosition(posicion));
            id = raf.readLong();
            for (int i = 0; i <= (Persona.MAX_LENGTH_DNI -1); i++) {
                sb.append(raf.readChar());
            }

            dni = sb.toString();
            sb = new StringBuilder();
            for (int i = 0; i < Persona.MAX_LENGTH_NOMBRE; i++) {
                sb.append(raf.readChar());
            }

            nombre = sb.toString();

            edad = raf.readInt();
            salario = raf.readFloat();

            borrado = raf.readBoolean();

            persona = new Persona(id, dni, edad, salario, nombre);
            persona.setBorrado(borrado);

        
        } catch (IOException ex) {
             Logger.getLogger(Tarea11Sol.class.getName()).log(Level.SEVERE, "Se ha producido una excepción leyendo la ruta "+ ruta + " posicion: "+ posicion, ex);
            throw new NotFoundPersonaException(posicion, "Se ha producido una excepción", ex);
        }  catch (Exception ex) {
            
            throw new NotFoundPersonaException(posicion, "Se ha producido una excepción", ex);
        }

        return persona;
    }

    private long converToBytePosition(int posicion) {
        if (posicion == 0) {
            return posicion;
        } else {
            return LONG_BYTES_PERSONA * (posicion - 1);
        }
    }

  
}
