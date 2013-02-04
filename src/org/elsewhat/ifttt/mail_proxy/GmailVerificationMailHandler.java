package org.elsewhat.ifttt.mail_proxy;

import java.io.IOException;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.elsewhat.ifttt.mail_proxy.util.MailUtils;

/**
 * Handle incoming messages from gmail for verifying an forwarding email addresse
 * 
 * End-result should be an email back to the gmail account with the verification code
 * 
 * 
 * @author dagfinn.parnas
 *
 */
public class GmailVerificationMailHandler {
	private static final Logger log = Logger.getLogger(GmailVerificationMailHandler.class.getName());
	
	public static final String GMAIL_FORWARDING_EMAIL_FROM = "mail-noreply@google.com";
	//public static final String GMAIL_FORWARDING_EMAIL_FROM = "dagfinn.parnas@bouvet.no";
	public static final String GMAIL_FORWARDING_SUBJECT= "Gmail Forwarding Confirmation";
	
	public static final String REPLY_FROM = "mail-noreply@elsewhat-iftttgmailproxy.appspotmail.com";
	
	
	public boolean canHandleEmail(MimeMessage message) throws MessagingException{
		InternetAddress emailSender = MailUtils.getSender(message);
		String subject = message.getSubject();
		
		if(GMAIL_FORWARDING_EMAIL_FROM.equalsIgnoreCase(emailSender.getAddress())
				&& subject.contains(GMAIL_FORWARDING_SUBJECT)){
			
			return true;
		}else {
			log.warning("Cannot handle email from sender " + emailSender.getAddress() + " and subject " + message.getSubject());
			return false;
		}	
	}
	
	public boolean handleEmail(MimeMessage message) throws MessagingException, IOException{
		if(canHandleEmail(message)==false){
			log.warning("Cannot handle email with subject " + message.getSubject());
			return false;
		}
		InternetAddress emailFrom = MailUtils.getSender(message);
		
		String subject = message.getSubject();
		String contentType = message.getContentType();
		Object objectContent = message.getContent();
		
		
		log.info("Mail from " + emailFrom + " subject "+subject + " contenttype "+contentType + " content:"+objectContent);
		
		String emailForVerification = findGmailFromContent((String)objectContent);
		InternetAddress replyEmailTo=null;
		try {
			replyEmailTo= new InternetAddress(emailForVerification);
			log.info("Found valid reference to "+emailForVerification );
		}catch(AddressException e){
			log.log(Level.WARNING, "Could not get valid email address from content. Tried to use " + emailForVerification, e);
			return false;
		}
		
		String content = (String)objectContent;
		
		Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);


        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(REPLY_FROM, "Elsewhat IFTTT GMail proxy"));
            
            msg.addRecipient(Message.RecipientType.TO,
            				 replyEmailTo);
            msg.setSubject("iftttgmailproxy - " + subject);
            msg.setText(content);
            Transport.send(msg);
            
            log.info("Sending email to " + replyEmailTo);
            return true;
            
        } catch (AddressException e) {
        	log.log(Level.WARNING, "Could not send email to " + replyEmailTo, e);
        } catch (MessagingException e) {
        	log.log(Level.WARNING, "Could not send email to " + replyEmailTo, e);
        	
        }
        return false;
		
	}

	private String findGmailFromContent(String content) {
		StringTokenizer tokenizer = new StringTokenizer(content);
		if(tokenizer.hasMoreTokens()){
			String token = tokenizer.nextToken();
			return token;
		}
		
		// TODO Auto-generated method stub
		return null;
	}	
	

	
}
