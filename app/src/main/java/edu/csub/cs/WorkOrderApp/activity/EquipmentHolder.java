package edu.csub.cs.WorkOrderApp.activity;

public class EquipmentHolder {
    private int id;
    private String name;

    public EquipmentHolder() {
    }

    public EquipmentHolder(int id, String name) {
        super();
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public int getId() { return id;}
    public String getName() { return name;}
    public void setName(String name) { this.name = name;}
    public void setId(int id) { this.id = id;}
}
