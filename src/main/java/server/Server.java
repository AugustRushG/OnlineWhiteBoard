package server;

import constant.RegistryConstant;
import gui.ServerGUI;

import javax.net.ServerSocketFactory;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {

    // add in try and catch for port number and address
    static int portNumber = 1234;

    public static void main(String[] args) {
        try{
            LocateRegistry.createRegistry(1099);

            RemoteDraw remoteDraw = new RemoteDraw();
            Registry registry = LocateRegistry.getRegistry();
            // provide service
            registry.bind(RegistryConstant.REMOTE_DRAW, remoteDraw);
            System.out.println("All binds on registry");
        } catch (AccessException e) {
            throw new RuntimeException(e);
        } catch (AlreadyBoundException e) {
            throw new RuntimeException(e);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }


        ServerSocketFactory serverSocketFactory = ServerSocketFactory.getDefault();

        try{
            // create socket connection
            ServerSocket serverSocket = serverSocketFactory.createServerSocket(portNumber);
            // create gui for the server
            ServerGUI serverGUI = new ServerGUI();
            System.out.println("server socket ready for connection");
            // thread per connection to accept connection
            while (true){
                Socket client = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(client);
                new Thread(clientHandler).start();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
