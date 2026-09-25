package edu.unam.servicios;

import edu.unam.repositorios.*;

import java.util.List;

import edu.unam.modelo.Expediente;
import edu.unam.modelo.Minuta;
import edu.unam.modelo.Reunion;





public class ServicioMinuta {

    private Repositorio repositorio;

    public ServicioMinuta(Repositorio p) {
        this.repositorio = p;
    }
    //MINUTA
    //Listar
    public List<Minuta> listarMinuta() {
        return this.repositorio.buscarTodos(Minuta.class);
    }
    
    //Buscar
    public Minuta buscarMinuta(Long idMinuta) {
        return this.repositorio.buscar(Minuta.class, idMinuta);
    }
    //Agregar
    public Long agregarMinuta(String desicionesTomadas, String notasAdicionales, Reunion reunion,Expediente expediente) {
        if (desicionesTomadas.trim().length() == 0 || reunion == null|| expediente  == null) {
            throw new IllegalArgumentException("Faltan datos");
        }
        this.repositorio.iniciarTransaccion();
        Minuta minuta = new Minuta( desicionesTomadas.toUpperCase().trim(), notasAdicionales.toUpperCase().trim(), reunion, expediente);
        this.repositorio.insertar(minuta);
        this.repositorio.confirmarTransaccion();
        this.repositorio.refrescar(minuta);
        return  minuta.getIdMinuta();
    }
    
    // Cambiar valor devuelto
    public void editarMinuta(Long idMinuta, String desicionesTomadas, String notasAdicionales, Reunion reunion,Expediente expediente) {
        if (desicionesTomadas.trim().length() == 0 || reunion == null|| expediente  == null) {
            throw new IllegalArgumentException("Faltan datos");
        }
        this.repositorio.iniciarTransaccion();
        Minuta minuta = this.repositorio.buscar(Minuta.class, idMinuta);
        if (minuta != null ) {
            minuta.setDesicionesTomadas(desicionesTomadas);
            minuta.setNotasAdicionales(notasAdicionales);
            if (! minuta.getExpediente().equals(expediente)) {
                minuta.getExpediente().getMinuta().remove(minuta);
                minuta.setExpediente(expediente);
                expediente.getMinuta().add(minuta);
            }
            if (! minuta.getReunion().equals(reunion)) {
                minuta.getReunion().getMinuta().remove(minuta);
                minuta.setReunion(reunion);
                reunion.getMinuta().add(minuta);
            }
            this.repositorio.modificar(minuta);
            this.repositorio.confirmarTransaccion();
        } else {
            this.repositorio.descartarTransaccion();
        }
    }
    //Eliminar 
    public int eliminarMinuta(Long idMinuta) {
        this.repositorio.iniciarTransaccion();
        Minuta minuta = this.repositorio.buscar(Minuta.class, idMinuta);
        if (minuta != null ) {
            this.repositorio.eliminar(minuta);
            this.repositorio.confirmarTransaccion();
            return 0;
        } else {
            this.repositorio.descartarTransaccion();
            return 1;
        }
    }
}

