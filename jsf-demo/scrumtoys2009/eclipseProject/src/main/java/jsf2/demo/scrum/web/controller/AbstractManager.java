package jsf2.demo.scrum.web.controller;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Dr. Spock (spock at dev.java.net)
 */
public abstract class AbstractManager {

    @PersistenceContext
    protected EntityManager entityManager;

    protected final Object doInTransaction(PersistenceAction action) throws ManagerException {
        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();
        try {
            Object result = action.execute(entityManager);
            tx.commit();
            return result;
        } catch (Exception e) {
            tx.rollback();
            throw new ManagerException(e);
        }
    }

    protected static interface PersistenceAction {

        Object execute(EntityManager em);
    }

    protected void addMessage(String message) {
        addMessage(null, message, FacesMessage.SEVERITY_INFO);
    }

    protected void addMessage(String componentId, String message) {
        addMessage(componentId, message, FacesMessage.SEVERITY_INFO);
    }

    protected void addMessage(String message, Severity severity) {
        addMessage(null, message, severity);
    }

    protected void addMessage(String componentId, String message, Severity severity) {
        FacesContext.getCurrentInstance().addMessage(componentId, new FacesMessage(severity, message, message));
    }
}
