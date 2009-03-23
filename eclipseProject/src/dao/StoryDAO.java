package dao;

import java.util.List;

import entities.Sprint;
import entities.Story;

public interface StoryDAO extends AbstractEntityDAO<Story> {

	public List<Story> findBySprint(Sprint $sprint) throws DAOException;
}
