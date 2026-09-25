package edu.unam.servicios;

import edu.unam.repositorios.*;

import java.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

import edu.unam.modelo.Accion;
import edu.unam.modelo.Expediente;
import edu.unam.modelo.Involucrado;
import edu.unam.modelo.Reunion;

public class ServivcioExpediente {
    private Repositorio repositorio;

    public ServivcioExpediente(Repositorio p) {
        this.repositorio = p;
    }
    //Expedientes
    //Listas
    public List<Expediente> listarExpedientes() {
        var expedientesRepositorio = this.repositorio.buscarTodos(Expediente.class);
        var listado = new ArrayList<Expediente>();
        for (var expediente : expedientesRepositorio) {
            if (expediente.isEstadoExpediente()) {
                listado.add(expediente);
            }
        }
        return listado;
    }
    //Listas Verificacion de un Atributo ya existente
    public boolean expedienteExiste(Long numeroExpediente) {
        List<Expediente> expedientes = listarExpedientes();
        for (Expediente expediente : expedientes) {
            if (expediente.getNroExpediente().equals(numeroExpediente)) {
                return true; // Si el número de expediente coincide, devuelve true
            }
        }
        return false; // Si no se encuentra ningún expediente con el número dado, devuelve false
    }
    //Listas buscapor numero de un Atributo ya existente
    public List<Expediente> buscarExpedientePorNumero(Long numeroExpediente) {
        List<Expediente> expedientesEncontrados = new ArrayList<>();
        List<Expediente> expedientes = listarExpedientes();
        for (Expediente expediente : expedientes) {
            if (expediente.getNroExpediente().equals(numeroExpediente)) {
                expedientesEncontrados.add(expediente);
            }
        }
        return expedientesEncontrados;
    }
    //Buscar
    public Expediente buscarExpediente(Long idExpediente) {
        return this.repositorio.buscar(Expediente.class, idExpediente);
    }
    //BuscarTodos
    public List<Expediente> buscarTodos() {
        return this.repositorio.buscarTodos(Expediente.class);
    }

    public int eliminarExpediente(Long idExpediente) {
        this.repositorio.iniciarTransaccion();
        try {
            // Buscar el expediente por ID
            Expediente expediente = this.repositorio.buscar(Expediente.class, idExpediente);

            if (expediente != null) {
                // Verificar si hay reuniones asociadas
                List<Reunion> reuniones = new ArrayList<>(expediente.getReunion());
                if (!reuniones.isEmpty()) {
                    // Descartar la transacción si hay reuniones asociadas
                    this.repositorio.descartarTransaccion();
                    return 1; // Indicando que el expediente tiene reuniones asociadas
                }

                List<Accion> acciones = new ArrayList<>(expediente.getAccion()); // Obtener la lista de acciones
                for (Accion accion : acciones) {
                    // Eliminar la acción de la base de datos directamente
                    this.repositorio.eliminar(accion);
                }
                
                 // Verificar si hay involucrados asociados
                List<Involucrado> involucrados = new ArrayList<>(expediente.getInvolucrado());
                if (!involucrados.isEmpty()) {
                    this.repositorio.descartarTransaccion();
                    return 2; // Indicando que el expediente tiene involucrados asociados
                }

                // Eliminar el expediente
                this.repositorio.eliminar(expediente);
                this.repositorio.confirmarTransaccion();
                return 0; // Eliminación exitosa
            } else {
                this.repositorio.descartarTransaccion();
                return 2; // Expediente no encontrado
            }
        } catch (Exception e) {
            this.repositorio.descartarTransaccion();
            e.printStackTrace();
            return 3; // Error inesperado
        }
    }

    public List<Expediente> obtenerExpedientesAbiertos() {
        // Obtener todos los expedientes
        List<Expediente> todosExpedientes = repositorio.buscarTodos(Expediente.class);
        
        // Filtrar los expedientes para obtener solo los que están abiertos
        List<Expediente> expedientesAbiertos = new ArrayList<>();
        for (Expediente expediente : todosExpedientes) {
            if (expediente.isEstadoExpediente()) {
                expedientesAbiertos.add(expediente);
            }
        }
        return expedientesAbiertos;
    }
    public Long agregarExpediente(Long nroExpediente, String iniciante , String tema, String textoNota , LocalDate fechaingresoFacultad, Boolean estadoExpediente) {
        if (nroExpediente == null ||iniciante.trim().length() == 0 ||tema.trim().length() == 0 ||textoNota.trim().length() == 0 || fechaingresoFacultad  == null || estadoExpediente == null) {
            throw new IllegalArgumentException("Faltan datos para agregar el Expediente");
        }
        this.repositorio.iniciarTransaccion();
        Expediente expediente = new Expediente(nroExpediente, iniciante.toUpperCase().trim(), tema.toUpperCase().trim(),textoNota.toUpperCase().trim(),fechaingresoFacultad,estadoExpediente);
        this.repositorio.insertar(expediente);
        this.repositorio.confirmarTransaccion();
        this.repositorio.refrescar(expediente);
        return expediente.getIdExpediente();
    }



    public void editarExpediente(Long idExpediente, Long nroExpediente, String iniciante, String tema, String nota, LocalDate fechaIngreso, Boolean estadoExpediente) {
        this.repositorio.iniciarTransaccion();
        // Cambiar la búsqueda para usar idExpediente
        Expediente expediente = this.repositorio.buscar(Expediente.class, idExpediente);
        if (expediente != null) {
            expediente.setNroExpediente(nroExpediente);
            expediente.setIniciante(iniciante.toUpperCase().trim());
            expediente.setTema(tema.toUpperCase().trim());
            expediente.setTextoNota(nota.toUpperCase().trim());
            expediente.setFechaingresoFacultad(fechaIngreso);
            expediente.setEstadoExpediente(estadoExpediente);
            this.repositorio.modificar(expediente);
            this.repositorio.confirmarTransaccion();
            this.repositorio.refrescar(expediente);
        } else {
            this.repositorio.descartarTransaccion();
        }
    }
}
