package by.bsu.mmf.badthrowgame.server.run;

import by.bsu.mmf.badthrowgame.server.master.MasterServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MasterRunner {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(8080);
            System.out.println("Master Server initialized on port: " + serverSocket.getLocalPort() + "\n");
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Client " + socket.getInetAddress() + " connected to Master Server" + "\n");
                new MasterServer(socket).start();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }
}
