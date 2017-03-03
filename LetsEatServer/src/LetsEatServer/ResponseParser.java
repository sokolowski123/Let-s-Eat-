package LetsEatServer;

import java.util.Arrays;

import org.bson.Document;

import com.mongodb.*;

public class ResponseParser {
	
	public static void main(String[] args) {
		
		ResponseParser parse = new ResponseParser();
		Document user = new Document();
		user.append("_id", "sokola@purdue.edu");
		user.append("_pass", "43dfdf3476gede3");
		user.append("_matches", "[user1@purdue.edu, user2@purdue.edu, user3@purdue.edu]");
		user.append("_chat", "[[0412170916sokola@purdue.edu..hey!, 0412170916user1@purdue.edu..how's it going?, 0412170916sokola@purdue.edu..nbu?],[user1@purdue.edu..hey],"
				+ "[sokola@purdue.edu: where you trying to eat?]]");
		user.append("blocked", "[0, 0, 0]");
		user.append("isValid", "[1, 1, 0]");
		user.append("dob", "5/29/1996");
		user.append("name", "Alex Sokol");
		user.append("userId", "sokola");
		user.append("gender", "male");
		
		User userA = parse.parseUser(user);
		parse.parseMessage(user);
		
	}
	
	
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
