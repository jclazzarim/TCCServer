package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * @author mauriverti
 */
public class Server {

    public static Integer portEntrada = 8910;
    public final static Map<String, ThreadServer> vms = new HashMap<>();

    public static void main(String[] args) throws IOException {

        new Thread(() -> {
            int x = 0;
            try (ServerSocket server = new ServerSocket(portEntrada)) {
                System.out.println(++x);
                while (true) {
                    Socket client = server.accept();

                    ThreadServer vm = new ThreadServer(client);
                    vm.start();

                    Thread.sleep(1000);//Gambeta das brava... pq né... SigW
                    System.out.println(vm.getEntrada());
                    vms.put(vm.getEntrada(), vm);

                }
            } catch (IOException ioE) {
                System.out.println("Problemas em criar socket Server, porta ocupada?");
            } catch (Exception e) {
                System.out.println("Problemas em criar socket Server");
            }
        }).start();
        
        System.out.println("saiu thread");

    }
}
