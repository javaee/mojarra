package services;

import java.util.LinkedList;
import java.util.List;

import dao.DAOException;
import dao.DAOFactory;
import dao.TaskDAO;
import entities.Story;
import entities.Task;

public class TaskService extends AppCrudService<TaskDAO, Task> {

	@Override
	public TaskDAO getDAO() {
		return DAOFactory.getInstance().getTaskDAO();
	}

	public List<Task> findByStory(Story $story) throws ServicesException {
		List<Task> tasks = new LinkedList<Task>();
		try {
			tasks = this.getDAO().findByStory($story);
		} catch (DAOException e) {
			throw new ServicesException(e);
		}//try
		return tasks;
	}
	
	
}
