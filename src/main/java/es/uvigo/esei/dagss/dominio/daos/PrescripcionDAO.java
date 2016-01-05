/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uvigo.esei.dagss.dominio.daos;

import es.uvigo.esei.dagss.dominio.entidades.Prescripcion;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

@Stateless
@LocalBean
public class PrescripcionDAO extends GenericoDAO<Prescripcion> {
    
    // Eliminar las prescripciones no relacionadas con ningún tratamiento
    public int borrarPrescripcionesAisladas() {
        int deleteCount = em.createQuery("DELETE FROM Prescripcion AS p "
                                    + " WHERE p.tratamiento IS NULL").executeUpdate();
        return deleteCount;
    }
    
}
