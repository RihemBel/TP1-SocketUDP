package model;

public class message {
    etudiant src;
    etudiant dest;
    String msg;

    public message(etudiant src, etudiant dest, String msg) {
        this.src = src;
        this.dest = dest;
        this.msg = msg;
    }

    public etudiant getSrc() {
        return src;
    }

    public void setSrc(etudiant src) {
        this.src = src;
    }

    public etudiant getDest() {
        return dest;
    }

    public void setDest(etudiant dest) {
        this.dest = dest;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "message{" +
                "src=" + src +
                ", dest=" + dest +
                ", msg='" + msg + '\'' +
                '}';
    }
}
