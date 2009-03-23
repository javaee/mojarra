package dao.jpa;

import dao.IFDAOFactoryOption;
import dao.SprintDAO;
import dao.StoryDAO;
import dao.TaskDAO;

public class DAOFactoryOptionJPA implements IFDAOFactoryOption {
	
	private static DAOFactoryOptionJPA instance;
	private TaskDAO taskDAO = TaskDAOJPA.getInstance();
	private StoryDAO storyDAO = StoryDAOJPA.getInstance();
	private SprintDAO sprintDAO = SprintDAOJPA.getInstance();
	
	public static synchronized DAOFactoryOptionJPA getInstance() {
		if (instance == null)
			instance = new DAOFactoryOptionJPA();
		return instance;
	}

	private DAOFactoryOptionJPA(){}
	
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
