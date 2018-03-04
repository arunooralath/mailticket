package test;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;

public class MailService {

	private String userName;
	private String password;
	
	public MailService(String userName, String password) {
		super();
		this.userName = userName;
		this.password = password;
	}

	public Message[] readMail() {

		Properties props = new Properties();

		try {
			// Load the properties file from resource folder
			props.load(new FileInputStream(new File("src/main/resources/config.properties")));

			Session session = Session.getDefaultInstance(props, null);
			Store store = session.getStore("imaps");

			// connect to store using smtp
			store.connect("smtp.gmail.com", userName, password);

			// read the folder inbox
			Folder inbox = store.getFolder("inbox");
			inbox.open(Folder.READ_ONLY);

			// get the total message count
			int messageCount = inbox.getMessageCount();
			System.out.println("Total Messages:- " + messageCount);

			// copy the messages to an array of type message
			Message[] messages = inbox.getMessages();				

//			inbox.close(true);
//			store.close();
			return messages;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
}
