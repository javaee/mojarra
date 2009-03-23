package dao;

import java.util.List;

import entities.AbstractEntity;

public interface IFGenericDAO<E extends AbstractEntity>   {
	public List<E> findAll() throws DAOException;
	public E findById(Long $id) throws DAOException;
	public void save(E $e) throws DAOException;
	public void create(E $e) throws DAOException;
	public void edit(E $e) throws DAOException;
	public void remove(final E $e) throws DAOException;
}
