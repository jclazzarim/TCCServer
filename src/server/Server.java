package server;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @author mauriverti
 */
public class Server {

    public static Integer portEntrada = 8910;
    public static Integer portVez = 8911;
//    public static Integer portEntrada = 12000;
    private static List<String> connections = new ArrayList<>();

    public static void main(String[] args) throws IOException {

//        for (int i = 0; i < 10; i++) {
        final Thread thread = new Thread(() -> {
            Socket client;
            try (ServerSocket server = new ServerSocket(portEntrada)) {
                client = server.accept();

                System.out.println("Conexao com o cliente:" + client.getInetAddress().getHostAddress());

                ServerSocket nSocket = new ServerSocket();
                nSocket.bind(null);
                System.out.println(nSocket.getLocalPort());
                PrintStream sClient = new PrintStream(client.getOutputStream());
                sClient.println(nSocket.getLocalPort());
                final Socket accept = nSocket.accept();

                Scanner scanner = new Scanner(accept.getInputStream());
                String entrada = "";
                if (scanner.hasNextLine()) {
                    entrada = scanner.nextLine().split("->")[0];
                }

//                if (!entrada.isEmpty() && !connections.contains(entrada)) {
//                    connections.add(entrada);
//                    Infos vms = new Infos();
//                    vms.printInfos(client);
//                }
                while (scanner.hasNextLine()) {
                    System.out.println(scanner.nextLine());
                }
                scanner.close();

                client.close();
            } catch (IOException ioE) {
                System.out.println("Problemas em criar socket Server, porta ocupada?");
            } catch (Exception e) {
                System.out.println("Problemas em criar socket Server");
            }
        });
//            portEntrada++;
        thread.start();
//        }
    }

}
