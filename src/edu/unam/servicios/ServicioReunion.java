package edu.unam.servicios;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.ArrayList;


import edu.unam.modelo.Asistencia;
import edu.unam.modelo.Expediente;
import edu.unam.modelo.Involucrado;
import edu.unam.modelo.Minuta;
import edu.unam.modelo.Reunion;
import edu.unam.servicios.ServicioAsistencia;
import edu.unam.servicios.ServicioMinuta;

import edu.unam.repositorios.Repositorio;

public class ServicioReunion {
    private Repositorio repositorio;

    public ServicioReunion(Repositorio p) {
        this.repositorio = p;
    }


    //Reunion
    //Listar
    public List<Reunion> listarReuniones() {
        return this.repositorio.buscarTodos(Reunion.class);
    }
    
    
    //Buscar
    public Reunion buscarReunion(Long idReunion) {
        return this.repositorio.buscar(Reunion.class, idReunion);
    }
    /*//Agregar
    public void agregarReunion(LocalDate fecha,String ordendelDia, String estadoReunion, LocalTime horaInicio, LocalTime horaFin, Set<Expediente> expediente,Set<Involucrado> involucrado) {
        if ( fecha == null || ordendelDia == null|| estadoReunion == null || expediente == null || involucrado == null ){
            throw new IllegalArgumentException("Faltan datos");
        }
        this.repositorio.iniciarTransaccion();
        Reunion reunion = new Reunion( fecha, ordendelDia, estadoReunion,  horaInicio, horaFin,expediente,involucrado);
        this.repositorio.insertar(reunion);
        this.repositorio.confirmarTransaccion();
    }*/
    

    public void editarReunion(Long idReunion, LocalDate fecha, String estadoReunion, LocalTime horaInicio, LocalTime horaFin, Set<Expediente> expedientes, Set<Involucrado> involucrados) {
        if (fecha == null || estadoReunion == null || expedientes == null || involucrados == null) {
            throw new IllegalArgumentException("Faltan datos");
        }
    
        this.repositorio.iniciarTransaccion();
        Reunion reunion = this.repositorio.buscar(Reunion.class, idReunion);
    
        if (reunion != null) {
            reunion.setFecha(fecha);
            reunion.setEstadoReunion(estadoReunion);
            reunion.setHoraInicio(horaInicio);
            reunion.setHoraFin(horaFin);
    
            // Limpiar asociaciones anteriores con expedientes
            for (Expediente exp : reunion.getExpediente()) {
                exp.getReunion().remove(reunion);
            }
    
            // Limpiar asociaciones anteriores con involucrados
            for (Involucrado inv : reunion.getInvolucrado()) {
                inv.getReunion().remove(reunion);
            }
    
            // Establecer nuevas asociaciones con expedientes
            for (Expediente exp : expedientes) {
                exp.getReunion().add(reunion);
            }
    
            // Establecer nuevas asociaciones con involucrados
            for (Involucrado inv : involucrados) {
                inv.getReunion().add(reunion);
            }
    
            reunion.setExpediente(expedientes);
            reunion.setInvolucrado(involucrados);
    
            this.repositorio.modificar(reunion);
            this.repositorio.confirmarTransaccion();
        } else {
            this.repositorio.descartarTransaccion();
        }
    }
    //Eliminar 
    /*public int eliminarReunion(Long idReunion) {
        Reunion reunion = this.repositorio.buscar(Reunion.class, idReunion);
        this.repositorio.iniciarTransaccion();
        if (reunion != null ) {
            this.repositorio.eliminar(reunion);
            this.repositorio.confirmarTransaccion();
            return 0;
        } else {
            this.repositorio.descartarTransaccion();
            return 1;
        }
    }*/
    public int eliminarReunion(Long idReunion) {
        this.repositorio.iniciarTransaccion();
        try {
            // Buscar la reunión por ID
            Reunion reunion = this.repositorio.buscar(Reunion.class, idReunion);
    
            if (reunion != null) {
                // Eliminar las asistencias asociadas
                List<Asistencia> asistencias = new ArrayList<>(reunion.getAsistencia()); // Obtener la lista de asistencias
                for (Asistencia asistencia : asistencias) {
                    // Eliminar la asistencia de la base de datos directamente usando el repositorio
                    this.repositorio.eliminar(asistencia);
                }
    
                // Eliminar las minutas asociadas
                List<Minuta> minutas = new ArrayList<>(reunion.getMinuta()); // Obtener la lista de minutas
                for (Minuta minuta : minutas) {
                    // Eliminar la minuta de la base de datos directamente usando el repositorio
                    this.repositorio.eliminar(minuta);
                }
    
                // Limpiar asociaciones de la reunión en los expedientes
                List<Expediente> expedientes = new ArrayList<>(reunion.getExpediente()); // Obtener la lista de expedientes
                for (Expediente expediente : expedientes) {
                    expediente.getReunion().remove(reunion); // Limpiar la asociación
                    this.repositorio.refrescar(expediente); // Actualizar la entidad expediente
                }
    
                // Eliminar la reunión
                this.repositorio.eliminar(reunion);
                this.repositorio.confirmarTransaccion();
                return 0;
            } else {
                // Descartar la transacción si no se encontró la reunión
                this.repositorio.descartarTransaccion();
                return 2;
            }
        } catch (Exception e) {
            // Manejar la excepción y descartar la transacción
            this.repositorio.descartarTransaccion();
            e.printStackTrace();
            return 3;
        }
    }
    // Agregar reunión con asociación de involucrados
    public void asociarInvolucradosAReunion(Long idReunion, Set<Involucrado> involucrados) {
        if (idReunion == null || involucrados == null) {
            throw new IllegalArgumentException("Faltan datos");
        }
        
        this.repositorio.iniciarTransaccion();
        
        Reunion reunion = this.repositorio.buscar(Reunion.class, idReunion);
        if (reunion != null) {
            involucrados.forEach(involucrado -> reunion.agregarInvolucrado(involucrado));
            this.repositorio.modificar(reunion);
        }
        
        this.repositorio.confirmarTransaccion();
        this.repositorio.refrescar(reunion);
    }
    
