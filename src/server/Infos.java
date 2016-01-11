/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author mauriverti
 */
public class Infos {
    
    private String      name;
    private Integer     id;
    private Integer     memory;
    private Integer     vcpu;
    private Character   state;
    private Double      time;

    
    
    
    public Infos() {
        coisas = "";
    }
    
    String coisas;
    
    String hostname;
    Integer hostId;
    Integer cpuFree;
    Double memTot;
    Double memFree;
    Double memMax = 3500.0;
    Double memMin = 2048.0;
    
    public void printInfos(Socket client) {
        try {
        Scanner scanner = new Scanner(client.getInputStream());
        
        while (scanner.hasNextLine()) {
            String dados = scanner.nextLine();      // Host9-> 10   3  87   0   0   0|1117M 59.8M  328M  114M|15-12 13:01:07
            atribuiValores(dados);
            
//            System.out.println(dados);
            
        }   
        scanner.close();


        client.close();
        
        } catch(IOException e) {
            
        } catch (Exception e) {
            
        }
        
    }
    
    public void setInfo() {

        String command;
        command = "sudo xl list";

        Process proc;
        String info = "";
        try {
            proc = Runtime.getRuntime().exec(command);

            BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));

            String line = "";
            List<String> vmsInfo = new ArrayList<String>();

            while ((line = reader.readLine()) != null) {
                vmsInfo.add(line);
            }

        info = vmsInfo.get(1);
        
            proc.waitFor();
        } catch (Exception e) {
            
        }
        
        
        String[]infos = info.split(" ");
        List<String> propriedades = new ArrayList<String>();
        
        for (int i =0; i < infos.length; i++) {
            if (!infos[i].isEmpty()) {
                propriedades.add(infos[i]);
            }
        }
        
        name = propriedades.get(0);
        id = Integer.valueOf(propriedades.get(1));
        memory = Integer.valueOf(propriedades.get(2));
        vcpu = Integer.valueOf(propriedades.get(3));
        state = propriedades.get(4).replace("-", "").isEmpty() ? '-' : propriedades.get(4).replace("-", "").charAt(0);      // retorna o char do status, ou um - se nao tiver status
        time = Double.valueOf(propriedades.get(5));

    }
    
    public void printData() {
        System.out.println(hostId + ": " + hostname + " -> " + cpuFree + "\t" + memFree + "\t" + memTot);
    }
    
    public void atribuiValores(String data) {
        List<String> properties = new ArrayList<String>();
        hostname = data.split("->")[0];
        hostId = new Integer(hostname.split("-")[1]);
        
        String[]infos = data.split("->")[1].replace('|', ' ').replace('M', ' ').split(" ");
        
        for (String info : infos) {
            if (!info.isEmpty()) {
                properties.add(info);
            }
        }
        
        cpuFree = new Integer(properties.get(2));
        memFree = new Double(properties.get(9));
        memTot = new Double(properties.get(6)) + new Double(properties.get(7)) + new Double(properties.get(8)) + memFree;
        
        printData();
        setInfo();
        
        if (memFree < 128) {
            System.out.println("Precisa de memoria");
            if (memory+128 < memMax) {
                System.out.println("Alocando 128Mb");
                String allocateMemCommand = "sudo xl mem-set " + hostname + " " + (memory+128) + " ";
                try {
                    Runtime.getRuntime().exec(allocateMemCommand);
                } catch (Exception e) {
                    System.out.println("Erro ao tentar alocar memoria para" + hostname);
                    e.printStackTrace();
                }
            } else {
                System.out.println("Impossivel alocar, limite maximo alcançado");
            }
        } else {
            if (memFree > 512) {
                System.out.println("Memoria sobrando");
                if (memMin+128 < memory) {
                    System.out.println("Desalocando 128Mb");
                    String allocateMemCommand = "sudo xl mem-set " + hostname + " " + (memory-128) + " ";
                    try {
                        Runtime.getRuntime().exec(allocateMemCommand);
                    } catch (Exception e) {
                        System.out.println("Erro ao tentar alocar memoria para" + hostname);
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("Impossivel alocar, limite maximo alcançado");
                }
            }
        }
    }
}
