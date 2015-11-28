package pojos;

import email.SendMail;
import utility.GlobalConstants;

public class MailNotifierThread extends Thread {
	private String messageBody= null;
	private String receiverEmail = null;
	private String subject = null;
	public MailNotifierThread(String messageBody, String receiverEmail, String subject) {
		super();
		this.messageBody = messageBody;
		this.receiverEmail = receiverEmail;
		this.subject = subject;
	}
	@Override
	public void run(){
		String[] to = { receiverEmail };
		SendMail.sendEmail(GlobalConstants.FROM_EMAIL,
				GlobalConstants.PASSWORD_EMAIL, subject, messageBody,
				to);
	}

}
