package assignments.mining;

import java.util.ArrayList;

public class Project {
	
	private ArrayList<Project> prereq = new ArrayList<Project>();
	private int value, position;
	
	public Project(int value, int position) {
		this.value = value;
	}

}
