/*
 Proyecto Java EE, DAGSS-2014
 */
package es.uvigo.esei.dagss.dominio.daos;

import es.uvigo.esei.dagss.dominio.entidades.Medicamento;
import java.util.List;
// import javax.annotation.ManagedBean;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.Query;

@Stateless
@LocalBean
// @ManagedBean
public class MedicamentoDAO extends GenericoDAO<Medicamento> {

    public List<Medicamento> buscarPorCualquiera(String busqueda) {
        Query q = em.createQuery("SELECT m FROM Medicamento AS m "
                            + " WHERE m.nombre LIKE :nombre "
                            + " OR m.familia LIKE :familia"
                            + " OR m.fabricante LIKE :fabricante");
        String busquedaComodines = "%" + busqueda + "%";
        q.setParameter("nombre", busquedaComodines);
        q.setParameter("familia", busquedaComodines);
        q.setParameter("fabricante", busquedaComodines);
        return q.getResultList();
    }
    
}
