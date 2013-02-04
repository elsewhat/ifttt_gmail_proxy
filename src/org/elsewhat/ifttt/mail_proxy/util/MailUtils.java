package org.elsewhat.ifttt.mail_proxy.util;

import java.util.logging.Logger;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.elsewhat.ifttt.mail_proxy.GmailVerificationMailHandler;


public class MailUtils {
	private static final Logger log = Logger.getLogger(MailUtils.class.getName());
	
	public static InternetAddress getSender(MimeMessage message) throws MessagingException{
		Address addressSender  =message.getSender();
		
		log.warning("Sender is " + addressSender );
		if(addressSender!=null &&  addressSender instanceof InternetAddress){
			return (InternetAddress)addressSender;
		}else {
			return null;
		}
	}

}
