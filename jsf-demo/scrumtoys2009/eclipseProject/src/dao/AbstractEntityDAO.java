package dao;

import java.util.List;

import entities.AbstractEntity;

public interface AbstractEntityDAO<E extends AbstractEntity> {

	public List<E> findAll() throws DAOException;
	public void save(E $e) throws DAOException;
	public void remove(E $e) throws DAOException;
	public E findById(E $e) throws DAOException;
}
