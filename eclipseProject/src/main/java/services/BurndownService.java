package services;

import java.util.LinkedList;
import java.util.List;

import dao.DAOException;
import dao.DAOFactory;
import dao.SprintDAO;
import entities.Sprint;

public class BurndownService extends AppService {

	public SprintDAO getDAO() {
		return DAOFactory.getInstance().getSprintDAO();
	}

	public Sprint findById(Integer $id) throws ServicesException {
		Sprint sprint = null;
		try {
			Sprint sprintParaBusca = new Sprint();
			sprintParaBusca.setId($id);
			sprint = this.getDAO().findById(sprintParaBusca);
		} catch (DAOException e){
			throw new ServicesException(e);
		}//try
		return sprint;
	}
	
	public List<Sprint> findSprints() throws ServicesException{
		List<Sprint> sprints = new LinkedList<Sprint>();
		try {
			sprints = this.getDAO().findAll();
		} catch (DAOException e) {
			throw new ServicesException(e);
		}//try
		return sprints;
	}
}
