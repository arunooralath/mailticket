package pojo;

import java.util.Date;

public class Fields {

	private String summary;
	private Project project;
	private Issuetype issuetype;
	private String description;
	private Assignee assignee;
	private Components[] components;
	private Priority priority;
	private String duedate;

	public Fields(String summary, Project project, Issuetype issuetype, String description, Assignee assignee,
			Components[] components, Priority priority, String duedate) {
		super();
		this.summary = summary;
		this.project = project;
		this.issuetype = issuetype;
		this.description = description;
		this.assignee = assignee;
		this.components = components;
		this.priority = priority;
		this.duedate = duedate;
	}

}
