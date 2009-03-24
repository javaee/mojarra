package services;

import java.util.LinkedList;
import java.util.List;

import dao.DAOException;
import dao.DAOFactory;
import dao.StoryDAO;
import entities.Sprint;
import entities.Story;

public class StoryService extends AppCrudService<StoryDAO, Story> {

	@Override
	public StoryDAO getDAO() {
		return DAOFactory.getInstance().getStoryDAO();
	}

	public List<Story> findBySprint(Sprint $sprint) throws ServicesException {
		List<Story> stories = new LinkedList<Story>();
		try {
			stories = this.getDAO().findBySprint($sprint);
		} catch (DAOException e) {
			throw new ServicesException(e);
		}//try
		return stories;
	}

}
