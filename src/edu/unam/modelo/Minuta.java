package edu.unam.modelo;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;




@Entity
@Table(name = "minuta")
public class Minuta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_minuta")
    private Long idMinuta;
    
    @Column(name="desiciones_tomadas", nullable = false)
    private String desicionesTomadas;
    @Column(name="notas_adicionales", nullable = false)
    private String notasAdicionales;

    @ManyToOne
    @JoinColumn(name = "idReunion")
    private Reunion reunion;
    @ManyToOne
    @JoinColumn(name="idExpediente")
    private Expediente expediente;
    
    //contructores
    public Minuta(){
    }
    
    public Minuta(String desicionesTomadas, String notasAdicionales, Reunion reunion,Expediente expediente) {

        this.desicionesTomadas = desicionesTomadas;
        this.notasAdicionales = notasAdicionales;
        this.reunion=reunion;
        this.expediente=expediente;
        expediente.agregarMinuta(this);
        reunion.agregarMinuta(this);
    }

    public Long getIdMinuta() {
        return idMinuta;
    }
    

    public String getDesicionesTomadas() {
        return desicionesTomadas;
    }
    public void setDesicionesTomadas(String desicionesTomadas) {
        this.desicionesTomadas = desicionesTomadas;
    }
    public String getNotasAdicionales() {
        return notasAdicionales;
    }
    public void setNotasAdicionales(String notasAdicionales) {
        this.notasAdicionales = notasAdicionales;
    }
    
    public Reunion getReunion() {
        return reunion;
    }

    public void setReunion(Reunion reunion) {
        this.reunion = reunion;
    }

    
    public Expediente getExpediente() {
        return expediente;
    }

    public void setExpediente(Expediente expediente) {
        this.expediente = expediente;
    }
    
    @Override
    public String toString() {
        return idMinuta + " " + desicionesTomadas + " " + notasAdicionales + " " +reunion+ " "+ expediente;
    }

    public void setIdMinuta(Long idMinuta) {
        this.idMinuta = idMinuta;
    } 
}
