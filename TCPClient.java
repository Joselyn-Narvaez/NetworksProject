import java.io.*;
import java.net.*;

public class TCPClient {
	//TODO: Automate client connection and sending requests: at least 3 calculation requests at random times
	public static void main(String argv[]) throws Exception {
		String sentence;
		String modifiedSentence;

		// declare IO and client socket
		BufferedReader inFromUser = null;
		DataOutputStream outToServer = null;
		BufferedReader inFromServer = null;
		
		String ip = argv[0];
		int PORT = Integer.parseInt(argv[1]); 		
		
		Socket clientSocket = new Socket(ip, PORT);
		System.out.println("Starting client on PORT " + PORT);

		// init IO
		try {
			inFromUser = new BufferedReader(new InputStreamReader(System.in));
			outToServer = new DataOutputStream(clientSocket.getOutputStream());
			inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		} catch (IOException e) {
			clientSocket.close();
			return;
		}

		// If connection successful, then the server will respond and the client will output 
		System.out.println("FROM SERVER: " + inFromServer.readLine());

		// communicate with TCP server for requests by client
		while (true) {
			try {
				sentence = inFromUser.readLine();

				// handle TCP client close connection request
				if ((sentence == null) || sentence.equalsIgnoreCase("QUIT")) {
					// System.out.println("Sending close TCP connection request to server\n");
					outToServer.writeBytes(sentence + '\n');
					outToServer.flush();

					modifiedSentence = inFromServer.readLine();
					System.out.println("FROM SERVER: " + modifiedSentence);
					clientSocket.close();
					return;
				} else {// handle any other request including math requests
					outToServer.writeBytes(sentence + '\n');
					outToServer.flush();

					modifiedSentence = inFromServer.readLine();
					System.out.println("FROM SERVER: " + modifiedSentence);
				}
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
		}
	}
}
