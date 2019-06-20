package com.application.clientServer.firstVersion;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

/**
 * Created by Ihor on 6/20/2019.
 */
public class Server implements Runnable {
    ServerSocket serverSocket;
    volatile boolean keepProcessing = true;

    public Server(int port, int millisecondsTimeout) throws IOException {
        serverSocket = new ServerSocket(port);
        serverSocket.setSoTimeout(millisecondsTimeout);
    }

    public void run() {
        System.out.printf("Server starting\n");

        while (keepProcessing) {
            try {
                System.out.printf("accepting client\n");
                Socket socket = serverSocket.accept();
                System.out.printf("got client\n");
                process(socket);
            } catch (Exception e) {
                handle(e);
            }
        }
    }

    private void handle(Exception e) {
        if(!(e instanceof SocketException)) {
            e.printStackTrace();
        }
    }

    public void stopProcessing() {
        keepProcessing = false;
        closeIgnoringException(serverSocket);
    }

    void process(Socket socket) {
        if(socket == null)
            return;
        try {
            System.out.printf("Server: getting message\n");
            String message = MessageUtils.getMessage(socket);
            System.out.printf("Server: got message: %s\n", message);
            MessageUtils.sendMessage(socket, "Rrocessed: " + message);
            System.out.printf("Server: sent\n");
            closeIgnoringException(socket);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void closeIgnoringException(Socket socket) {
        if(socket != null)
            try {
                socket.close();
            } catch (IOException ignore){
                /*NOP*/
            }
    }

    private void closeIgnoringException(ServerSocket serverSocket) {
        if(serverSocket != null)
            try {
                serverSocket.close();
            } catch (IOException ignore){
                /*NOP*/
            }
    }

    public static void main(String[] args) {
        
    }
}
