package program2;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Expense {

	private String group;
	private String typ;
	private Double cost;
	private LocalTime time;

	public String getgroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getTyp() {
		return typ;
	}

	public void setTyp(String typ) {
		this.typ = typ;
	}

	public Double getCost() {
		return cost;
	}

	public void setCost(Double cost) {
		this.cost = cost;
	}

	public LocalTime getTime() {
		return time;
	}

	public void setTime(LocalTime time) {
		this.time = time;
	}

	public Expense() {

	}

	public Expense(String group, Double cost) {
		super();
		this.group = group;
		this.cost = cost;
	}

	public Expense(String group, String typ, Double cost, LocalTime time) {
		super();
		this.group = group;
		this.typ = typ;
		this.cost = cost;
		this.time = time;
	}

	@Override
	public String toString() {
		String s;
		if(typ==null){
		s = "All costs: "+group +": "  + cost;
		}else {

		s = "group: " + group + " typ: " + typ + " cost: " + cost + " € "
				+ time.format(DateTimeFormatter.ofPattern("HH:mm"));
		}

		return s;
	}

	@Override
	public int hashCode() {
		return Objects.hash(cost, time, group, typ);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Expense other = (Expense) obj;
		return Objects.equals(cost, other.cost) && Objects.equals(time, other.time)
				&& Objects.equals(group, other.group) && Objects.equals(typ, other.typ);
	}

}
