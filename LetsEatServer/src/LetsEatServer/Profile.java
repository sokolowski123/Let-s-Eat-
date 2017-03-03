package LetsEatServer;

public class Profile {
	int age;
    int prefMaxAge;
    int prefMinAge;
    String bio;
	String location;
	String[] additionalInfo;
	
	public Profile(int age, int prefMaxAge, int prefMinAge, String bio, String location, String[] additionalInfo) {
		this.age = age;
		this.prefMaxAge = prefMaxAge;
		this.bio = bio;
		this.location = location;
		this.additionalInfo = additionalInfo;
	}
}
