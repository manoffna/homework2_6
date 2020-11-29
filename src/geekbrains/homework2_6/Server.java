package geekbrains.homework2_6;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

public class Server {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private DataInputStream in;
    private DataOutputStream out;

    public Server() {
        try {
            System.out.println("Сервер запускается");
            serverSocket = new ServerSocket(8099);

            System.out.println("Сервер ожидает подключения клиентов ...");
            clientSocket = serverSocket.accept();
            System.out.println("Клиент подключился: " + clientSocket);

            in = new DataInputStream(clientSocket.getInputStream());
            out = new DataOutputStream(clientSocket.getOutputStream());

            AtomicBoolean isDrop = new AtomicBoolean(false);

            new Thread(() -> {
                try {
                while(true) {
                    String incomingMessage = in.readUTF();
                    if (incomingMessage.contains("-quit")) {
                        out.writeUTF("cmd Bye!");
                        isDrop.set(true);
                        break;
                    }
                    System.out.println(incomingMessage);
            } System.out.println("Клиент отключается ... ");
                }catch (IOException e) {
                    e.printStackTrace();
                }
            } ).start();

            System.out.println("Current Server dialog:");
           new Thread(() -> {
               try {
                   while(true) {
                       if (isDrop.get()) {
                           System.out.println("Closing...");
                           break;
                       }
                    BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
                    String msg = bufferRead.readLine();
                    out.writeUTF("Server: " + msg);
                    }
                    //System.out.println("Клиент отключается ... ");
                }catch (IOException e) {
                        e.printStackTrace();
                    }
    } ).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeConnection() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Server();
    }
}

