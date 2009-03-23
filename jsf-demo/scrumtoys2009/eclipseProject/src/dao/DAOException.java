package dao;

public class DAOException extends Exception {

	private static final long serialVersionUID = 3144433177433995614L;

	public DAOException(String $mensagem, Exception $e) {
		super($mensagem, $e);
	}
}
