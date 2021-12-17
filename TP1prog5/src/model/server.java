package model;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class server {
    static List<etudiant> etudiants=new ArrayList<>();
    static List<groupe> groupes=new ArrayList<>();
    static List<message> messages=new ArrayList<>();
    public  boolean Search(String name){
        Boolean exist=false;
        for (int i=0;i<etudiants.size();i++){
            if (etudiants.get(i).getNom().equals(name)){
                exist=true;
            }
        }
        return exist;
    }
    public  boolean SearchGroupe(String name){
        Boolean exist=false;
        for (int i=0;i<groupes.size();i++){
            if (groupes.get(i).getName().equals(name)){
                exist=true;
            }
        }
        return exist;
    }
    public  etudiant GetEtudiant(InetAddress address,int port){
        etudiant e=null;
        for (int i=0;i<etudiants.size();i++){
            if (etudiants.get(i).getAddr().equals(address) && etudiants.get(i).getPort()==port){
                e=new etudiant(etudiants.get(i).getNom(),etudiants.get(i).getAddr(),etudiants.get(i).getPort());
            }
        }
        return e;
    }
    public  etudiant getEtudiantByName(String name){
        etudiant e=null;
        for (int i=0;i<etudiants.size();i++){
            if (etudiants.get(i).getNom().equals(name)){
                e=new etudiant(etudiants.get(i).getNom(),etudiants.get(i).getAddr(),etudiants.get(i).getPort());
            }
        }
        return e;
    }
    public  groupe getGroupeByName(String name){
        groupe e=null;
        for (int i=0;i<groupes.size();i++){
            if (groupes.get(i).getName().equals(name)){
                e=new groupe(groupes.get(i).getName(),groupes.get(i).getList());
                return e;
            }

        }
        return e;
    }
    public  int GetIndexOfGroupe(String name){
        int index=-1;
        for (int i=0;i<groupes.size();i++){
            if (groupes.get(i).getName().equals(name)){
                return i ;
            }

        } return index;
    }
    public static void main(String[] args) throws IOException {
        int port = 4000;

        DatagramSocket s=new DatagramSocket(4000);
        while (true) {

            try {
                byte[] dataSnd = new byte[1024];
                DatagramPacket pkRcv = new DatagramPacket(new byte[1024], 1024);
                s.receive(pkRcv);
                String msg = new String(pkRcv.getData()).trim();
                String msgsend = "";
                String m=msg.substring(2);
                if (msg.startsWith("##")) {
                    if (!new server().Search(m)) {
                        etudiant etudiant = new etudiant(m, (InetAddress) pkRcv.getAddress(), pkRcv.getPort());
                        etudiants.add(etudiant);
                        msgsend="welcome "+etudiant.getNom()+" inscription faite avec succes"+"\n";
                    }
                    else {
                        msgsend="welcome "+m+" tu es deja inscrit"+"\n";}
                } else if (msg.startsWith("#list")) {
                    for (int i = 0; i < etudiants.size(); i++) {
                        msgsend += "\n" + etudiants.get(i).getNom();
                    }

                } else if (msg.startsWith("#histo")) {
                    for (int i = 0; i < messages.size(); i++) {
                        System.out.println(messages.get(i));
                        if ((messages.get(i).getSrc().getAddr().equals(pkRcv.getAddress()) && messages.get(i).getSrc().getPort() == pkRcv.getPort()) || (messages.get(i).getDest().getAddr().equals(pkRcv.getAddress() ) && messages.get(i).getDest().getPort() == pkRcv.getPort()))
                            msgsend += "\nde: " + messages.get(i).getSrc().getNom()+" : "+messages.get(i).getMsg();
                    }
                } else if (msg.startsWith("@#")) {
                    etudiant e = new server().GetEtudiant((InetAddress) pkRcv.getAddress(),pkRcv.getPort());
                    if (new server().getEtudiantByName(new String(pkRcv.getData()).trim().substring(2, new String(pkRcv.getData()).trim().indexOf("@#", 2))) != null) {
                        etudiant e2 = new server().getEtudiantByName(new String(pkRcv.getData()).trim().substring(2, new String(pkRcv.getData()).trim().indexOf("@#", 2)));
                        message message = new message(e, e2, new String(new String(pkRcv.getData()).trim().substring(new String(pkRcv.getData()).trim().indexOf("@#", 2) +2)));
                        System.out.println("message from function @# "+message);
                        messages.add(message);
                        InetAddress IPAddress = e2.getAddr();
                        int portClt = e2.getPort();
                        System.out.println(msgsend);
                        dataSnd =(e.getNom()+" > "+ message.getMsg()).getBytes();
                        DatagramPacket pkSend = new DatagramPacket(dataSnd, dataSnd.length, IPAddress, portClt);
                        s.send(pkSend);
                        continue;
                    } else {
                        msgsend = "client doesnt exist";
                    }
                } else if (msg.startsWith("#GRPS")) {
                    for (int i = 0; i < groupes.size(); i++) {
                        msgsend += "\n" + groupes.get(i).getName();
                    }


                } else if (msg.startsWith("#GRP#")) {
                    if (!new server().SearchGroupe(msg.substring(5))) {
                        List<etudiant> list = new ArrayList<>();
                        List<message> listmessages = new ArrayList<>();
                        list.add(new server().GetEtudiant((InetAddress) pkRcv.getAddress(),pkRcv.getPort()));
                        groupe groupe = new groupe(msg.substring(5), list,listmessages);
                        groupes.add(groupe);
                    } else msgsend = "groupe existe";
                } else if (msg.startsWith("#>")) {

                    if (new server().SearchGroupe(msg.substring(2))) {
                        etudiant e = new server().GetEtudiant((InetAddress) pkRcv.getAddress(),pkRcv.getPort());
                        int index = new server().GetIndexOfGroupe(msg.substring(2));
                        groupes.get(index).getList().add(e);
                        msgsend="vous avez rejoindre le groupe "+ groupes.get(index).getName()+" avec succe";
                    } else
                        msgsend = "groupe n'existe pas ";
                } else if (msg.startsWith("#ETDS#")) {
                    if (new server().SearchGroupe(msg.substring(6))) {
                        groupe groupe = groupes.get(new server().GetIndexOfGroupe(msg.substring(6)));
                        for (etudiant e : groupe.getList()) {
                            msgsend += "\n" + e.getNom();
                        }
                    }

                } else if (msg.startsWith("@>")) {
                    if (new server().SearchGroupe(new String(pkRcv.getData()).trim().substring(2, new String(pkRcv.getData()).trim().indexOf("@>", 2)))) {
                        groupe groupe = new server().getGroupeByName(new String(pkRcv.getData()).trim().substring(2, new String(pkRcv.getData()).trim().indexOf("@>", 2)));
                        etudiant e = new server().GetEtudiant((InetAddress) pkRcv.getAddress(),pkRcv.getPort());

                        for (int i = 0; i < groupe.getList().size(); i++) {
                            etudiant e2 = new server().getEtudiantByName(groupe.getList().get(i).getNom());
                            message message = new message(e, e2, new String(new String(pkRcv.getData()).trim().substring(new String(pkRcv.getData()).trim().indexOf("@>", 2) + 2)));
                            messages.add(message);
                            groupes.get(new server().GetIndexOfGroupe(groupe.getName())).getMessages().add(message);
                            InetAddress IPAddress = groupe.getList().get(i).getAddr();
                            int portClt = groupe.getList().get(i).getPort();
                            System.out.println(msgsend);
                            dataSnd = ("Group "+groupe.getName()+"::"+e.getNom()+"::"+message.getMsg()).getBytes();
                            DatagramPacket pkSend = new DatagramPacket(dataSnd, dataSnd.length, IPAddress, portClt);
                            s.send(pkSend);
                        }
                    }
                    continue;
                }
                InetAddress IPAddress = pkRcv.getAddress();
                int portClt = pkRcv.getPort();
                System.out.println(msgsend);
                dataSnd = msgsend.getBytes();
                DatagramPacket pkSend = new DatagramPacket(dataSnd, dataSnd.length, IPAddress, portClt);
                s.send(pkSend);

            }catch (Exception e){
                e.printStackTrace();
            }}
    }

}
