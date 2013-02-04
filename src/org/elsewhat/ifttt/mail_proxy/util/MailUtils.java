package org.elsewhat.ifttt.mail_proxy.util;

import java.util.logging.Logger;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class MailUtils {
	private static final Logger log = Logger.getLogger(MailUtils.class.getName());
	
	public static InternetAddress getSender(MimeMessage message) throws MessagingException{
		Address addressSender  =message.getSender();
		
		if(addressSender!=null){
			if(addressSender instanceof InternetAddress){
				return (InternetAddress)addressSender;
			}else {
				log.warning("Sender is not instance of InternetAddress " + addressSender );
				return null;
			}
			
		}else {
			//if sender is null, we need to use the from field
			Address[] addressFrom  =message.getFrom();
			if(addressFrom!=null && addressFrom.length>0 && addressFrom[0] instanceof InternetAddress){
				return (InternetAddress)addressFrom[0];
			}else {
				log.warning("Could not find sender for message with subject. Both sender and from fields are invalid " + message.getSubject());
				return null;
			}
			
		}
		
		
	}

}
