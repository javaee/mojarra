package dao.jpa;

import dao.DAOException;
import dao.SprintDAO;
import entities.Sprint;


public class SprintDAOJPA extends GenericDAOJPA<Long, Sprint> implements
		SprintDAO {

	private static SprintDAOJPA instance;

	private SprintDAOJPA() {
		super(ApplicationPersistenceUnits.DEFAULT.getUnitName(), Sprint.class);
	}

	public synchronized static SprintDAOJPA getInstance() {
		if (instance == null)
			instance = new SprintDAOJPA();
		return instance;
	}

	@Override
	public void saveAll(Sprint $sprint) throws DAOException {
		this.save($sprint);
	}

	@Override
	public Sprint findById(Sprint $e) throws DAOException {
		return this.findById($e.getId());
	}


}
