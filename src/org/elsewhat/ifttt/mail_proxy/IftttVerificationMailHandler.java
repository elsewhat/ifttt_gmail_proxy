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
public class IftttVerificationMailHandler {
	private static final Logger log = Logger.getLogger(IftttVerificationMailHandler.class.getName());
	
	public static final String IFTTT_VERIFICATION_EMAIL_FROM = "action@ifttt.com";
	public static final String IFTTT_VERIFICATION_SUBJECT= "Email channel activation PIN";
	
	public static final String REPLY_FROM = "mail-noreply@elsewhat-iftttgmailproxy.appspotmail.com";
	public static final String REPLY_TO = "dagfinn.parnas@gmail.com";
	
	
	public boolean canHandleEmail(MimeMessage message) throws MessagingException{
		InternetAddress emailSender = MailUtils.getSender(message);
		String subject = message.getSubject();
		
		if(IFTTT_VERIFICATION_EMAIL_FROM.equalsIgnoreCase(emailSender.getAddress())
				&& subject.contains(IFTTT_VERIFICATION_SUBJECT)){
			
			return true;
		}else {
			//log.warning("Cannot handle email from sender " + emailSender.getAddress() + " and subject " + message.getSubject());
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

		
		Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);


        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(REPLY_FROM, "Elsewhat IFTTT GMail proxy"));
            
            msg.addRecipient(Message.RecipientType.TO,
            		new InternetAddress(REPLY_TO, "Dagfinn Parnas"));
            msg.setSubject("iftttgmailproxy - " + subject);
            msg.setContent(objectContent, contentType);
            Transport.send(msg);
            
            log.info("Sending email to " + REPLY_TO);
            return true;
            
        } catch (AddressException e) {
        	log.log(Level.WARNING, "Could not send email to " + REPLY_TO, e);
        } catch (MessagingException e) {
        	log.log(Level.WARNING, "Could not send email to " + REPLY_TO, e);
        	
        }
        return false;
		
	}


	

	
}
