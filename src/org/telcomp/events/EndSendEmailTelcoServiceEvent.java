package org.telcomp.events;

import java.util.HashMap;
import java.util.Random;
import java.io.Serializable;

public final class EndSendEmailTelcoServiceEvent implements Serializable {

	private static final long serialVersionUID = 1L;
	private final long id;
	private boolean sended;

	public EndSendEmailTelcoServiceEvent(HashMap<String, ?> hashMap) {
		id = new Random().nextLong() ^ System.currentTimeMillis();
		this.sended = (boolean) Boolean.parseBoolean((String) hashMap.get("sended"));
	}

	public boolean equals(Object o) {
		if (o == this) return true;
		if (o == null) return false;
		return (o instanceof EndSendEmailTelcoServiceEvent) && ((EndSendEmailTelcoServiceEvent)o).id == id;
	}
	
	public int hashCode() {
		return (int) id;
	}
	
	public boolean getSended(){
		return this.sended;
	}
	
	public String toString() {
		return "endSendEmailEvent[" + hashCode() + "]";
	}
}
