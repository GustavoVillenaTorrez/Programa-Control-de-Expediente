package edu.unam.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDate;


@Entity
@Table(name = "asistencia")
public class Asistencia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_asistencia")
    private Long idAsistencia;
    @Column(name="presencia")
    private Boolean presencia;
    @Column(name="fecha")
    private LocalDate fecha;

    @ManyToOne
    @JoinColumn(name = "idReunion")
    private Reunion reunion;

    @ManyToOne
    @JoinColumn(name = "idInvolucrado")
    private Involucrado involucrado;

    //Contructores
    public Asistencia(){

    }
    public Asistencia(Boolean presencia,LocalDate fecha, Reunion reunion,Involucrado involucrado) {
        this.presencia = presencia;
        this.fecha=fecha;
        this.involucrado=involucrado;
        this.reunion=reunion;
        involucrado.agregarAsistencia(this);
        reunion.agregarAsistencia(this);
    }
    //set y get
    
    public Long getIdAsistencia() {
        return idAsistencia;
        
    }
    public void setIdAsistencia(Long idAsistencia) {
        this.idAsistencia = idAsistencia;
    }
    public Boolean isPresencia() {
        return presencia;
    }
    public void setPresencia(Boolean presencia) {
        this.presencia = presencia;
    }
    public LocalDate getFecha() {
        return fecha;
    }
    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }
    
    public Reunion getReunion() {
        return reunion;
    }
    public void setReunion(Reunion reunion) {
        this.reunion = reunion;
    }
    public Involucrado getInvolucrado() {
        return involucrado;
    }
    public void setInvolucrado(Involucrado involucrado) {
        this.involucrado = involucrado;
    }
    
    public Boolean getPresencia() {
        return presencia;
    }
    @Override
    public String toString() {
        return idAsistencia + " " + presencia + " " + involucrado + " "+ reunion;
    }
    
    

    
}
