package com.project.delivery.model.enums;

public enum Status {
    WAIT(1),
    PICKUP(2),
    DEPART(3),
    ARRIVAL(4);

    private int status;

    Status(int status) {
        this.status = status;
    }

    public int getStatus(){
        return status;
    }
}