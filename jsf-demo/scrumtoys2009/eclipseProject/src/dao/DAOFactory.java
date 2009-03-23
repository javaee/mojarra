package dao;

import dao.jpa.DAOFactoryOptionJPA;

public class DAOFactory {
	
	private static DAOFactory instance;
	private IFDAOFactoryOption daoFactoryOption = DAOFactoryOptionJPA.getInstance();
//	private IFDAOFactoryOption daoFactoryOption = DAOFactoryOptionMemory.getInstance();
	public static DAOFactory getInstance() {
		if (instance == null)
			instance = new DAOFactory();
		return instance;
	}

	private DAOFactory(){}
	
	public TaskDAO getTaskDAO(){
		return this.daoFactoryOption.getTaskDAO();
	}
	
	public StoryDAO getStoryDAO(){
		return this.daoFactoryOption.getStoryDAO();
	}
	
	public SprintDAO getSprintDAO(){
		return this.daoFactoryOption.getSprintDAO();
	}
	
}
