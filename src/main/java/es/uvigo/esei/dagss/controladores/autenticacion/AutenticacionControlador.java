/*
 Proyecto Java EE, DAGSS-2015
 */
package es.uvigo.esei.dagss.controladores.autenticacion;

import es.uvigo.esei.dagss.dominio.daos.UsuarioDAO;
import es.uvigo.esei.dagss.dominio.entidades.TipoUsuario;
import es.uvigo.esei.dagss.dominio.entidades.Usuario;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;



/**
 *
 * @author ribadas
 */
@Named(value = "autenticacionControlador")
@SessionScoped
public class AutenticacionControlador implements Serializable {

    private boolean usuarioAutenticado;
    private TipoUsuario tipoUsuario;
    private Usuario usuarioActual;

    @EJB
    private UsuarioDAO usuarioDAO;

    public AutenticacionControlador() {
    }

    @PostConstruct
    public void inicializar() {
        this.usuarioAutenticado = false;
        this.usuarioActual = null;
    }

    public boolean isUsuarioAutenticado() {
        return usuarioAutenticado;
    }

    public void setUsuarioAutenticado(boolean usuarioAutenticado) {
        this.usuarioAutenticado = usuarioAutenticado;
    }

    public Usuario getUsuarioActual() {
        return usuarioActual;
    }

    public void setUsuarioActual(Usuario usuarioActual) {
        this.usuarioActual = usuarioActual;
    }

    public boolean autenticarUsuario(Long idUsuario, String passwordEnClaro, String tipo) {
        if (usuarioDAO.autenticarUsuario(idUsuario, passwordEnClaro, tipo)) {
            usuarioAutenticado = true;
            usuarioActual = usuarioDAO.buscarPorId(idUsuario);
            tipoUsuario = usuarioActual.getTipoUsuario();
            return true;
        } else {
            usuarioAutenticado = false;
            usuarioActual = null;
            tipoUsuario = null;
            return false;
        }
    }
   

    public String doLogOut() {
        usuarioDAO.actualizarUltimoAcceso(usuarioActual.getId());
        usuarioAutenticado = false;
        usuarioActual = null;
        tipoUsuario = null;

        // Finalizar la sesion        
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();

        // Volver a la página principal
        return "/index?faces-redirect=true";
    }

}