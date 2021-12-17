package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.time.Clock;

public class Client {
    public static void main(String[] args) throws IOException {
        DatagramSocket ets = new DatagramSocket();
        String msg = null;
        Boolean login = false;
        while (!login) {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            msg = br.readLine();
            if (msg.startsWith("##")) {
                login = true;
            }

        }
        DatagramPacket datagramPacket=new DatagramPacket(msg.getBytes(StandardCharsets.UTF_8)
                , msg.length(), Inet4Address.getLocalHost(),4000);
        ets.send(datagramPacket);
        etd_send etd_send=new etd_send(ets,msg.substring(2));
        etd_rec etd_rec=new etd_rec(ets);
        etd_send.start();
        etd_rec.start();



    }
}