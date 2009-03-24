package services;

import dao.DAOFactory;
import dao.SprintDAO;
import entities.Sprint;

public class SprintService extends AppCrudService<SprintDAO, Sprint> {

	@Override
	public SprintDAO getDAO() {
		return DAOFactory.getInstance().getSprintDAO();
	}

	
}
