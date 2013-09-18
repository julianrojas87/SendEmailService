package org.telcomp.sbb;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.slee.ActivityContextInterface;
import javax.slee.Address;
import javax.slee.RolledBackContext;
import javax.slee.SbbContext;

import org.telcomp.events.EndSendEmailTelcoServiceEvent;
import org.telcomp.events.StartSendEmailTelcoServiceEvent;
import org.telcomp.others.SMTPAuthenticator;


public abstract class SendEmailSbb implements javax.slee.Sbb {
	
	private static boolean proxyNeeded;
	
	@SuppressWarnings("static-access")
	public void onStartSendEmailTelcoServiceEvent(StartSendEmailTelcoServiceEvent event, ActivityContextInterface aci) {
		System.out.println("*******************************************");
		System.out.println("SendEmailTelcoService Invoked");
		System.out.println("Input Email = "+event.getEmail());
		System.out.println("Input Subject = "+event.getSubject());
		System.out.println("Input Message = "+event.getMessage());
		
		Properties prop = new Properties();
        try {
			prop.load(new FileInputStream("/usr/local/Mobicents-JSLEE/telcoServices.properties"));
			proxyNeeded = Boolean.parseBoolean(prop.getProperty("PROXY_NEEDED"));
		} catch (Exception e) {
			e.printStackTrace();
		}

		final String from = "telcompunicauca@gmail.com";
		final String password = "servicioemail";
		String to = event.getEmail();
		String text = event.getMessage();
		text = text + "\n\nBest Regards,\n\nTelcomp2.0 Development Team";
		String subject;
		
		if(event.getSubject() != null){
			subject = event.getSubject();
		} else {
			subject = "Telcomp 2.0 Email";
		}
		
		String host = "smtp.gmail.com";
		String port = "465";
		
		if(!proxyNeeded){
			try {
				Properties props = new Properties();
				//Properties props = System.getProperties();
				props.put("mail.smtp.user", from);
				props.put("mail.smtp.host", host);
				props.put("mail.smtp.port", port);
				props.put("mail.smtp.starttls.enable","true");
				//props.put("mail.smtp.debug", "true");
				props.put("mail.smtp.auth", "true");
				props.put("mail.smtp.socketFactory.port", port);
				props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
				props.put("mail.smtp.socketFactory.fallback", "false");
				
				Authenticator auth = new SMTPAuthenticator();
				Session session = Session.getInstance(props, auth);
				//session.setDebug(true);
				MimeMessage message = new MimeMessage(session);
				message.setFrom(new InternetAddress(from));
				message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
				message.setSubject(subject);
				message.setText(text);
				message.saveChanges();
				Transport transport = session.getTransport("smtps");
				transport.connect(host, 465, "telcompunicauca", password);
				transport.send(message, message.getAllRecipients());
				transport.close();
				System.out.println("Sent Email successfully....");
				
				HashMap<String, Object> operationInputs = new HashMap<String, Object>();
				operationInputs.put("sended", (String) "true");
				EndSendEmailTelcoServiceEvent EndSendEmailWSEvent = new EndSendEmailTelcoServiceEvent(operationInputs);
				this.fireEndSendEmailTelcoServiceEvent(EndSendEmailWSEvent, aci, null);
				aci.detach(this.sbbContext.getSbbLocalObject());
				System.out.println("Output Sended = true");
				System.out.println("*******************************************");
			} catch (Exception ex) {
				HashMap<String, Object> operationInputs = new HashMap<String, Object>();
				operationInputs.put("sended", (String) "false");
				EndSendEmailTelcoServiceEvent EndSendEmailWSEvent = new EndSendEmailTelcoServiceEvent(operationInputs);
				this.fireEndSendEmailTelcoServiceEvent(EndSendEmailWSEvent, aci, null);
				aci.detach(this.sbbContext.getSbbLocalObject());
				System.out.println("Output Sended = false");
				System.out.println("*******************************************");
			}
		} else{
			System.out.println("Couldn't send Email due to proxy server....");
			HashMap<String, Object> operationInputs = new HashMap<String, Object>();
			operationInputs.put("sended", (String) "false");
			EndSendEmailTelcoServiceEvent EndSendEmailWSEvent = new EndSendEmailTelcoServiceEvent(operationInputs);
			this.fireEndSendEmailTelcoServiceEvent(EndSendEmailWSEvent, aci, null);
			aci.detach(this.sbbContext.getSbbLocalObject());
			System.out.println("Output Sended = false");
			System.out.println("*******************************************");
		}
	}
	
	// TODO: Perform further operations if required in these methods.
	public void setSbbContext(SbbContext context) {
		this.sbbContext = context;
	}

	public void unsetSbbContext() {
		this.sbbContext = null;
	}

	// TODO: Implement the lifecycle methods if required
	public void sbbCreate() throws javax.slee.CreateException {
	}

	public void sbbPostCreate() throws javax.slee.CreateException {
	}

	public void sbbActivate() {
	}

	public void sbbPassivate() {
	}

	public void sbbRemove() {
	}

	public void sbbLoad() {
	}

	public void sbbStore() {
	}

	public void sbbExceptionThrown(Exception exception, Object event,
			ActivityContextInterface activity) {
	}

	public void sbbRolledBack(RolledBackContext context) {
	}

	public abstract void fireEndSendEmailTelcoServiceEvent(EndSendEmailTelcoServiceEvent event,
			ActivityContextInterface aci, Address address);

	/**
	 * Convenience method to retrieve the SbbContext object stored in
	 * setSbbContext.
	 * 
	 * TODO: If your SBB doesn't require the SbbContext object you may remove
	 * this method, the sbbContext variable and the variable assignment in
	 * setSbbContext().
	 * 
	 * @return this SBB's SbbContext object
	 */

	protected SbbContext getSbbContext() {
		return sbbContext;
	}

	private SbbContext sbbContext; // This SBB's SbbContext

}
