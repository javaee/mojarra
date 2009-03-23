package dao.jpa;

import java.util.List;

import dao.DAOException;
import dao.TaskDAO;
import entities.Story;
import entities.Task;


public class TaskDAOJPA extends GenericDAOJPA<Long, Task> implements
		TaskDAO {

	private static TaskDAOJPA instance;

	private TaskDAOJPA() {
		super(ApplicationPersistenceUnits.DEFAULT.getUnitName(), Task.class);
	}

	public synchronized static TaskDAOJPA getInstance() {
		if (instance == null)
			instance = new TaskDAOJPA();
		return instance;
	}

	@Override
	public List<Task> findByStory(Story $story) throws DAOException {
		return $story.getTasks();
	}

	@Override
	public Task findById(Task $e) throws DAOException {
		return this.findById($e.getId());
	}

}
