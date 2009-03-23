package dao;

import entities.Sprint;

public interface SprintDAO extends AbstractEntityDAO<Sprint> {
	public void saveAll(Sprint $sprint) throws DAOException;	
}
