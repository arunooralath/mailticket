package pojo;

public class Issue {

//	 response
	private long id;
	private String self;	
	private String key;

	// request
	private Fields fields;
	
//	constructor
	public Issue(Fields fields) {
		super();
		this.fields = fields;
	}


	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getSelf() {
		return self;
	}

	public void setSelf(String self) {
		this.self = self;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

}
