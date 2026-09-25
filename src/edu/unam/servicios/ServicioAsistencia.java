package edu.unam.servicios;

import edu.unam.repositorios.*;

import java.time.LocalDate;
import java.util.List;
import edu.unam.modelo.Asistencia;
import edu.unam.modelo.Involucrado;
import edu.unam.modelo.Reunion;

public class ServicioAsistencia {
    private Repositorio repositorio;

    public ServicioAsistencia(Repositorio p) {
        this.repositorio = p;
    }
    //Asistencia
    //Listar
    public List<Asistencia> listarAsistencias() {
        return this.repositorio.buscarTodos(Asistencia.class);
    }
    
    //Buscar
    public Asistencia buscarAsistencia(Long idAsistencia) {
        return this.repositorio.buscar(Asistencia.class, idAsistencia);
    }
    //Agregar
    public Long agregarAsistencia(Boolean presencia, LocalDate fecha, Reunion reunion, Involucrado involucrado) {
        if (fecha == null || involucrado == null || reunion == null) {
            throw new IllegalArgumentException("Faltan datos");
        }
        
        this.repositorio.iniciarTransaccion();
        Asistencia asistencia = new Asistencia(presencia, fecha, reunion,involucrado); // Asegúrate de que el constructor de Asistencia reciba Reunion como parámetro
        this.repositorio.insertar(asistencia);
        this.repositorio.confirmarTransaccion();
        
        
        return asistencia.getIdAsistencia();
    }
    
    // Cambiar valor devuelto
    public void editarAsistencia(Long idAsistencia, Boolean presencia,LocalDate fecha, Reunion reunion,Involucrado involucrado) {
        if (fecha==null||involucrado==null|| reunion == null) {
            throw new IllegalArgumentException("Faltan datos");
        }
        this.repositorio.iniciarTransaccion();
        Asistencia asistencia = this.repositorio.buscar(Asistencia.class, idAsistencia);
        if (asistencia != null ) {
            asistencia.setPresencia(presencia);
            asistencia.setFecha(fecha);
            if (! asistencia.getReunion().equals(reunion)) {
                asistencia.getReunion().getAsistencia().remove(asistencia);
                asistencia.setReunion(reunion);
                reunion.getAsistencia().add(asistencia);
            }
            if (! asistencia.getInvolucrado().equals(involucrado)) {
                asistencia.getInvolucrado().getAsistencia().remove(asistencia);
                asistencia.setInvolucrado(involucrado);
                involucrado.getAsistencia().add(asistencia);
            }
            
            this.repositorio.modificar(asistencia);
            this.repositorio.confirmarTransaccion();
        } else {
            this.repositorio.descartarTransaccion();
        }
    }
    //Eliminar 
    /*public int eliminarAsistencia(Long idAsistencia) {
        this.repositorio.iniciarTransaccion();
        Asistencia asistencia = this.repositorio.buscar(Asistencia.class, idAsistencia);
        if (asistencia != null ) {
            this.repositorio.eliminar(asistencia);
            this.repositorio.confirmarTransaccion();
            return 0;
        } else {
            this.repositorio.descartarTransaccion();
            return 1;
        }
    }*/
    public int eliminarAsistencia(Long idAsistencia) {
        try {
            this.repositorio.iniciarTransaccion();
            
            Asistencia asistencia = this.repositorio.buscar(Asistencia.class, idAsistencia);
            
            if (asistencia != null) {
                Involucrado involucrado = asistencia.getInvolucrado();
                if (involucrado != null) {
                    involucrado.getAsistencia().remove(asistencia);
                    this.repositorio.refrescar(involucrado); // Actualiza la entidad involucrado
                }
                
                this.repositorio.eliminar(asistencia);
                this.repositorio.confirmarTransaccion();
                
                return 0;
            } else {
                this.repositorio.descartarTransaccion();
                return 1;
            }
        } catch (Exception e) {
            this.repositorio.descartarTransaccion();
            e.printStackTrace();
            return 2;
        }
    }
    

}
