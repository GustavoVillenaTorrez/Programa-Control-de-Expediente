package edu.unam.modelo;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;


import java.time.LocalTime;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "reunion")
public class Reunion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_reunion")
    private Long idReunion;
    @Column(name="fecha", nullable = false)
    private LocalDate fecha;
    @Column(name="estado_reunion", nullable = false)
    private String estadoReunion;
    @Column(name="hora_inicio", nullable = false)
    private LocalTime horaInicio;
    @Column(name="hora_fin", nullable = false)
    private LocalTime horaFin;

    @ManyToMany(mappedBy = "reunion")
    @JoinTable(
        name = "involucrado_reunion",
        joinColumns = @JoinColumn(name = "idReunion"),
        inverseJoinColumns = @JoinColumn(name = "idInvolucrado"),
        uniqueConstraints = @UniqueConstraint(columnNames = {"idReunion", "idInvolucrado"})
    )
    private Set<Involucrado> involucrado;

    @OneToMany(mappedBy = "reunion",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Asistencia> asistencia = new ArrayList<>();

    @ManyToMany(mappedBy = "reunion")
    @JoinTable(
        name = "expediente_reunion",
        joinColumns = @JoinColumn(name = "idReunion"),
        inverseJoinColumns = @JoinColumn(name = "idExpediente"),
        uniqueConstraints = @UniqueConstraint(columnNames = {"idReunion", "idExpediente"})
    )
    private Set<Expediente> expediente ;
    
    @OneToMany(mappedBy = "reunion",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Minuta> minuta = new ArrayList<>();
    
    //Constructores
    public Reunion() {
    }

    public Reunion(LocalDate fecha, String estadoReunion, LocalTime horaInicio, LocalTime horaFin) {
        this.fecha = fecha;
        this.estadoReunion = estadoReunion;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
    }

    //set y get
    public Long getIdReunion() {
        return idReunion;
    }
    public void setIdReunion(Long idReunion) {
        this.idReunion = idReunion;
    }
    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public void setEstadoReunion(String estadoReunion) {
        this.estadoReunion = estadoReunion;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public LocalTime getHoraFin() {
        return horaFin;
    }
    public void sethoraFin(LocalTime horaFin) {
        this.horaFin = horaFin;
    }
    public void setHoraFin(LocalTime horaFin) {
        this.horaFin = horaFin;
    }
    public String getEstadoReunion() {
        return estadoReunion;
    }
    

    public Set<Involucrado> getInvolucrado() {
        return involucrado;
    }

    public void setInvolucrado(Set<Involucrado> involucrado) {
        this.involucrado = involucrado;
    }
    public List<Asistencia> getAsistencia() {
        return asistencia;
    }

    public void setAsistencia(List<Asistencia> asistencia) {
        this.asistencia = asistencia;
    }
    public Set<Expediente> getExpediente() {
        return expediente;
    }

    public void setExpediente(Set<Expediente> expediente) {
        this.expediente = expediente;
    }

    public List<Minuta> getMinuta() {
        return minuta;
    }

    public void setMinuta(List<Minuta> minuta) {
        this.minuta = minuta;
    }


    @Override
    public String toString() {
        StringBuilder temasExpedientes = new StringBuilder();
        for (Expediente exp : expediente) {
            temasExpedientes.append(exp.getTema()).append(", ");
        }
        String temas = temasExpedientes.toString();
        return "Id: " + idReunion + " - Fecha: " + fecha + " - Estado: " + estadoReunion + " - Temas Expedientes: " + temas;
    }

     public void agregarMinuta(Minuta minuta) {
        this.minuta.add(minuta);
    }
    public void agregarAsistencia(Asistencia e) {
        this.asistencia.add(e);
    }

    public void agregarExpediente(Expediente e) {
        this.expediente.add(e);
        e.agregarReunion(this);
    }

    public void agregarInvolucrado(Involucrado e) {
        this.involucrado.add(e);
        e.agregarReunion(this);
    }



}
