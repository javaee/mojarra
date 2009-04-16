package dao.jpa;

import java.util.List;

import dao.DAOException;

public interface IFGenericDAOJPA<PK, E> {
	public List<E> findAll() throws DAOException;

	public List<E> findByProperty(String $propertyName, Object $value)
			throws DAOException;

	public E findById(PK $id) throws DAOException;

	public List<E> find(String $jpaQL, int $limit,
			Parametro... $parametros) throws DAOException;

	public void save(E $e) throws DAOException;

	public void remove(E $e) throws DAOException;

}
