package org.telcomp.events;

import java.util.HashMap;
import java.util.Random;
import java.io.Serializable;

public final class StartSendEmailTelcoServiceEvent implements Serializable {

	private static final long serialVersionUID = 1L;
	private final long id;
	
	private String email;
	private String message;
	private String subject;

	public StartSendEmailTelcoServiceEvent(HashMap<String, ?> hashMap) {
		id = new Random().nextLong() ^ System.currentTimeMillis();
		this.email = (String) hashMap.get("email");
		this.message = (String) hashMap.get("message");
		this.subject = (String) hashMap.get("subject");
	}

	public boolean equals(Object o) {
		if (o == this) return true;
		if (o == null) return false;
		return (o instanceof StartSendEmailTelcoServiceEvent) && ((StartSendEmailTelcoServiceEvent)o).id == id;
	}
	
	public int hashCode() {
		return (int) id;
	}
	
	public String getEmail(){
		return this.email;
	}
	
	public String getMessage(){
		return this.message;
	}
	
	public String getSubject(){
		return this.subject;
	}
	
	public String toString() {
		return "startSendEmailEvent[" + hashCode() + "]";
	}
}
