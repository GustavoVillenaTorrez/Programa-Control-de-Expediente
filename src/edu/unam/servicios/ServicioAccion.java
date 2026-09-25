package edu.unam.servicios;

import edu.unam.repositorios.*;

import java.time.LocalDate;
import java.util.List;
import edu.unam.modelo.Accion;
import edu.unam.modelo.Expediente;

public class ServicioAccion {
    private Repositorio repositorio;

    public ServicioAccion(Repositorio p) {
        this.repositorio = p;
    }
     //ACCION
    //Listar
    public List<Accion> listarAccion() {
        return this.repositorio.buscarTodos(Accion.class);
    }
    
    //Buscar
    public Accion buscarAccion(Long idAccion) {
        return this.repositorio.buscar(Accion.class, idAccion);
    }
    //Agregar
    public Long agregarAccion(String descripcion, LocalDate fecha, Expediente expediente) {
        if (descripcion.trim().length() == 0 || fecha  == null || expediente == null) {
            throw new IllegalArgumentException("Faltan datos");
        }
        this.repositorio.iniciarTransaccion();
        Accion accion = new Accion(descripcion.toUpperCase().trim(), fecha , expediente);
        this.repositorio.insertar(accion);
        this.repositorio.confirmarTransaccion();
        this.repositorio.refrescar(accion);
        return accion.getIdAccion();
    }
    
    // Cambiar valor devuelto
    public void editarAccion(Long idAccion, String descripcion, LocalDate fecha, Expediente expediente) {
        if (descripcion.trim().length() == 0 || fecha  == null || expediente == null) {
            throw new IllegalArgumentException("Faltan datos para editar la Accion");
        }
        this.repositorio.iniciarTransaccion();
        Accion accion = this.repositorio.buscar(Accion.class, idAccion);
        if (accion != null) {
            accion.setDescripcion(descripcion);
            accion.setFecha(fecha);
            if (! accion.getExpediente().equals(expediente)) {
                accion.getExpediente().getAccion().remove(accion);
                accion.setExpediente(expediente);
                expediente.getAccion().add(accion);
            }
            this.repositorio.modificar(accion);
            this.repositorio.confirmarTransaccion();
            this.repositorio.refrescar(accion);
        } else {
            this.repositorio.descartarTransaccion();
        }
    }
    

    //Eliminar 
    public int eliminarAccion(Long idAccion) {
        this.repositorio.iniciarTransaccion();
        Accion accion = this.repositorio.buscar(Accion.class, idAccion);
        // como se soluciona??
        if (accion != null ) {
            this.repositorio.eliminar(accion);
            this.repositorio.confirmarTransaccion();
            return 0;
        } else {
            this.repositorio.descartarTransaccion();
            return 1;
        }
    }

    
}
