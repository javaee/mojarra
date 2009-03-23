package dao.memory;

import java.util.LinkedList;
import java.util.List;

import dao.DAOException;
import dao.StoryDAO;
import entities.Sprint;
import entities.Story;

public class StoryDAOMemory extends GenericDAOMemory<Story> implements StoryDAO {

	@Override
	public List<Story> findBySprint(Sprint $sprint) throws DAOException {
		List<Story> stories = new LinkedList<Story>();
		List<Story> allStories = this.findAll();
		for (Story story: allStories){
			if (story.getSprint().equals($sprint)){
				stories.add(story);
			}//if
		}//for
		return stories;
	}

	@Override
	public String getPersistenceFileName() {
		return Sprint.class.getName()+".out";
	}

}
