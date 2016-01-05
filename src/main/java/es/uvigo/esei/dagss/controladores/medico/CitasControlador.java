/*
 Proyecto Java EE, DAGSS-2015
 */
package es.uvigo.esei.dagss.controladores.medico;

import es.uvigo.esei.dagss.dominio.daos.CitaDAO;
import es.uvigo.esei.dagss.dominio.daos.MedicamentoDAO;
import es.uvigo.esei.dagss.dominio.daos.MedicoDAO;
import es.uvigo.esei.dagss.dominio.daos.PacienteDAO;
import es.uvigo.esei.dagss.dominio.daos.PrescripcionDAO;
import es.uvigo.esei.dagss.dominio.daos.TratamientoDAO;
import es.uvigo.esei.dagss.dominio.entidades.Cita;
import es.uvigo.esei.dagss.dominio.entidades.EstadoCita;
import es.uvigo.esei.dagss.dominio.entidades.Medicamento;
import es.uvigo.esei.dagss.dominio.entidades.Medico;
import es.uvigo.esei.dagss.dominio.entidades.Paciente;
import es.uvigo.esei.dagss.dominio.entidades.Prescripcion;
import es.uvigo.esei.dagss.dominio.entidades.Tratamiento;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author dagss
 */
@Named(value = "citasControlador")
@SessionScoped
public class CitasControlador implements Serializable {

    static final public Integer DURACION_CITA_POR_DEFECTO = 15; // Citas de 15 minutos

    @EJB
    CitaDAO citaDAO;
    @EJB
    MedicoDAO medicoDAO;
    @EJB
    PacienteDAO pacienteDAO;
    @EJB
    TratamientoDAO tratamientoDAO;
    @EJB
    MedicamentoDAO medicamentoDAO;
    @EJB
    PrescripcionDAO prescripcionDAO;

    List<Cita> citas;
    Cita citaActual;
    
    List<Tratamiento> tratamientos;
    Tratamiento tratamientoActual;

    Prescripcion prescripcionActual;
    

    public CitasControlador() {}

    @PostConstruct
    public void inicializar() {
        citas = citaDAO.buscarPorFecha("2014-01-15"); // Citas en cierta fecha (ejemplo conocido en la base de datos, para pruebas)
        // citas = citaDAO.buscarPorFecha(); // Citas de «hoy»
    }

    // Usado en la lista desplegable con los estados de una cita
    public EstadoCita[] getEstadosCitas() {
        return EstadoCita.values();
    }

    public List<Cita> getCitas() {
        return citas;
    }

    public void setCitas(List<Cita> citas) {
        this.citas = citas;
    }

    public Cita getCitaActual() {
        return citaActual;
    }

    public void setCitaActual(Cita citaActual) {
        this.citaActual = citaActual;
    }

    public List<Paciente> getListadoPacientes() {
        return pacienteDAO.buscarTodos();
    }

    public List<Medico> getListadoMedicos() {
        return medicoDAO.buscarTodos();
    }

    public void doEliminar() {
        citaDAO.eliminar(citaActual);
        citas = citaDAO.buscarTodos(); // Actualizar lista de centros
    }

    public void doNuevo() {
        citaActual = new Cita(); // Cita valia   
        citaActual.setDuracion(DURACION_CITA_POR_DEFECTO);
        citaActual.setEstado(EstadoCita.PLANIFICADA);
    }

    public void doEditar(Cita cita) {
        citaActual = cita; // Otra alternativa: volver a refrescarlos desde el DAO
    }
    
    public void finalizarCita() {
        if (citaActual != null) {
            citaActual.setEstado(EstadoCita.COMPLETADA);
            citaActual = citaDAO.actualizar(citaActual);
            // citas = citaDAO.buscarTodos();
        }
    }
    
    public void anularCita() {
        if (citaActual != null) {
            citaActual.setEstado(EstadoCita.ANULADA);
            citaActual = citaDAO.actualizar(citaActual);
        }
    }
    
    public boolean isFinalizarCitaDisabled() {
        return citaActual == null
            || citaActual.getEstado().equals(EstadoCita.COMPLETADA);
    }

