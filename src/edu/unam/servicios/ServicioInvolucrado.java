package edu.unam.servicios;

import edu.unam.repositorios.*;

import java.util.ArrayList;
import java.util.List;

import edu.unam.modelo.Asistencia;
import edu.unam.modelo.Expediente;
import edu.unam.modelo.Involucrado;

public class ServicioInvolucrado {
    private Repositorio repositorio;

    public ServicioInvolucrado(Repositorio p) {
        this.repositorio = p;
    }
    //Involucrados
    //Listar
    public List<Involucrado> listarInvolucrados() {
        return this.repositorio.buscarTodos(Involucrado.class);
    }
    
    //Buscar
    public Involucrado buscaroInvolucrado(Long idInvolucrados) {
        return this.repositorio.buscar(Involucrado.class, idInvolucrados);
    }
    public List<Involucrado> obtenerMiembrosConsejo() {
        // Obtener todos los involucrados
        List<Involucrado> todosInvolucrados = repositorio.buscarTodos(Involucrado.class);
        
        // Filtrar los involucrados para obtener solo los miembros del consejo
        List<Involucrado> miembrosConsejo = new ArrayList<>();
        for (Involucrado involucrado : todosInvolucrados) {
            if (involucrado.isMiembroConsejo()) {
                miembrosConsejo.add(involucrado);
            }
        }
        return miembrosConsejo;
    }

    //Listas Verificacion de un Atributo ya existente
    public Boolean involucradoExiste(int dniInvolucrado) {
        List<Involucrado> involucrados = listarInvolucrados();
        for (Involucrado involucrado : involucrados) {
            if (involucrado.getDni() == dniInvolucrado) {
                return true; // Si el número de expediente coincide, devuelve true
            }
        }
        return false; // Si no se encuentra ningún expediente con el número dado, devuelve false
    }
    //Listas buscapor numero de un Atributo ya existente
    public List<Involucrado> buscarInvolucradoPorDni(int dniInvolucrados) {
        List<Involucrado> involucradosEncontrados = new ArrayList<>();
        List<Involucrado> involucrados = listarInvolucrados();
        for (Involucrado involucrado : involucrados) {
            if (involucrado.getDni() == dniInvolucrados) {
                involucradosEncontrados.add(involucrado);
            }
        }
        return involucradosEncontrados;
    }
    //Listas busca por nombre de un Atributo ya existente
    public List<Involucrado> buscarInvolucradoPorNombre(String nombreInvolucrado) {
        List<Involucrado> involucradosEncontrados = new ArrayList<>();
        List<Involucrado> involucrados = listarInvolucrados();
        for (Involucrado involucrado : involucrados) {
            if (involucrado.getNombre().equals(nombreInvolucrado)) {
                involucradosEncontrados.add(involucrado);
            }
        }
        return involucradosEncontrados;
    }
    //Agregar
    public Long agregarInvolucrado(String nombre, int dni,Long telefono, String email, String rol, Boolean esmiembroConsejo, Expediente expedientes) {
        if ( nombre.trim().length() == 0 || dni==0 || telefono== 0 ||  email.trim().length() == 0 || rol.trim().length() == 0 || esmiembroConsejo== null|| expedientes == null) {
            throw new IllegalArgumentException("Faltan datos");
        }
        this.repositorio.iniciarTransaccion();
        Involucrado involucrado = new Involucrado(nombre, dni, telefono, email, rol, esmiembroConsejo,  expedientes);
        this.repositorio.insertar(involucrado);
        this.repositorio.confirmarTransaccion();
        this.repositorio.refrescar(involucrado);
        return involucrado.getIdInvolucrado();
    }
    
    // Cambiar valor devuelto
    public void editarInvolucrado(Long idInvolucrado, String nombre, int dni,Long telefono, String email, String rol, Boolean esmiembroConsejo, Expediente expedientes) {
        if (  nombre.trim().length() == 0 || dni==0 || telefono== 0 ||  email.trim().length() == 0 || rol.trim().length() == 0 || esmiembroConsejo== null|| expedientes == null) {
            throw new IllegalArgumentException("Faltan datos");
        }
        this.repositorio.iniciarTransaccion();
        Involucrado involucrado = this.repositorio.buscar(Involucrado.class, idInvolucrado);
        if (involucrado != null ) {
            involucrado.setNombre(nombre);
            involucrado.setDni(dni);
            involucrado.setTelefono(telefono);
            involucrado.setEmail(email);
            involucrado.setRol(rol);
            involucrado.setEsmiembroConsejo(esmiembroConsejo);
            if (! involucrado.getExpediente().equals(expedientes)) {
                involucrado.getExpediente().getInvolucrado().remove(involucrado);
                involucrado.setExpediente(expedientes);
                expedientes.getInvolucrado().add(involucrado);
            }
            this.repositorio.modificar(involucrado);
            this.repositorio.confirmarTransaccion();
            this.repositorio.refrescar(involucrado);
        } else {
            this.repositorio.descartarTransaccion();
        }
    }
   /*  //Eliminar 
   public int eliminarInvolucrados(Long idInvolucrados) {
    try {
        this.repositorio.iniciarTransaccion();
        
        Involucrado involucrado = this.repositorio.buscar(Involucrado.class, idInvolucrados);
        
        if (involucrado != null) {
            if (involucrado.getAsistencia().isEmpty()) {
                involucrado.getReunion().clear();
                this.repositorio.eliminar(involucrado);
                this.repositorio.confirmarTransaccion();
                
               
                
                return 0;
            } else {
                this.repositorio.descartarTransaccion();
                return 1;
            }
        } else {
            this.repositorio.descartarTransaccion();
            return 2;
        }
    } catch (Exception e) {
        this.repositorio.descartarTransaccion();
        e.printStackTrace();
        return 3;
    }
}*/
public int eliminarInvolucrados(Long idInvolucrados) {
    // Iniciar la transacción
    this.repositorio.iniciarTransaccion();

    try {
        // Buscar el involucrado por ID
        Involucrado involucrado = this.repositorio.buscar(Involucrado.class, idInvolucrados);
        
        if (involucrado != null) {
            // Obtener y eliminar todas las asistencias asociadas
            List<Asistencia> asistencias = new ArrayList<>(involucrado.getAsistencia());
            for (Asistencia asistencia : asistencias) {
                // Eliminar la asistencia de la base de datos
                this.repositorio.eliminar(asistencia);
            }
            
            // Limpiar asociaciones de reuniones si es necesario
            involucrado.getReunion().clear();
            
            // Eliminar el involucrado
            this.repositorio.eliminar(involucrado);
            
            // Confirmar la transacción
            this.repositorio.confirmarTransaccion();
            
            // Retornar 0 para indicar éxito
            return 0;
        } else {
            // Descartar la transacción si no se encontró el involucrado
            this.repositorio.descartarTransaccion();
            
            // Retornar 2 para indicar que el involucrado no fue encontrado
            return 2;
        }
    } catch (Exception e) {
        // Manejar la excepción y descartar la transacción
        this.repositorio.descartarTransaccion();
        e.printStackTrace();
        // Retornar código de error o registrar el error
        return 3;
    }
}
    

}
