package model;

import java.util.List;

public class groupe {
    String name;
    List<etudiant> list;
    List<message> messages;

    public groupe(String name, List<etudiant> list) {
        this.name = name;
        this.list = list;
    }

    public groupe(String name, List<etudiant> list, List<message> messages) {
        this.name = name;
        this.list = list;
        this.messages = messages;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<etudiant> getList() {
        return list;
    }

    public void setList(List<etudiant> list) {
        this.list = list;
    }

    public List<message> getMessages() {
        return messages;
    }

    public void setMessages(List<message> messages) {
        this.messages = messages;
    }

    @Override
    public String toString() {
        return "groupe{" +
                "name='" + name + '\'' +
                ", list=" + list +
                ", messages=" + messages +
                '}';
    }
}
