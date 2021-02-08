package com.springinventory.demo.model;

import javax.persistence.Id;

import javax.persistence.*;

@Entity
@Table(name = "item")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long itemNumber; //item no.
    private long quantity;
    private String name; // item name
    private String description; // item description
    @Column(unique = true)
    private String inventoryCode; // inventory code

    public Item(){
    }

    public Item(String _name, String _description, String _inventoryCode)
    {
        name = _name;
        description = _description;
        inventoryCode = _inventoryCode;
    }

    public long getItemNumber() {
        return itemNumber;
    }

    // may change it to lowest available number
    public void setItemNumber(long itemNumber) {
        this.itemNumber = itemNumber;
    }

    @Column(name = "name", nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setInventoryCode(String inventoryCode) {
        this.inventoryCode = inventoryCode;
    }

    public String getInventoryCode() {
        return inventoryCode;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }
}
