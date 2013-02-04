package org.elsewhat.ifttt.mail_proxy;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



/**
 * MailHandler servlet for handling incoming emails
 * See https://developers.google.com/appengine/docs/java/mail/receiving
 * 
 * Email adresse is of format 
 * <string>@elsewhat-iftttgmailproxy.appspotmail.com
 * 
 * 
 * @author dagfinn.parnas
 *
 */
@SuppressWarnings("serial")
public class MailHandlerServlet extends HttpServlet {
	private static final Logger log = Logger.getLogger(MailHandlerServlet.class.getName());

	
	
    public void doPost(HttpServletRequest req, 
            HttpServletResponse resp)  { 
    	
    	log.info("MailHandlerServlet is receiving new email (http post)");
		
    	try {
    		//try retrieving the email
        	Session session = Session.getDefaultInstance(new Properties(), null); 
        	
        	MimeMessage message = new MimeMessage(session, req.getInputStream());
			
        	GmailVerificationMailHandler gmailVerificationMailHandler = new GmailVerificationMailHandler();
			
        	gmailVerificationMailHandler.handleEmail(message);
			
			
			log.warning("Mail from " + message.getFrom()+ " subject "+message.getSubject());
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			log.log(Level.WARNING, "Exception thrown while reading email", e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.log(Level.WARNING, "Exception thrown while reading email", e);
		}
    	
    	
    	
    }
}
