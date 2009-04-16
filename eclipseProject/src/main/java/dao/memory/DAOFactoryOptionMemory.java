package dao.memory;

import java.util.List;

import dao.DAOException;
import dao.IFDAOFactoryOption;
import dao.SprintDAO;
import dao.StoryDAO;
import dao.TaskDAO;
import entities.Sprint;
import entities.sample.SampleSprint;

public class DAOFactoryOptionMemory implements IFDAOFactoryOption {
	
	private static DAOFactoryOptionMemory instance;
	private TaskDAO taskDAO = new TaskDAOMemory();
	private StoryDAO storyDAO = new StoryDAOMemory();
	private SprintDAO sprintDAO = new SprintDAOMemory();
	
	public static synchronized DAOFactoryOptionMemory getInstance() {
		if (instance == null)
			instance = new DAOFactoryOptionMemory();
			instance.saveSampleSprint();
		return instance;
	}

	private void saveSampleSprint()  {
		try {
			List<Sprint> list = this.sprintDAO.findAll();
			if (list.size() == 0){
				Sprint sprint = new SampleSprint();
				this.sprintDAO.saveAll(sprint);
			}//if
		} catch (DAOException e) {
			e.printStackTrace();
		}//try
	}	
	
	private DAOFactoryOptionMemory(){}
	
	public TaskDAO getTaskDAO(){
		return this.taskDAO;
	}
	
	public StoryDAO getStoryDAO(){
		return this.storyDAO;
	}
	
	public SprintDAO getSprintDAO(){
		return this.sprintDAO;
	}
	
}