    public void doGuardarNuevo() {
        // Crea un nuevo centro de salud
        citaActual = citaDAO.crear(citaActual);
        // Actualiza lista de centros de salud a mostrar
        citas = citaDAO.buscarTodos();
    }

    public void doGuardarEditado() {
        // Actualiza un centro de salud
        citaActual = citaDAO.actualizar(citaActual);
        // Actualiza lista de centros de salud a mostrar
        citas = citaDAO.buscarTodos();
    }

    public String verListadoCitas() {
        return "../listadoCitas?faces-redirect=true";
    }
    
    
    // Tratamientos
    
    public List<Tratamiento> getTratamientos() {
        return tratamientos;
    }
    
    public void setTratamientos(List<Tratamiento> t) {
        tratamientos = t;
    }
    
    public Tratamiento getTratamientoActual() {
        return tratamientoActual;
    }

    public void setTratamientoActual(Tratamiento tratamientoActual) {
        this.tratamientoActual = tratamientoActual;
    }
    
    public void doGetTratamientosPaciente() {
        if (citaActual != null) {
            tratamientos = tratamientoDAO.buscarPorPaciente(citaActual.getPaciente());
        }
    }
    
    public boolean isTratarPacienteDisabled() {
        return citaActual == null 
            || !citaActual.getEstado().equals(EstadoCita.PLANIFICADA); // Solo permitir acceder a citas en este estado
    }
    
    public void doNuevoTratamiento() {
        if (citaActual != null) {
            tratamientoActual = new Tratamiento();
            tratamientoActual.setMedico(citaActual.getMedico());
            tratamientoActual.setPaciente(citaActual.getPaciente());
        }
    }
    
    private void enlazarPrescripcionesConTratamientos() {
        for (Prescripcion p : tratamientoActual.getPrescripciones()) {
            p.setTratamiento(tratamientoActual);
            p = prescripcionDAO.actualizar(p);
        }
        prescripcionDAO.borrarPrescripcionesAisladas();
    }
    
    public void doGuardarNuevoTratamiento() {
        tratamientoActual = tratamientoDAO.crear(tratamientoActual);
        enlazarPrescripcionesConTratamientos();
        doGetTratamientosPaciente();
    }
    
    public void doEditarTratamiento() {
        tratamientoActual = tratamientoDAO.actualizar(tratamientoActual);
        enlazarPrescripcionesConTratamientos();
        doGetTratamientosPaciente();
    }
    
    public void doBorrarTratamiento() {
        if (tratamientoActual != null) {
            tratamientoDAO.eliminar(tratamientoActual);
            tratamientos = tratamientoDAO.buscarPorPaciente(citaActual.getPaciente());
        }
    }
    
    
    // Prescripciones

    public Prescripcion getPrescripcionActual() {
        return prescripcionActual;
    }

    public void setPrescripcionActual(Prescripcion prescripcionActual) {
        this.prescripcionActual = prescripcionActual;
    }
    
    public void doNuevaPrescripcion() {
        if (tratamientoActual != null) {
            prescripcionActual = new Prescripcion();
        }
    }
    
    public void anadirNuevaPrescripcion() {
        // Necesitamos persistir las prescripciones de antemano para contentar
        // al elemento "dataTable" de la vista
        prescripcionActual.setMedicamento(medicamentoSeleccionado);
        prescripcionActual = prescripcionDAO.crear(prescripcionActual);
        tratamientoActual.getPrescripciones().add(prescripcionActual);
        
    }
    
    public void borrarPrescripcion() {
        if (prescripcionActual != null) {
            prescripcionDAO.eliminar(prescripcionActual);
            tratamientoActual.getPrescripciones().remove(prescripcionActual);
            tratamientoActual = tratamientoDAO.actualizar(tratamientoActual);
        }
    }
    
    
    // Medicamentos
    
    private Medicamento medicamentoSeleccionado;
    
    public Medicamento getMedicamentoSeleccionado() {
        return medicamentoSeleccionado;
    }
    
    public void setMedicamentoSeleccionado(Medicamento m) {
        medicamentoSeleccionado = m;
    }
    
    public List<Medicamento> getMedicamentos(String query) {
        return medicamentoDAO.buscarPorCualquiera(query);
    }

}
