package edu.csub.cs.WorkOrderApp.activity;

public class EquipmentHolder {
    private int room_id;
    private String name;
    private int id;
    public EquipmentHolder() {
    }

    public EquipmentHolder(int id, int room_id, String name) {
        super();
        this.id = id;
        this.room_id = room_id;
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public int getId() { return id;}
    public int getRoomId() { return room_id;}
    public String getName() { return name;}
    public void setName(String name) { this.name = name;}
    public void setId(int id) { this.id = id;}
}
