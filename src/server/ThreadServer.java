/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author server
 */
public class ThreadServer extends Thread {

    private final Socket client;

    private final Queue<VMData> datas = new LinkedList<>();
    private String entrada = "";
    private static final int TAM_FILA = 60;

    public ThreadServer(Socket accept) {
        this.client = accept;
    }

    @Override
    public void run() {
        try {
            System.out.println("Conexao com o cliente:" + client.getInetAddress().getHostAddress());

            ServerSocket nSocket = new ServerSocket();
            nSocket.bind(null);
            PrintStream sClient = new PrintStream(client.getOutputStream());
            sClient.println(nSocket.getLocalPort());
            final Socket accept = nSocket.accept();
            client.close();
            Scanner scanner = new Scanner(accept.getInputStream());
            if (scanner.hasNextLine()) {
                entrada = scanner.nextLine().split("->")[0];
            }

//                if (!entrada.isEmpty() && !connections.contains(entrada)) {
//                    connections.add(entrada);
//                    Infos vms = new Infos();
//                    vms.printInfos(client);
//                }
            PrintStream saida = new PrintStream(accept.getOutputStream());
            while (scanner.hasNextLine()) {
                final String line = scanner.nextLine().trim();
                final String[] fullSplit = line.split("\\|");

                VMData data = new VMData();
                data.setDomain(getEntrada());
                data.setCpuLivre(fullSplit[0].split(" ")[7]);

                String[] memoSplit = fullSplit[1].split(" ");
                data.setMemUsada(memoSplit[0]);
                System.out.println(memoSplit);
                data.setMemLivre(memoSplit[4]);

                getDatas().add(data);
                if (getDatas().size() > TAM_FILA) {
                    getDatas().poll();
                }
                saida.println(true);

            }
            scanner.close();
        } catch (IOException ex) {
        }
    }

    public synchronized Queue<VMData> getDatas() {
        return datas;
    }

    public String getEntrada() {
        return entrada;
    }

}
