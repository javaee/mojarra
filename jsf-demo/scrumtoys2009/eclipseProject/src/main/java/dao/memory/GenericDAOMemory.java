package dao.memory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import dao.DAOException;
import entities.AbstractEntity;


public abstract class GenericDAOMemory<E extends AbstractEntity>  {
	
	private Map<Long,E> mapa = new HashMap<Long,E>();
	
	
	@SuppressWarnings("unchecked")
	public GenericDAOMemory() {
		String fileName = this.getPersistenceFileName();
		File file = new File(fileName);
		if (file.exists() && file.isFile() && file.canRead()){
			FileInputStream fis;
			try {
				fis = new FileInputStream(file);
				System.out.println("Path: "+file.getAbsolutePath());
				ObjectInputStream ois = new ObjectInputStream(fis);
				Object obj = ois.readObject();
				if (obj instanceof Map){
					this.mapa = (Map<Long,E>) obj;
				}//if
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}//if
	}
	
	public abstract String getPersistenceFileName();
	
	public E findById(E $e) throws DAOException {
		System.out.println(String.format("Entidade %s recuperada com sucesso.", $e.getId()));		
		return this.mapa.get($e.getId());
	}

	public List<E> findAll() throws DAOException {
		System.out.println(String.format("Colecao de entidades recuperada com sucesso."));
		return new LinkedList<E>(this.mapa.values());
	}
	
	public void remove(E $e) throws DAOException {
		this.mapa.remove($e.getId());
		System.out.println(String.format("Entidade %s removida com sucesso.", $e.getId()));
	}

	public void save(E $e) throws DAOException {
		if ($e.getId() == 0) IdGeneratorMemory.getInstance().nextId($e);
		this.mapa.put($e.getId(), $e);
		System.out.println(String.format("Entidade %s salva com sucesso.", $e.getId()));
	}
}
