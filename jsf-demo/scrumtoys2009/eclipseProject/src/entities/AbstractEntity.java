package entities;

import java.io.Serializable;

public class AbstractEntity implements Serializable {

	private static final long serialVersionUID = 6324183460883510746L;
	private long id;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (this.id ^ (this.id >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractEntity other = (AbstractEntity) obj;
		if (this.id != other.id)
			return false;
		return true;
	}

	public long getId() {
		return this.id;
	}

	public void setId(long $id) {
		this.id = $id;
	}

	@SuppressWarnings("unchecked")
	public Class getClassForIdGenerator() {
		return this.getClass();
	}
}
