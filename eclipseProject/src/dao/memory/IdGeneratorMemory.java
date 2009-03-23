package dao.memory;

import java.util.HashMap;
import java.util.Map;

import entities.AbstractEntity;


public class IdGeneratorMemory {
	private static IdGeneratorMemory instance;

	public static IdGeneratorMemory getInstance() {
		if (instance == null)
			instance = new IdGeneratorMemory();
		return instance;
	}

	@SuppressWarnings("unchecked")
	private Map<Class, Integer> map;

	@SuppressWarnings("unchecked")
	private IdGeneratorMemory() {
		this.map = new HashMap<Class, Integer>();
	}

	public void nextId(AbstractEntity $identificado) {
		Integer contador = this.map.get($identificado.getClassForIdGenerator());
		if (contador == null)
			contador = new Integer(0);
		this.map.put($identificado.getClassForIdGenerator(), ++contador);
		$identificado.setId(contador);
	}
}
