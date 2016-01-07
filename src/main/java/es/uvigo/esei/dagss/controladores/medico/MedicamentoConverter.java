package es.uvigo.esei.dagss.controladores.medico;

import es.uvigo.esei.dagss.dominio.daos.MedicamentoDAO;
import es.uvigo.esei.dagss.dominio.entidades.Medicamento;
import javax.faces.application.FacesMessage;
// import javax.faces.bean.ManagedProperty;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
// import javax.inject.Named;

// @Named
@FacesConverter("medicamentoConverter")
public class MedicamentoConverter implements Converter {
    
    // @ManagedProperty("#{medicamentoDAO}")
    private MedicamentoDAO medicamentoDAO;

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if(value != null && value.trim().length() > 0) {
            try {
                // MedicamentoDAO medicamentoDAO = (MedicamentoDAO) context.getELContext().getELResolver().getValue(context.getELContext(), null, "medicamentoDAO"); 
                // MedicamentoDAO medicamentoDAO = (MedicamentoDAO) context.getExternalContext().getApplicationMap().get("medicamentoDAO");
                return medicamentoDAO.buscarPorId(Integer.parseInt(value));
            } catch (NumberFormatException e) {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error de conversión", "Medicamento inválido"));
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object obj) {
        if(obj != null) {
            return String.valueOf(((Medicamento) obj).getId());
        }
        return null;
    }
    
}