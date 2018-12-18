package com.google.mbarte.barte_movieworld;

public class ListSchedule
{
    private String head;
    private String desc;
    private String bottoms;

    public ListSchedule(String head, String desc, String bottoms) {
        this.head = head;
        this.desc = desc;
        this.bottoms = bottoms;
    }

    public String getHead() {
        return head;
    }

    public String getDesc() {
        return desc;
    }

    public String getBottoms() {
        return bottoms;
    }
}
