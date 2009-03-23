package dao.memory;

import java.util.LinkedList;
import java.util.List;

import dao.DAOException;
import dao.TaskDAO;
import entities.Sprint;
import entities.Story;
import entities.Task;

public class TaskDAOMemory extends GenericDAOMemory<Task> implements TaskDAO {
	@Override
	public List<Task> findByStory(Story $story) throws DAOException {
		List<Task> tasks = new LinkedList<Task>();
		List<Task> allTasks = this.findAll();
		for (Task task: allTasks){
			if (task.getStory().equals($story)){
				tasks.add(task);
			}//if
		}//for
		return tasks;
	}

	@Override
	public String getPersistenceFileName() {
		return Sprint.class.getName()+".out";
	}
	
}
