import java.io.*;
import java.net.*;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

class TCPServer extends Thread {
	Socket connectionSocket;
	int clientNum;
	String clientSentence, capitalizedSentence;
	LocalTime connectTime, disconnectTime;
	String[] request;

	TCPServer(Socket inSocket, int counter, LocalTime startTime) {
		connectionSocket = inSocket;
		clientNum = counter;
		connectTime = startTime;
	}

	public void run() {
		int solution;
		String serverResponse;
		try {

			BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
			DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());

			System.out.println("Client " + clientNum + " >>> CONNECT >>> Open Connection Time: "
								+ connectTime + " sec");
			serverResponse = "Server >>> Successful connection...\n";
			outToClient.writeBytes(serverResponse);
			outToClient.flush();

			// start accepting client requests through socket
			while (true) {
				// accept TCP client request until end request
				clientSentence = inFromClient.readLine();

				while ((clientSentence != null) && !(clientSentence.toUpperCase().equals("QUIT"))) {
					// If the client sends in a math request
					if (clientSentence.toUpperCase().contains("MATH")) {
						request = clientSentence.split("\\.");
						solution = mathFunction(request[1]);
						System.out.println(
								"Client " + clientNum + " >>> MATH >>> " + request[1] + " >>> Solution: " + solution);
						serverResponse = "Server >>> Solution: " + solution + "\n";

					}
					// If the client does not send a math request, this is the default response
					else {
						serverResponse = clientSentence.toUpperCase() + '\n';
					}

					outToClient.writeBytes(serverResponse);
					outToClient.flush();

					clientSentence = inFromClient.readLine();
				}

				// handles null string exception
				if (clientSentence == null) {
					continue;
				}

				// handle TCP client's end connection request
				if (clientSentence.equalsIgnoreCase("QUIT")) {
					// log client connection duration
					disconnectTime = LocalTime.now();
					System.out.println("Client " + clientNum + " >>> QUIT >>> Connection Duration: "
							+ connectTime.until(disconnectTime, ChronoUnit.SECONDS) + " sec");

					outToClient.writeBytes("Server >>> Confirming end connection request\n");
					outToClient.flush();
				}
			}
		} catch (Exception ex) {
			System.out.println(ex);
		} finally {
			System.out.println("Client " + clientNum + " has disconnected. ");
		}
	}


	// Simple math functions: addition, subtraction, multiplication, division and exponent
	public static int mathFunction(String mathRequest) {
		try {
            int a, b;
			// If the math request contains the associated math operator, then we split (via split() method) by that operator
			// Then we set the value to the left of the split to be the first integer and the value to the right to be the second and then do the operation.
            if (mathRequest.contains("+")) {
                String[] splitExpr = mathRequest.split("\\+");
                a = Integer.valueOf(splitExpr[0].trim());
                b = Integer.valueOf(splitExpr[1].trim());
                return a + b;
            } else if (mathRequest.contains("-")) {
                String[] splitExpr = mathRequest.split("-");
                a = Integer.valueOf(splitExpr[0].trim());
                b = Integer.valueOf(splitExpr[1].trim());
                return a - b;
            } else if (mathRequest.contains("*")) {
                String[] splitExpr = mathRequest.split("\\*");
                a = Integer.valueOf(splitExpr[0].trim());
                b = Integer.valueOf(splitExpr[1].trim());
                return a * b;
            } else if (mathRequest.contains("/")) {
                String[] splitExpr = mathRequest.split("/");
                a = Integer.valueOf(splitExpr[0].trim());
                b = Integer.valueOf(splitExpr[1].trim());
                return a / b;
            } else if (mathRequest.contains("^")) {
                String[] splitExpr = mathRequest.split("\\^");
                a = Integer.valueOf(splitExpr[0].trim());
                b = Integer.valueOf(splitExpr[1].trim());
                return (int) Math.pow(a, b);
            }

        }
		// If there was any type of error in the syntax of the inputted math expression
        catch(Exception e) {
            System.out.println(e);
            return -1;
        }

		return 0;
	}
}
