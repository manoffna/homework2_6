package geekbrains.homework2_6;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

public class Client {
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    public Client() {
        try {
            System.out.println("Подключение к серверу ...");
            socket = new Socket("localhost", 8099);
            System.out.println("Подключение к серверу установлено: " + socket);

            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            AtomicBoolean isDrop = new AtomicBoolean(false);

            new Thread(() -> {
                try {
                    while (true) {
                        String incomingMessage = in.readUTF();
                        if (incomingMessage.contains("cmd Bye!")) {
                            System.out.println(incomingMessage);
                            System.out.println("Нажмите ENTER, чтобы выйти из командной строки");
                            isDrop.set(true);
                            break;
                        }
                        System.out.println(incomingMessage);
                    }
                }catch (IOException e) {
                    e.printStackTrace();
                };
            }).start();

            System.out.println("Current client dialog: ");
            new Thread(() -> {
                     while (true) {
                        if (isDrop.get()) {
                            System.out.println("Closing...");
                            break;
                        }
                        try {
                            BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
                            String msg = bufferRead.readLine();
                            out.writeUTF("Client:" + msg);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    //out.writeUTF(scanner.nextLine());
                }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMsg(String msg) {
        try {
            out.writeUTF(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Client socket = new Client();
    }
}
