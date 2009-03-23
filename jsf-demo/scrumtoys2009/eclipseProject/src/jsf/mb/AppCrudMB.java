package jsf.mb;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

import dao.AbstractEntityDAO;

import jsf.util.FacesUtil;
import services.AppCrudService;
import services.ServicesException;
import entities.AbstractEntity;

/**
 * Backing bean for CRUD functionalities
 * 
 * @author Vinny @ since 2008
 * 
 */
public abstract class AppCrudMB<D extends AbstractEntityDAO<E>, S extends AppCrudService<D, E>, E extends AbstractEntity>
		extends AppMB {

	private static final long serialVersionUID = -3357324277219101747L;
	private S service;
	private DataModel<E> dm;
	private List<E> lst;
	private E toSave;
	private E toEdit = null;
	private E toRemove = null;
	
	public abstract E getNewEntity();
	
	public abstract S getService();

	public abstract String getEntitiesLabel();

	public abstract String getEntityLabel();

	public abstract boolean preSave();

	public AppCrudMB(){
		this.init();
	}
	/**
	 * @category MB action
	 */
	public String load() {
		try {
			this.lst = this.getLoadedEntities();
			this.dm = new ListDataModel<E>(this.lst);
			getFacesUtil().addGlobalInfoMessage(
					String.format("%s loaded successful.", this
							.getEntitiesLabel()),
					String.format("%d %s loaded successful.", this.lst.size(),this
							.getEntitiesLabel()));
		} catch (ServicesException e) {
			getFacesUtil().createErrorMessage(e);
		}// try
		return OUTCOME.SAME_PAGE.getValue();
	}
	
	public List<E> getLoadedEntities() throws ServicesException{
		return this.service.findAll();
	}

	/**
	 * @category MB action
	 */
	public String save() {
		try {
			if (this.preSave()){
				this.service.save(this.toSave);
				this.toSave = this.getNewEntity();
				return this.load();
			}//if
		} catch (ServicesException e) {
			getFacesUtil().createErrorMessage(e);
		}//try
		return OUTCOME.SAME_PAGE.getValue();
	}

	private FacesUtil getFacesUtil() {
		return FacesUtil.getInstance();
	}

	/**
	 * @category MB action
	 */
	public String edit() {
		this.toSave = this.dm.getRowData();
		return OUTCOME.SAME_PAGE.getValue();
	}

	/**
	 * @category MB action
	 */
	public String remove() {
		try {
			this.toRemove = this.dm.getRowData();
			this.service.remove(toRemove);
			this.toRemove = null;
		} catch (ServicesException e) {
			getFacesUtil().createErrorMessage(e);
		}// try
		return this.load();
	}

	@PostConstruct
	protected void init() {
		this.toSave = this.getNewEntity();
		this.service = this.getService();
		this.load();
	}

	private enum OUTCOME {
		SAME_PAGE(null);
		private String value;

		OUTCOME(String $value) {
			this.value = $value;
		}

		public String getValue() {
			return this.value;
		}
	}

	public E getToSave() {
		return toSave;
	}

	public void setToSave(E toSave) {
		this.toSave = toSave;
	}

	public E getToEdit() {
		return toEdit;
	}

	public void setToEdit(E toEdit) {
		this.toEdit = toEdit;
	}

	public E getToRemove() {
		return toRemove;
	}

	public void setToRemove(E toRemove) {
		this.toRemove = toRemove;
	}

	public DataModel<E> getDm() {
		return dm;
	}

}
