package by.bsu.mmf.badthrowgame.client;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler {
    private Socket clientSocket;
    private ObjectOutputStream outStream;
    private ObjectInputStream inStream;



    public ClientHandler(Socket socket, ObjectOutputStream oos, ObjectInputStream ois) {
        clientSocket = socket;
        outStream = oos;
        inStream = ois;
    }


    public ObjectOutputStream getOutStream() {
        return outStream;
    }

    public ObjectInputStream getInStream() {
        return inStream;
    }
}
