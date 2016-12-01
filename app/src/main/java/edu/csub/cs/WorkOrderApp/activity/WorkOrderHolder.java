package edu.csub.cs.WorkOrderApp.activity;

import java.io.Serializable;

/**
 * Created by Ponism on 11/15/2016.
 */

public class WorkOrderHolder implements Serializable {
    private int wo_id;
    private String area;
    private String equipment;
    private String status;
    private String priority;
    private String create_date;
    private String emp;
    private String description;
    public WorkOrderHolder() {
    }

    public WorkOrderHolder(int id, String area, String equipment, String status, String priority, String create_date, String emp, String description) {
        super();
        this.wo_id = id;
        this.area = area;
        this.equipment = equipment;
        this.status = status;
        this.priority = priority;
        this.create_date = create_date;
        this.emp = emp;
        this.description = description;
    }

    @Override
    public String toString() {
        return area;
    }

    public String getArea() { return area; }
    public String getEquipment() { return equipment; }
    public String getStatus() { return status;}
    public String getPriority() { return priority;}
    public String getCreateDate() { return create_date;}
    public String getId() { return wo_id+"";}
    public String getEmp() { return emp+""; }
    public String getDescription() { return description; }

    public void setEmp(String emp) { this.emp = emp; }
    public void setCreateDate(String date) { this.create_date = date;}
    public void setArea(String name) { this.area = name;}
    public void setEquipment(String equipment) { this.equipment = equipment; }
    public void setStatus(String status) { this.status = status; }
    public void setPriority(String priority) { this.priority = priority; }
    public void setDescription(String description) { this.description = description; }
    public void setId(int id) { this.wo_id = id; }
}
