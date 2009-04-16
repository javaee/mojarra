package dao.memory;

import java.util.List;

import dao.DAOException;
import dao.DAOFactory;
import dao.SprintDAO;
import entities.Sprint;
import entities.Story;
import entities.Task;

public class SprintDAOMemory extends GenericDAOMemory<Sprint> implements SprintDAO {

	@Override
	public String getPersistenceFileName() {
		return Sprint.class.getName()+".out";
	}
	
	
	public Sprint findAllById(Sprint $s) throws DAOException {
		List<Story> stories = DAOFactory.getInstance().getStoryDAO().findBySprint($s);
		$s.setStories(stories);
		for (Story story: stories){
			List<Task> tasks = DAOFactory.getInstance().getTaskDAO().findByStory(story);
			story.setTasks(tasks);
		}//for
		return $s;
	}


	public void saveAll(Sprint $sprint) throws DAOException {
		$sprint.setId(0);
		this.save($sprint);
		for (Story story: $sprint.getStories()){
			DAOFactory.getInstance().getStoryDAO().save(story);
			for (Task task: story.getTasks()){
				DAOFactory.getInstance().getTaskDAO().save(task);				
			}//for
		}//for
	}


	@Override
	public List<Sprint> findAllOrderBy(String... $properties)
			throws DAOException {
		// TODO Auto-generated method stub
		return null;
	}

}
