package dao.jpa;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import dao.DAOException;
import entities.AbstractEntity;


public class GenericDAOJPA<PK,E extends AbstractEntity> {

	private EntityManager em;
	private Class<E> clazz;
	public GenericDAOJPA(String $unitName, Class<E> $clazz) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory($unitName);
		this.em = emf.createEntityManager();
		this.clazz = $clazz;
	}
	
	@Override
	protected void finalize() throws Throwable {
		this.em.close();
		this.em = null;
	}

	public List<E> findAll() throws DAOException {
		String jpaQL = String.format("SELECT m FROM %s AS m", this.clazz.getSimpleName());
		return this.find(jpaQL, -1);
	}

	public List<E> findAllOrderBy(String[] $properties) throws DAOException {
		StringBuilder orderByList = new StringBuilder();
		int counter = 0;
		for (String property: $properties){
			String str = counter == 0?"m."+property:",m."+property;
			orderByList.append(str);
			counter++;
		}//for
		String jpaQL = String.format("SELECT m FROM %s AS m ORDER BY %s", this.clazz.getSimpleName(), orderByList);
		return this.find(jpaQL, -1);
	}
	
 	public List<E> findByProperty(String $propertyName, Object $value) throws DAOException {  
    	String nomeDoParametro = "valor";
		String jpaQL = String.format("SELECT m FROM %s AS m where m.%s = %s", this.clazz.getSimpleName(),$propertyName, ":"+nomeDoParametro);
		return this.find(jpaQL, -1, new Parametro("valor",$value));
    }  

	
	public E findById(PK $id) throws DAOException {
		return (E) this.em.find(this.clazz, $id);
	}
	
	@SuppressWarnings("unchecked")
	public List<E> find(String $jpaQL, int $limit, Parametro ... $parametros) throws DAOException {
		List<E> lista = new LinkedList<E>();
		try {
			javax.persistence.Query query = this.em.createQuery($jpaQL);
			if ($limit >= 0) query.setMaxResults($limit);
			for(Parametro parametro: $parametros) {
				query.setParameter(parametro.getChave(), parametro.getValor());
			}//for
			lista = query.getResultList();
		} catch (RuntimeException e) {
			e.printStackTrace();
			throw new DAOException("Erro ao consultar entidades", e);
		}//try
		return lista;
	}

	public void save(E $e) throws DAOException {
		if ($e.getId() == 0){
			this.create($e);
		} else {
			this.edit($e);
		}//if
	}
	
	
	public void create(E $e) throws DAOException {
		this.em.getTransaction().begin();
		try {
			this.em.persist($e);
			this.em.getTransaction().commit();
		} catch (RuntimeException exception) {
			exception.printStackTrace();
			this.em.getTransaction().rollback();
			throw new DAOException("Erro ao criar entidade", exception);
		}//try
	}

	public void edit(E $e) throws DAOException {
		this.em.getTransaction().begin();
		try {
			$e = this.em.merge($e);
			this.em.getTransaction().commit();
		} catch (RuntimeException exception) {
			exception.printStackTrace();
			this.em.getTransaction().rollback();
			throw new DAOException("Erro ao editar entidade", exception);
		}//try
	}
	
	public void remove(E $e) throws DAOException {
		this.em.getTransaction().begin();
		try {
			$e = this.em.merge($e);
			this.em.remove($e);
			this.em.getTransaction().commit();
		} catch (RuntimeException exception) {
			exception.printStackTrace();
			this.em.getTransaction().rollback();
			throw new DAOException("Erro ao remover entidade", exception);
		}//try
	}
	
	   
}

class Parametro{
	private String chave;
	private Object valor;
	
	public Parametro(String $chave, Object $valor) {
		super();
		this.chave = $chave;
		this.valor = $valor;
	}
	public String getChave() {
		return this.chave;
	}
	public Object getValor() {
		return this.valor;
	}
}
