//LULSEGED ADMASU 2022

import java.net.*;
import java.io.*;
import java.util.Random;

public class Server {
    public static ServerSocket port;
    private static int connCounter = 0;

    public static void main(String[] args) throws IOException {
        ConnThread nextThread;
        Socket toClientSocket;
        port = new ServerSocket(8787);

        do{
            try {
                System.out.println("Waiting for connection");
                toClientSocket = port.accept();
                System.out.println("ACCEPTED: " + ++connCounter + "\n");
                nextThread = new ConnThread(toClientSocket);
                nextThread.start();
            } catch (Exception e) {
                System.err.println("SERVER: " + e);
                System.exit(0);
            }
        } while(true);




    }
}