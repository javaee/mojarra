package dao;
import java.util.List;

import junit.framework.TestCase;
import entities.Sprint;
import entities.Story;
import entities.Task;


public class TestSprintDAO extends TestCase {

	private SprintDAO sprintDAO = DAOFactory.getInstance().getSprintDAO();
	
	public void testFindById() throws DAOException {
		
	}
	public void testFindAll() throws DAOException {
		List<Sprint> sprints = this.sprintDAO.findAll();
		for (Sprint sprint: sprints){
			System.out.printf("%s (%d) %n %n", sprint.getName(), sprint.getId());
			for (Story story: sprint.getStories()){
				System.out.printf("%s (%d) %n", story.getName(), story.getId());
				for (Task task: story.getTasks()){
					System.out.printf("%s (%d) %n", task.getName(), task.getId());
				}//for
			}//for
		}//for
	}
	
	public void testSave() throws DAOException{
	}
}
