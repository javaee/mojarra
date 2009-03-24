package dao;

import java.util.List;

import entities.Story;
import entities.Task;

public interface TaskDAO extends AbstractEntityDAO<Task> {
	public List<Task> findByStory(Story $story) throws DAOException;
}
