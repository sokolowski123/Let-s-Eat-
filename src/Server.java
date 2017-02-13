import java.net.*;
import java.lang.Exception.*;
import java.io.*;

class Server {
    public void run() {
	try {
	    ServerSocket serverSocket = new ServerSocket();
	} catch (IOException e) {
	    System.out.println("System failed to create a defualt ServerSocket.");
	}

    }

    public static void main(String[] args) {
	Server server;
        
	server = new Server();
	server.run();
    }
}
