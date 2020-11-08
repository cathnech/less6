package less6;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.io.DataInputStream;

public class MainServer {
	private static ServerSocket server;
	private static Socket socket;
	private static final int PORT = 8189;

	private static Scanner in;
	
	public static void main(String[] args) {
		DataInputStream in;
		DataOutputStream out;

		try {
			server = new ServerSocket ( PORT );
			System.out.println ( "server started!" );
			
			socket = server.accept ();
			System.out.println ( "client connected!" );
			System.out.println("RemoteSocketAddress "+ socket.getRemoteSocketAddress());
			System.out.println("LocalSocketAddress "+ socket.getLocalSocketAddress());

			in = new DataInputStream(socket.getInputStream());
			out = new DataOutputStream(socket.getOutputStream());
			
			Socket finalSocket1 = socket;
			Thread readThread = new Thread(() -> {
				try{
					while(true){
						String msg = in.readUTF();
						System.out.println("\nclient: " + msg);
						System.out.print("...");
					}
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					try {
						finalSocket1.close();
						in.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
			readThread.start();

			Socket finalSocket = socket;
			Thread sendThread = new Thread(() -> {
				Scanner read = new Scanner(System.in);
				try{
					while(true){
						System.out.print("...");
						String msg = read.nextLine();
						if (msg.equals("/end")) {
							break;
						} else {
							out.writeUTF(msg);
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					try {
						finalSocket.close();
						out.close();
						read.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
			sendThread.start();

		} catch ( IOException e ) {
			System.out.println ( "Error connection" );
		} finally {
			try {
				server.close();
			} catch (IOException e){
				e.printStackTrace();
			}
		}
	}
}
