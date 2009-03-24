package entities;

import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @category domain entity
 * @author Vinny
 * @ since 2008
 *
 */
public class Meeting extends AbstractEntity {
	private static final long serialVersionUID = 1457577582305931954L;
	private Date date;
	private String report;
	private int estimatedDuration;
	private int actualDuration;
	
	public Meeting(int $year, int $month, int $date){
		this.date = new GregorianCalendar($year, $month, $date).getTime();
	}
	public boolean isOutOfTime(){
		return this.actualDuration > this.estimatedDuration;
	}

	public Date getDate() {
		return this.date;
	}

	public void setDate(Date $date) {
		this.date = $date;
	}

	public String getReport() {
		return this.report;
	}

	public void setReport(String $report) {
		this.report = $report;
	}

	public int getEstimatedDuration() {
		return this.estimatedDuration;
	}

	public void setEstimatedDuration(int $estimatedDuration) {
		this.estimatedDuration = $estimatedDuration;
	}

	public int getActualDuration() {
		return this.actualDuration;
	}

	public void setActualDuration(int $actualDuration) {
		this.actualDuration = $actualDuration;
	}
}
