package com.digital.web.actions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.digital.web.core.DynamicObject;
import com.digital.web.models.MailInterfaceModel;
import com.digital.web.services.SimpleEmailService;
import com.digital.web.utils.AppUtil;

public class SendEmailAction extends Action {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SendEmailAction() {
		super();
	}
	
	public SendEmailAction(Object ctx) {
		super();
		this.setContext(ctx);
	}
	
	private MailInterfaceModel getMailInterfaceModel() {
		return (MailInterfaceModel) this.getContext();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Object execute(Object params) {
		
		try {
			
			DynamicObject data = getMailInterfaceModel().getData();
			
			SimpleEmailService emailService = new SimpleEmailService();
			
			Properties props = new Properties();
			props.put("mail.smtp.host", this.getString("ip"));
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.transport.protocol", "smtp");
			props.put("mail.smtp.port", "2525");
			
			String message = data.getString("message");
			String negativeText = data.getString("negativeText");
			if (!data.isFieldNull("negativeText")) {
				
				if (message.contains("{negative_text}")) {
					message = message.replace("{negative_text}", negativeText);
				}/* else {
					int i = message.indexOf("<body>");
					if (i >= 0) {
						String bodyStart = message.substring(0, i + 6);
						String bodyEnd = message.substring(i + 6, message.length());
						
						
					}
				}*/
			}
			
			emailService.setConfigProperties(props);
			emailService.setUsername(getProperty("PowerMTA_Username")); //datatechbox4ikmO9zu70J0
			emailService.setPassword(getProperty("PowerMTA_Password")); //dN18s85Kcx70753XxiwT
			
			String fromH = data.getString("fromEmail");
			
			if (!data.isFieldNull("fromName")) {
				fromH = data.getString("fromName") + " <" +data.getString("fromEmail") + ">";
			}
			
			emailService.setFrom(fromH);
			
			// TODO appendHeaders();
			Map<String, String> headersMap = prepareHeaders(data);

			emailService.setHeaders(headersMap);
			
			List<String> receipients = (List<String>) this.get("iterationEmails");//data.getString("sendToEmails").split(",");
			int i = 0;
			for (String toMail : receipients) {
				
				if (!this.isFieldNull("iterationCount") && this.getInt("iterationCount") != -1
						&& i++ >= this.getInt("iterationCount")) {
					//this.getMailInterfaceModel().checkUsageLimit(this);
					//this.setCheckUsage(true);
					if (this.getInt("ipUsageExceeded") >= 0) {
						System.out.println(this.getString("ip") + " : Thread is waiting for " + this.getInt("ipUsageExceeded") + " Secs.");
						Thread.sleep(this.getInt("ipUsageExceeded") * 1000);
					} else {
						System.out.println(this.getString("ip") + " : Killing Thread as limit is reached " + this.getInt("iterationCount"));
						Thread.currentThread().interrupt();
					}
				}
				String subject = data.getString("subject");
				if (!AppUtil.isNullOrEmpty(subject) && subject.contains("{user_email}")) {
					subject = subject.replace("{user_email}", toMail);
				}
				
				String tempMsg = message;
				if (!AppUtil.isNullOrEmpty(tempMsg) && tempMsg.contains("{user_email}")) {
					tempMsg = tempMsg.replace("{user_email}", toMail);
				}
				
				String offerId = data.getString("offerId");
				if (!AppUtil.isNullOrEmpty(tempMsg) && tempMsg.contains("{offer_id}")) {
					tempMsg = tempMsg.replace("{offer_id}", offerId);
				}
				
				emailService.setSubject(subject);
				emailService.setMessage(tempMsg);
				emailService.setRecipients(toMail);
				emailService.sendEmail();
				System.out.println("Mail sent to : " + toMail);
				
				if (!this.isFieldNull("testingPeriod") && i == this.getInt("testingPeriod")) {
					String[] testEmails = (String[]) this.get("testEmails");
					
					//String originalSubject = emailService.getSubject();
					emailService.setSubject("Acknoledgement: " + data.getString("subject"));
					emailService.setRecipients(testEmails);
					
					emailService.sendEmail();
					System.out.println(" Test Mail sent to : " + testEmails);
					
					//emailService.setSubject(originalSubject);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	
	/**
	 * 
	 * @param service
	 */
	private Map<String, String> prepareHeaders(DynamicObject data) {
		
		Map<String, String> headersMap = new HashMap<String, String>();
		String headersString = data.getString("headerFormat");
		
		if (!AppUtil.isNullOrEmpty(headersString)) {
			String[] headersArr = headersString.split("\n");
			
			for (String headerStr : headersArr) {
				String[] hv = headerStr.split(":");
				String hValue = hv[1].trim().contains("<__") && hv[1].trim().contains(">") 
						? data.getString(hv[1].trim().replace("<__", "").replace(">", "")) : hv[1].trim(); 
				headersMap.put(hv[0].trim(), hValue);
			}
		} else {
			if (!data.isFieldNull("xMailer"))
				headersMap.put("X-Mailer", data.getString("xMailer"));
			
			if (!data.isFieldNull("contentType"))
				headersMap.put("Content-Type", data.getString("contentType"));
			
			if (!data.isFieldNull("returnPath"))
				headersMap.put("Return-Path", data.getString("returnPath"));
		}
		String fromH = data.getString("fromEmail");
		
		if (!data.isFieldNull("fromName")) {
			fromH = data.getString("fromName") + " <" +data.getString("fromEmail") + ">";
		}
		
		headersMap.put("From", fromH);
		
		System.out.println(headersMap);
		return headersMap;
	}
}