    // Agregar reunión con asociación de expedientes
    public void asociarExpedientesAReunion(Long idReunion, Set<Expediente> expedientes) {
        if (idReunion == null || expedientes == null) {
            throw new IllegalArgumentException("Faltan datos");
        }
        
        this.repositorio.iniciarTransaccion();
        
        Reunion reunion = this.repositorio.buscar(Reunion.class, idReunion);
        if (reunion != null) {
            expedientes.forEach(expediente -> reunion.agregarExpediente(expediente));
            this.repositorio.modificar(reunion);
        }
        
        this.repositorio.confirmarTransaccion();
        
    }
    public Long agregarReunion(LocalDate fecha, String estadoReunion, LocalTime horaInicio, LocalTime horaFin, Set<Expediente> expedientes, Set<Involucrado> involucrados) {
        if (fecha == null || estadoReunion == null  || expedientes == null || involucrados == null) {
            throw new IllegalArgumentException("Faltan datos");
        }
    
        this.repositorio.iniciarTransaccion();
        Reunion reunion = new Reunion(fecha, estadoReunion, horaInicio, horaFin);
    
        // Configurar relaciones bidireccionales con expedientes
        for (Expediente expediente : expedientes) {
            expediente.agregarReunion(reunion);
            
        }
    
        // Configurar relaciones bidireccionales con involucrados
        for (Involucrado involucrado : involucrados) {
            involucrado.agregarReunion(reunion);
            
        }
    
        this.repositorio.insertar(reunion);
        this.repositorio.confirmarTransaccion();
        this.repositorio.refrescar(reunion);
        return reunion.getIdReunion();
    }
    public List<Reunion> buscarReunionesPorFecha(LocalDate fecha) {
        // Obtener todas las reuniones
        List<Reunion> todasLasReuniones = repositorio.buscarTodos(Reunion.class);

        // Filtrar las reuniones por fecha
        List<Reunion> reunionesPorFecha = todasLasReuniones.stream()
                .filter(reunion -> reunion.getFecha().equals(fecha))
                .collect(Collectors.toList());

        return reunionesPorFecha;
    }
}
