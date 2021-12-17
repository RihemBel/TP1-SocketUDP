package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class etd_send  extends  Thread  {
    DatagramSocket s;
    String ppseudo;

    public etd_send(DatagramSocket s, String ppseudo) {
        this.s = s;
        this.ppseudo = ppseudo;
    }
    public void run()  {
        while (true){
            // System.out.println("this is send thread");
            Scanner scn = new Scanner(System.in);
            String msg = scn.nextLine();
            DatagramPacket pk= null;
            try {
                pk = new DatagramPacket(msg.getBytes(StandardCharsets.UTF_8),msg.length(), InetAddress.getLocalHost(),4000);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
            try {
                s.send(pk);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}