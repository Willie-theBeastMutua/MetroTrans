package com.example.metrotrans;

public class schedule {
    public String fro;
    public String to;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }



    public String userid;

    public String getFro() {
        return fro;
    }

    public void setFro(String fro) {
        this.fro = fro;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getVehregno() {
        return vehregno;
    }

    public void setVehregno(String vehregno) {
        this.vehregno = vehregno;
    }

    public String getSeatsno() {
        return seatsno;
    }

    public void setSeatsno(String seatsno) {
        this.seatsno = seatsno;
    }

    public String getTimer() {
        return timer;
    }

    public void setTimer(String timer) {
        this.timer = timer;
    }

    public String vehregno;
    public String seatsno;
    public String timer;

    public schedule() {
    }

    public schedule(String fro, String to, String vehregno, String seatsno, String timer, String userid) {
        this.fro = fro;
        this.to = to;
        this.vehregno = vehregno;
        this.seatsno = seatsno;
        this.timer = timer;
        this.userid = userid;
    }
}
