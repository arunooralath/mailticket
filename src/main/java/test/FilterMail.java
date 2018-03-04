package test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMultipart;

import pojo.Assignee;
import pojo.Components;
import pojo.Fields;
import pojo.Issue;
import pojo.Issuetype;
import pojo.Priority;
import pojo.Project;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static utils.Constants.*;

public class FilterMail {

	private Message[] mails;
	private int mailCount;
	private String subject;
	private String msgBody;

	public FilterMail(Message[] mails) {
		// TODO Auto-generated constructor stub
		this.mails = mails;
		this.mailCount = mails.length;
	}

	public void filterService() throws InterruptedException {
		// check for issues in the last 10 mails
		// loop into last 10 mails in the inbox

		for (int n = mailCount; n > mailCount - 10; n--) {
			try {
				subject = mails[n - 1].getSubject();

				if (subject.equals(NE_REQ)) {
					generateFDCCNERTicket(mails[n - 1]);

				} else if (subject.equals(DMS_SR)) {
					generateDMS_SRTicket(mails[n - 1]);

				} else if (subject.equals(CANCEL_REQ)) {
					generateCANCEL_REQTicket(mails[n - 1]);

				} else if (subject.equals(BUY_SELL_REQ)) {
					generateBUY_SELL_REQTicket(mails[n - 1]);

				} else if (subject.equals(BRONZE_UG_REQ)) {
					generateBRONZE_UG_REQTicket(mails[n - 1]);

				} else if (subject.equals(BRONZE_DG_REQ)) {
					generateBRONZE_DG_REQTicket(mails[n - 1]);

				} else if (subject.equals(RDI)) {
					generateRDITicket(mails[n - 1]);

				}

			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	private void generateRDITicket(Message message) throws MessagingException, IOException {
		// Create RDI Ticket
		msgBody = getTextFromMessage(message);
		createApiTicket(RDI, msgBody, JIRA_PROJECT, TYPE_TASK, RADHIKA, ENROLLMENT);
	}

	private void generateBRONZE_DG_REQTicket(Message message) throws MessagingException, IOException {
		// Create Ticket for Bronze DownGrade
		msgBody = getTextFromMessage(message);
		createApiTicket(BRONZE_DG_REQ, msgBody, JIRA_PROJECT, TYPE_TASK, RADHIKA, ENROLLMENT);
	}

	private void generateBRONZE_UG_REQTicket(Message message) throws MessagingException, IOException {
		// Create Ticket for Bronze Upgrade
		msgBody = getTextFromMessage(message);
		createApiTicket(BRONZE_UG_REQ, msgBody, JIRA_PROJECT, TYPE_TASK, RADHIKA, ENROLLMENT);
	}

	private void generateBUY_SELL_REQTicket(Message message) throws MessagingException, IOException {
		// Create Ticket for Buy Sell Request
		msgBody = getTextFromMessage(message);
		createApiTicket(BUY_SELL_REQ, msgBody, JIRA_PROJECT, TYPE_TASK, RADHIKA, ENROLLMENT);
	}

	private void generateCANCEL_REQTicket(Message message) throws MessagingException, IOException {
		// Create Ticket for Cancellation Request
		msgBody = getTextFromMessage(message);
		createApiTicket(CANCEL_REQ, msgBody, JIRA_PROJECT, TYPE_TASK, RADHIKA, ENROLLMENT);
	}

	private void generateDMS_SRTicket(Message message) throws MessagingException, IOException {
		// Create ticket for DMS Switch
		msgBody = getTextFromMessage(message);
		createApiTicket(DMS_SR, msgBody, JIRA_PROJECT, TYPE_TASK, RADHIKA, ENROLLMENT);
	}

	private void generateFDCCNERTicket(Message message) throws MessagingException, IOException {
		// Create ticket for New Enrolment
		msgBody = getTextFromMessage(message);
		createApiTicket(NE_REQ, msgBody, JIRA_PROJECT, TYPE_TASK, RADHIKA, ENROLLMENT);
	}

	// common function to make asynchronous api call to server
	private void createApiTicket(String summary, String description, String projectName, String issueType,
			String assigneeName, String component) throws UnsupportedEncodingException {

		// set the POJO constructor for retrofit
		Issuetype issuetype = new Issuetype(issueType);
		Project project = new Project(projectName);
		Assignee assignee = new Assignee("alisyed");
		Components[] components = new Components[] { new Components(component) };
		Priority priority = new Priority("1");

		// Set the date with 10 days ahead of current date
		Date d = new Date();// intialize your date to any date
		Date duedate = new Date(d.getTime() + 10 * 24 * 3600 * 1000l);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String formattedDate = formatter.format(duedate);
		System.out.println(summary + "-" + formattedDate);

		Fields fields = new Fields(summary, project, issuetype, description, assignee, components, priority,
				formattedDate);
		Issue issue = new Issue(fields);

		// Set the api basic authentication with Username and Password
		String base = JIRA_USERNAME + ":" + JIRA_PASSWORD;
		String authHeader = "Basic " + Base64.getEncoder().encodeToString(base.getBytes("utf-8"));

		UserService service = ServiceGenerator.createService(UserService.class);
		Call<Issue> callAsync = service.createIssue(authHeader, issue);
		callAsync.enqueue(new Callback<Issue>() {

			public void onResponse(Call<Issue> call, Response<Issue> response) {
				Issue issue = response.body();
				if (response.code() == 201) {
					System.out.println("Responsecode -" + response.code());
					System.out.println(issue.getId());

				} else
					System.out.println("Error in APi call -" + response.code());
			}

			public void onFailure(Call<Issue> call, Throwable throwable) {
				System.out.println(throwable);
			}

		});
	}

	// Function to extract body from email
	private static String getTextFromMessage(Message message) throws MessagingException, IOException {
		String result = "";
		if (message.isMimeType("text/plain")) {
			System.out.println("Message type text/plain");
			result = message.getContent().toString();
		} else if (message.isMimeType("multipart/*")) {
			MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
			result = getTextFromMimeMultipart(mimeMultipart);
		}
		return result;
	}

	// function to extract mime from email
	private static String getTextFromMimeMultipart(MimeMultipart mimeMultipart) throws MessagingException, IOException {
		String result = "";
		int count = mimeMultipart.getCount();
		for (int i = 0; i < count; i++) {
			BodyPart bodyPart = mimeMultipart.getBodyPart(i);
			if (bodyPart.isMimeType("text/plain")) {
				result = result + "\n" + bodyPart.getContent();
				break; // without break same text appears twice in my tests
			} else if (bodyPart.isMimeType("text/html")) {
				String html = (String) bodyPart.getContent();
				result = result + "\n" + org.jsoup.Jsoup.parse(html).text();
			} else if (bodyPart.getContent() instanceof MimeMultipart) {
				result = result + getTextFromMimeMultipart((MimeMultipart) bodyPart.getContent());
			}
		}
		return result;
	}

}