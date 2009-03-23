package services;

import dao.DAOException;

public class ServicesException extends Exception {

	private static final long serialVersionUID = 3144433177433995614L;

	public ServicesException(String $mensagem, Exception $e) {
		super($mensagem, $e);
	}

	public ServicesException(DAOException $e) {
		this($e.getLocalizedMessage(), $e);
	}
}
