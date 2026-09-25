package edu.unam.modelo;
import java.time.LocalDate;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "accion")
public class Accion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "id_accion")
    private Long idAccion;
    @Column(name="descripcion", nullable = false)
    private String descripcion;
    @Column(name="fecha", nullable = false)
    private LocalDate fecha;

    @ManyToOne
    @JoinColumn(name="id_Expediente")
    private Expediente expediente;

    //contructores
    public Accion() {
    }

    public Accion(String descripcion, LocalDate fecha,Expediente expediente) {
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.expediente=expediente;
        expediente.agregarAccion(this);
    }

    //set y get
    public Long getIdAccion() {
        return idAccion;
    }
    public void setIdAccion(Long idAccion) {
        this.idAccion = idAccion;
    }
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Expediente getExpediente() {
        return expediente;
    }
    
    public void setExpediente(Expediente expediente) {
        this.expediente = expediente;
    }

    @Override
    public String toString() {
        return idAccion + " " + descripcion + " " + fecha + " " +expediente;
    }

}
