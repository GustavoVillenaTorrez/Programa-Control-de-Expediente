package edu.unam.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "involucrado")
public class Involucrado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_involucrado")
    private Long idInvolucrado;
    @Column(name="nombre_apellido", nullable = false, length = 70 )
    private String nombre;
    @Column(name="dni", nullable = false )
    private int dni;
    @Column(name="telefono", nullable = false )
    private Long telefono;
    @Column(name="email", nullable = false, length = 100 )
    private String email;
    @Column(name="rol", nullable = false, length = 50 )
    private String rol;
    @Column(name="es_miembroconsejo", nullable = false)
    private Boolean esmiembroConsejo;



    @ManyToOne
    @JoinColumn(name="idExpediente")
    private Expediente expediente;
    
    @OneToMany(mappedBy = "involucrado",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Asistencia> asistencia = new ArrayList<>();
    
    @ManyToMany
    @JoinColumn(name="idReunion")
    private Set<Reunion> reunion;


    
    //Constructores
    public Involucrado(){

    }
    public Involucrado(String nombre, int dni, Long telefono, String email, String rol, Boolean esmiembroConsejo, Expediente expediente) {
        this.nombre = nombre;
        this.dni = dni;
        this.telefono = telefono;
        this.email = email;
        this.rol = rol;
        this.esmiembroConsejo = esmiembroConsejo;
        
        this.expediente = expediente;
        expediente.agregarInvolucrado(this);
    }

    public Long getIdInvolucrado() {
        return idInvolucrado;
    }
    public void setIdInvolucrado(Long idInvolucrado) {
        this.idInvolucrado = idInvolucrado;
    }
    
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public Long getTelefono() {
        return telefono;
    }
    public void setDetallesdeContaco(Long telefono) {
        this.telefono = telefono;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getRol() {
        return rol;
    }
    public void setRol(String rol) {
        this.rol = rol;
    }
    public Boolean getEsmiembroConsejo() {
        return esmiembroConsejo;
    }
    public Boolean isMiembroConsejo() {
        return esmiembroConsejo;
    }
    

    public Expediente getExpediente() {
        return expediente;
    }
    public void setExpediente(Expediente expediente) {
        this.expediente = expediente;
    }
    public List<Asistencia> getAsistencia() {
        return asistencia;
    }
    public void setAsistencia(List<Asistencia> asistencia) {
        this.asistencia = asistencia;
    }
    public Set<Reunion> getReunion() {
        return reunion;
    }
    public void setReunion(Set<Reunion> reunion) {
        this.reunion = reunion;
    }
    public int getDni() {
        return dni;
    }
    public void setDni(int dni) {
        this.dni = dni;
    }
    public void setTelefono(Long telefono) {
        this.telefono = telefono;
    }
    public void setEsmiembroConsejo(Boolean esmiembroConsejo) {
        this.esmiembroConsejo = esmiembroConsejo;
    }

    @Override
    public String toString() {
        return "Id:"+ idInvolucrado +" - Nombre:"+ nombre + " - DNI:"+ dni +" - Telef:" + telefono + " - Correo " + email + " - Rol:" + rol ;
    }

    public void agregarAsistencia(Asistencia asistencia) {
        this.asistencia.add(asistencia);
    }

    public void agregarReunion(Reunion reunion) {
        this.reunion.add(reunion);
    }
    
}
