import java.net.*;
import java.lang.Exception.*;
import java.io.*;
import java.lang.Thread;

class Server {

    /* This function only works when client and severs are run on the same computer. */
    /* No IP address used. */
    public void run() {
	Socket clientSocket;
	ServerSocket serverSocket;

	try {
	    serverSocket = new ServerSocket(4444); /* For client use this port for the moment. */
	} catch (IOException e) {
	    System.out.printf("System failed to create a defualt ServerSocket.\n");
	    return;
	}
	System.out.printf("ServerSocket Creation Successful.\n");
	System.out.printf("ServerSocket Port Number: %d\n", serverSocket.getLocalPort());

	/* This portion will eventualy be put in a infinte while loop. */
	try {
	    clientSocket = serverSocket.accept();
	} catch (Exception e) {
	    System.out.printf("Error when waiting for a connection to the server.\n");
	    return;
	}

	/* The commnication bettween a client and a server is threaded to allow for multiple users. */ 
	(new ServerClient(clientSocket)).start();	
	
    }

    public static void main(String[] args) {
	Server server;
        
	server = new Server();
	server.run();
    }
}

class ServerClient extends Thread {
    
    Socket clientSocket;

    ServerClient(Socket clientSocket) {
	this.clientSocket = clientSocket;
    }
    
    /* This fuction facilitates the communication bettween a client and the server. */
    public void run() {
    	String str;
	PrintWriter out;
	BufferedReader in;
	
	System.out.printf("A connection between client and server has been made.\n");
	try {

	    /* Writing to the client. */
	    out = new PrintWriter(clientSocket.getOutputStream());
	    
	    /* Reading from the client. */
	    in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
	
	} catch (IOException e) {
	    System.out.printf("A IOExecption has occurred when retrieveing the InputStream or OutputStream of a Socket.\n");
	    return;
	}

	while (!clientSocket.isClosed()) {
	    try {
	        str = in.readLine();
	    } catch (IOException e) {
		System.out.printf("A IOException has occurred when retrieveing the next line from the BufferedReader.\n");
		return;
	    }
	    if (str != null) {
		
		str = this.parseHttpRequest(str);							
		
	    }
	}

    }

    /* This function will read a http request and turn it into something. (Databse?) */
    /* The return type might change. */
    public String parseHttpRequest(String request) {
	return "Information for Database";
    }
}
