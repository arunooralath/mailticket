package test;

import javax.mail.Message;
import javax.mail.MessagingException;

public class MailTest {

	private static MailTest mail;
	private static Message[] mails;
	private static String subject;

	public static void main(String[] args) throws MessagingException, InterruptedException {
		// TODO Auto-generated method stub
		MailService ms = new MailService("alisyed7592", "Family786");
		mails = ms.readMail();

		FilterMail fm = new FilterMail(mails);
		fm.filterService();

	}

}
