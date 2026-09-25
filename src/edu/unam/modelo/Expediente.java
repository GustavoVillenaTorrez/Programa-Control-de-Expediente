package edu.unam.modelo;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Entity
@Table(name = "expediente")
public class Expediente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_expediente")
    private Long idExpediente;
    @Column(name="nro_expediente", nullable = false)
    private Long nroExpediente;
    @Column(name="iniciante", nullable = false, length = 50 )
    private String iniciante;
    @Column(name="tema", nullable = false, length = 50 )
    private String tema;
    @Column(name="texto_nota", nullable = false, length = 50 )
    private String textoNota;
    @Column(name="fecha_ingreso_facultad", nullable = false)
    private LocalDate fechaingresoFacultad;
    @Column(name="estado_expediente", nullable = false)
    private Boolean estadoExpediente;

    @OneToMany(mappedBy = "expediente")
    private List<Involucrado> involucrado = new ArrayList<>();

    @OneToMany(mappedBy = "expediente",cascade = CascadeType.ALL)
    private List<Accion> accion = new ArrayList<>();

    @OneToMany(mappedBy = "expediente", cascade = CascadeType.ALL)
    private List<Minuta> minuta = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinColumn(name="idReunion")
    private Set<Reunion> reunion;

    //contructores
    public Expediente() {
    }
    public Expediente( Long nroExpediente ,String iniciante, String tema,String textoNota, LocalDate fechaingresoFacultad,Boolean estadoExpediente) {
        this.nroExpediente = nroExpediente;
        this.iniciante = iniciante;
        this.tema=tema;
        this.textoNota = textoNota;
        this.fechaingresoFacultad = fechaingresoFacultad;
        this.estadoExpediente = estadoExpediente;   
    }
    
    //get y set
    public Long getIdExpediente() {
        return idExpediente;
    }
    public void setIdExpediente(Long idExpediente) {
        this.idExpediente = idExpediente;
    }
    public String getIniciante() {
        return iniciante;
    }
    public String getTema() {
        return tema;
    }
    public void setTema(String tema) {
        this.tema = tema;
    }
    public void setIniciante(String iniciante) {
        this.iniciante = iniciante;
    }
    public String getTextoNota() {
        return textoNota;
    }
    public void setTextoNota(String textoNota) {
        this.textoNota = textoNota;
    }
    public LocalDate getFechaingresoFacultad() {
        return fechaingresoFacultad;
    }
    public void setFechaingresoFacultad(LocalDate fechaingresoFacultad) {
        this.fechaingresoFacultad = fechaingresoFacultad;
    }
 
    public Boolean isEstadoExpediente() {
        return estadoExpediente;
    }
    
    public Boolean getEstadoExpediente() {
        return estadoExpediente;
    }
    public void setEstadoExpediente(Boolean estadoExpediente) {
        this.estadoExpediente = estadoExpediente;
    }
    
    public List<Involucrado> getInvolucrado() {
        return involucrado;
    }
    public void setInvolucrado(List<Involucrado> involucrado) {
        this.involucrado = involucrado;
    }
    public List<Accion> getAccion() {
        return accion;
    }
    public void setAccion(List<Accion> accion) {
        this.accion = accion;
    }
    public List<Minuta> getMinuta() {
        return minuta;
    }
    public Set<Reunion> getReunion() {
        return reunion;
    }
    public void setReunion(Set<Reunion> reunion) {
        this.reunion = reunion;
    }
    public void setMinuta(List<Minuta> minuta) {
        this.minuta = minuta;
    }
    
    public Long getNroExpediente() {
        return nroExpediente;
    }
    public void setNroExpediente(Long nroExpediente) {
        this.nroExpediente = nroExpediente;
    }
    @Override
    public String toString() {
        return "Id:"+idExpediente +" - N°: " + nroExpediente + " - Iniciante: " + iniciante +" - Tema:" + tema+ "- Fecha: "+ fechaingresoFacultad;
    }

    public void agregarAccion(Accion accion) {
        this.accion.add(accion);
    }
    public void agregarMinuta(Minuta minuta) {
        this.minuta.add(minuta);
    }
    public void agregarInvolucrado(Involucrado involucrado) {
        this.involucrado.add(involucrado);
    }
    public void agregarReunion(Reunion reunion) {
        this.reunion.add(reunion);
    }
    
    

    
}

