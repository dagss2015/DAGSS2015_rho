/*
 Práctica Java EE 7, DAGSS 2015/16 (ESEI, U. de Vigo)
 */
package es.uvigo.esei.dagss.dominio.entidades;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Version;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

@Entity
public class Prescripcion implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Size(min = 0, max = 255)
    String indicaciones;

    @ManyToOne
    Tratamiento tratamiento;

    @ManyToOne
    Medicamento medicamento;

    @Min(1)
    Integer dosis;

    @OneToMany(mappedBy = "prescripcion", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    List<Receta> recetas = new ArrayList<>();

    @Version
    Long version;

    public Prescripcion() {}

    public Prescripcion(String indicaciones, Tratamiento tratamiento, Medicamento medicamento, Integer dosis) {
        this.indicaciones = indicaciones;
        this.tratamiento = tratamiento;
        this.medicamento = medicamento;
        this.dosis = dosis;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIndicaciones() {
        return indicaciones;
    }

    public void setIndicaciones(String indicaciones) {
        this.indicaciones = indicaciones;
    }

    public Tratamiento getTratamiento() {
        return tratamiento;
    }

    public void setTratamiento(Tratamiento tratamiento) {
        this.tratamiento = tratamiento;
    }

    public Medicamento getMedicamento() {
        return medicamento;
    }

    public void setMedicamento(Medicamento medicamento) {
        this.medicamento = medicamento;
    }

    public Integer getDosis() {
        return dosis;
    }

    public void setDosis(Integer dosis) {
        this.dosis = dosis;
    }

    public List<Receta> getRecetas() {
        return recetas;
    }

    public void setRecetas(List<Receta> recetas) {
        this.recetas = recetas;
    }
    
    // Útil para distinguir las prescripciones de un tratamiento antes de haber
    // persistido su información en la base de datos
    /* public int getRowKey() {
        return hashCodeAlt();
    }
    
    private int hashCodeAlt() {
        if (this.dosis == null ||
            this.tratamiento == null ||
            this.medicamento == null ||
            this.indicaciones == null) {
            throw new NullPointerException("Imposible generar código hash alternativo sin medicamento, dosis, indicaciones y tratamiento");
        }
        int hash = 7;
        hash = 47 * hash + Objects.hashCode(this.indicaciones +
                                            this.dosis.toString() +
                                            this.medicamento.id.toString() + 
                                            this.tratamiento.id.toString());
        return hash;
    } */

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final Prescripcion other = (Prescripcion) obj;
        return Objects.equals(this.id, other.id);
    }

}
