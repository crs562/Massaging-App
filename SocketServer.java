import java.util.*;
import java.io.*;
import java.net.*;

class MultithreadServer extends Thread {
	private Socket socket;
	ObjectInputStream sin = null;
	ObjectOutputStream sout  = null;
	public MultithreadServer(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		try {
			System.out.println("Client Connected" + socket);
			sin = new ObjectInputStream(socket.getInputStream());
			sout = new ObjectOutputStream(socket.getOutputStream());

			while(true) {
				String clientMsg = (String) sin.readObject();
				System.out.println(clientMsg);
				if(clientMsg.equals("exit")) {
					break;
				}
				String serverMsg = "*"+clientMsg+"*";
				sout.writeObject(serverMsg);
			}
		} catch(IOException e) {
			System.out.println("Oops: " + e.getMessage());
		} catch(Exception e) {
			System.err.println(e);
			return;
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				System.out.println("Socket Close: " + e.getMessage());
			}
		}
	}
}

public class SocketServer {
	public static void main(String[] args) throws IOException {
		if (args.length != 1) {
			System.err.println("Usage: java SocketServer port");
			return;
		}
      	try {
	 		ServerSocket s = new ServerSocket(Integer.parseInt(args[0]));
	 		while (true) {
	 			new MultithreadServer(s.accept()).start();
			}
	 	} catch (IOException e) {
			System.out.println("Server exception " + e.getMessage());
		}
	}
}
