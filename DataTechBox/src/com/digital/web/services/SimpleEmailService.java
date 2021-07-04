package com.digital.web.services;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * @author swapnilsarwade
 *
 */
public class SimpleEmailService implements EmailService {

	private String username;
	
	private String password;
	
	private String from;
	
	private String[] recipients;
	
	private String subject;
	
	private String message;
	
	private Properties configProperties;
	
	public Properties getConfigProperties() {
		return configProperties;
	}

	public void setConfigProperties(Properties configProperties) {
		this.configProperties = configProperties;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String[] getRecipients() {
		return recipients;
	}

	public void setRecipients(String... recipients) {
		this.recipients = recipients;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	private Session session;
	
	public Session getEmailSession() {
		
		if (session == null) {
			session = Session.getInstance(getConfigProperties(), new Authenticator() {
				@Override
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(getUsername(), getPassword());
				}
			});
		}
		
		return session;
	}
	
	private MimeMessage mimeMessage;
	
	private MimeMessage getMimeMessage() {
		if (mimeMessage == null) {
			mimeMessage = new MimeMessage(this.getEmailSession());
		}
		return mimeMessage;
	}
	
	private void setMimeMessage(MimeMessage mime) {
		this.mimeMessage = mime;
	}
	
	private Map<String, String> headers;
	
	public Map<String, String> getHeaders() {
		return headers;
	}
	
	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}
	
	/* (non-Javadoc)
	 * @see com.digital.web.services.EmailService#sendEmail()
	 */
	@Override
	public synchronized void sendEmail() {

		//addHeaders();
/*		
		Session session = Session.getInstance(getConfigProperties(), new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(getUsername(), getPassword());
			}
		});
*/		
		try {
			InternetAddress recps[] = new InternetAddress[getRecipients().length];
			
			for (int i = 0; i < getRecipients().length; i++) {
				recps[i] = new InternetAddress(getRecipients()[i]);
			}
			
//			MimeMessage msg = new MimeMessage(session);
			appendHeaders();

			getMimeMessage().addRecipients(Message.RecipientType.TO, recps);
			getMimeMessage().addFrom(new InternetAddress[] { new InternetAddress(getFrom()) } );
			getMimeMessage().setSubject(getSubject());
			
			if (getMimeMessage().getHeader("Content-Type") != null && getMimeMessage().getHeader("Content-Type").length > 0 
					&& getMimeMessage().getHeader("Content-Type")[0].equals("text/html")) {
				getMimeMessage().setContent(getMessage(), getMimeMessage().getHeader("Content-Type")[0]);
			} else {
				getMimeMessage().setText(getMessage());
			}
			
			Transport.send(getMimeMessage());
			
			setMimeMessage(null);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void appendHeaders() {
		if (this.getHeaders() != null) {
			for (Entry<String, String> entry : this.getHeaders().entrySet()) {
				try {
					if ("from".equalsIgnoreCase(entry.getKey())) {
						//this.getMimeMessage().addHeaderLine(entry.getKey() + " " + entry.getValue());
					} else {
						this.getMimeMessage().addHeader(entry.getKey(), entry.getValue());
					}
				} catch (MessagingException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
