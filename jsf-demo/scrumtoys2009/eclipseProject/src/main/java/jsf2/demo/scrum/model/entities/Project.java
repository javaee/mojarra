package jsf2.demo.scrum.model.entities;

import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Dr. Spock (spock at dev.java.net)
 */
@Entity
@Table(name = "projects")
@NamedQueries({@NamedQuery(name = "project.getAll", query = "select p from Project as p"),
    @NamedQuery(name = "project.getAllOpen", query = "select p from Project as p where p.endDate is null"),
    @NamedQuery(name = "project.countByName", query = "select count(p) from Project as p where p.name = :name and not(p = :currentProject)"),
    @NamedQuery(name = "project.new.countByName", query = "select count(p) from Project as p where p.name = :name")})
public class Project extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Column(nullable = false, unique = true)
    private String name;
    @Temporal(TemporalType.DATE)
    @Column(name = "start_date", nullable = false)
    private Date startDate;
    @Temporal(TemporalType.DATE)
    @Column(name = "end_date")
    private Date endDate;
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<Sprint> sprints;

    public Project() {
        this.startDate = new Date();
    }

    public Project(String name) {
        this();
        this.name = name;
    }

    public Project(String name, Date startDate) {
        this(name);
        this.startDate = startDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public List<Sprint> getSprints() {
        return (sprints != null) ? Collections.unmodifiableList(sprints) : Collections.EMPTY_LIST;
    }

    public boolean addSprint(Sprint sprint) {
        if (sprints == null) {
            sprints = new LinkedList<Sprint>();
        }
        if (sprint != null && !sprints.contains(sprint)) {
            sprints.add(sprint);
            sprint.setProject(this);
            return true;
        }
        return false;
    }

    public boolean removeSpring(Sprint sprint) {
        if (sprints != null && !sprints.isEmpty()) {
            return sprints.remove(sprint);
        } else {
            return false;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Project other = (Project) obj;
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + (this.name != null ? this.name.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "Project[name=" + name + ",startDate=" + startDate + ",endDate=" + endDate + "]";
    }
}
