package dao;

public interface IFDAOFactoryOption {
	public TaskDAO getTaskDAO();
	public StoryDAO getStoryDAO();
	public SprintDAO getSprintDAO();
}
