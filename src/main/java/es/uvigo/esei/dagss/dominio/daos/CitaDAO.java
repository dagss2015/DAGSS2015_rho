/*
 Proyecto Java EE, DAGSS-2014
 */

package es.uvigo.esei.dagss.dominio.daos;

import es.uvigo.esei.dagss.dominio.entidades.Cita;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.Query;

@Stateless
@LocalBean
public class CitaDAO extends GenericoDAO<Cita> {
    
    private java.util.Date strToDate(String str) throws ParseException {
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        return fmt.parse(str);
    }
    
    public List<Cita> buscarPorFecha(String fechaStr) {
        java.util.Date fecha;
        try {
            fecha = strToDate(fechaStr);
        } catch (ParseException ex) {
            fecha = new java.util.Date();
        }
        return buscarPorFecha(fecha);
    }
    
    public List<Cita> buscarPorFecha() {
        return buscarPorFecha(new java.util.Date());
    }

    public List<Cita> buscarPorFecha(java.util.Date fecha) {        
        Query q = em.createQuery("SELECT c FROM Cita AS c "
                            + " WHERE c.fecha = :fecha");
        q.setParameter("fecha", new java.sql.Date(fecha.getTime()));

        return q.getResultList();
    }
    
}
