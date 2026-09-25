package edu.unam.repositorios;

import java.util.List;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;



public class Repositorio  {
    
    private final EntityManager em;
        
    public Repositorio(EntityManagerFactory emf) {
        this.em = emf.createEntityManager();        
    }
    
    public void iniciarTransaccion() {
        em.getTransaction().begin();
    }
    
    public void confirmarTransaccion() {
        em.getTransaction().commit();
    }

    public void descartarTransaccion() {
        em.getTransaction().rollback();
    }
    
    public void insertar(Object o) {
        this.em.persist(o);
    }
    
    public void modificar(Object o){
        this.em.merge(o);
    }

    public void eliminar(Object o){
        this.em.remove(o);
    }

    public void refrescar(Object o) {
        this.em.refresh(o);
    }
    

    // Devuelve un objeto de tipo clase (T) teniendo en cuenta el id    
    public <T extends Object> T buscar(Class<T> clase, Object id) {
        return (T) this.em.find(clase, id);
    }
     
    

    // Devuelve una lista de ese tipo clase =(T)
    public <T extends Object> List<T> buscarTodos (Class<T> clase) {
        // obtengo un CriteriaBuilder
        CriteriaBuilder cb = this.em.getCriteriaBuilder();
        // creo un CriteriaQuery
        CriteriaQuery<T> consulta = cb.createQuery(clase);
        // defino el FROM
        Root<T> origen = consulta.from(clase);
        // defino el select (opcional)
        consulta.select(origen);
        // ejecuto y obtengo el resultado
        return em.createQuery(consulta).getResultList();      
    }

    

}