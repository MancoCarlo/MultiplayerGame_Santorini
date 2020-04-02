package it.polimi.ingsw.PSP29.model;

public class God {
    private int ID;
    private String name;
    private String description;

    public God(int i, String n, String d){
        ID=i;
        name=n;
        description=d;

    }

    public int getID() { return ID; }

    public String getName() { return name; }

    public String getDescription() { return description; }

}
