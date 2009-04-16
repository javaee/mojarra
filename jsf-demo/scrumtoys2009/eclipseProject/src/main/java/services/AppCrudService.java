package services;

import java.util.LinkedList;
import java.util.List;

import dao.AbstractEntityDAO;
import dao.DAOException;
import entities.AbstractEntity;

public abstract class AppCrudService<D extends AbstractEntityDAO<E>, E extends AbstractEntity> {
	
	private D dao;
	
	public AppCrudService(){
		this.dao = this.getDAO();
	}

	public abstract D getDAO();
	
	
	public List<E> findAll() throws ServicesException {
		List<E> list = new LinkedList<E>();
		try {
			list = this.dao.findAll();
		} catch (DAOException e) {
			throw new ServicesException(e);
		}//try
		return list;
	}
	public void save(E $e) throws ServicesException {
		try {
			this.dao.save($e);
		} catch (DAOException e) {
			throw new ServicesException(e);
		}//try
	}

	public void remove(E $e) throws ServicesException {
		try {
			this.dao.remove($e);
		} catch (DAOException e) {
			throw new ServicesException(e);
		}//try
	}

}
