package LetsEatServer;

import java.util.Arrays;

import org.bson.Document;

import com.mongodb.*;

public class ResponseParser {
	Account parseAccount(Document user) {
		String email = user.get("_id").toString();
		String pass = user.get("_pass").toString();
		return new Account(email, pass);
	}
	
	User parseUser(Document user) {
		String dob = user.get("dob").toString();
		String name = user.get("name").toString();
		String userId = user.get("userId").toString();
		String gender = user.get("gender").toString();
		String matches = user.get("_matches").toString();
		String valid = user.getString("isValid").toString();
		String blocked = user.get("blocked").toString();
		matches = matches.substring(1, matches.length()-1);
		blocked = blocked.substring(1, blocked.length()-1);
		valid = valid.substring(1, valid.length()-1);
		
		String[] matchArray = matches.split(", ");
		String[] validArray = valid.split(", ");
		String[] blockedArray = blocked.split(", ");
		
		Match[] matchObjs = new Match[matchArray.length];
		for (int i = 0; i < matchObjs.length; i++) {
			boolean validB, blockedB;
			if (validArray[i].equals("0")) {
				validB = false;
			} 
			else {
				validB = true;
			}
			if (blockedArray[i].equals("0")) {
				blockedB = false;
			}
			else {
				blockedB = true;
			}
			matchObjs[i] = new Match(validB, blockedB, user.get("_id").toString(), matchArray[i]);
			
		}
		
		return new User(dob, userId, name, gender, matchObjs);
		
	}
	
}
