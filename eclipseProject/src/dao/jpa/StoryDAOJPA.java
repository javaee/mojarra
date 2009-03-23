package dao.jpa;

import java.util.List;

import dao.DAOException;
import dao.StoryDAO;
import entities.Sprint;
import entities.Story;


public class StoryDAOJPA extends GenericDAOJPA<Long, Story> implements
		StoryDAO {

	private static StoryDAOJPA instance;

	private StoryDAOJPA() {
		super(ApplicationPersistenceUnits.DEFAULT.getUnitName(), Story.class);
	}

	public synchronized static StoryDAOJPA getInstance() {
		if (instance == null)
			instance = new StoryDAOJPA();
		return instance;
	}

	@Override
	public List<Story> findBySprint(Sprint $sprint) throws DAOException {
		return $sprint.getStories();
	}

	@Override
	public Story findById(Story $e) throws DAOException {
		return this.findById($e.getId());
	}


}
