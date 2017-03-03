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
	
	String parseMessage(Document user) {
		String message = user.get("_chat").toString();
		//System.out.println(message);
		return "";
	}
	
	User parseUser(Document user) {
		String dob = user.get("dob").toString();
		String name = user.get("name").toString();
		String userId = user.get("userId").toString();
		String gender = user.get("gender").toString();
		String matches = user.get("_matches").toString();
		String valid = user.getString("isValid").toString();
		String blocked = user.get("blocked").toString();
		String chat = user.getString("_chat").toString();
		
		matches = matches.substring(1, matches.length()-1);
		blocked = blocked.substring(1, blocked.length()-1);
		valid = valid.substring(1, valid.length()-1);
		chat = chat.substring(1, chat.length()-1);
	
		String[] matchArray = matches.split(", ");
		String[] validArray = valid.split(", ");
		String[] blockedArray = blocked.split(", ");
		String[] chatArray = chat.split("],");
		for (int i = 0; i < chatArray.length-1; i++) {
			chatArray[i] = chatArray[i] + "]";
		}
		String[][] chatHist = new String[chatArray.length][];
		for (int i = 0; i < chatArray.length; i++) {
			String messages = chatArray[i].substring(1, chatArray[i].length()-1);
			String[] nextHist = messages.split(",");
			chatHist[i] = new String[nextHist.length];
			for (int j = 0; j < nextHist.length; j++) {
				chatHist[i][j] = nextHist[j];
			}
		}
		System.out.println(Arrays.deepToString(chatHist));
		
	
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
		
		return new User(dob, userId, name, gender, matchObjs, chatHist);
		
	}
	
}
