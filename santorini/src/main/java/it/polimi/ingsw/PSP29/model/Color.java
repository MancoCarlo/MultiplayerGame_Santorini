package it.polimi.ingsw.PSP29.model;

public enum Color
{
    ANSI_LEVEL1("\u001B[38;5;102m"),
    ANSI_LEVEL2("\u001B[38;5;66m"),
    ANSI_LEVEL3("\u001B[38;5;80m"),
    ANSI_LEVEL4("\u001B[38;5;239m"),
    ANSI_RED("\u001B[31m"),
    ANSI_YELLOW("\u001B[33m"),
    ANSI_BLUE("\u001B[34m");

    public static final String RESET = "\u001B[0m";


    private String escape;


    Color(String escape)
    {
        this.escape = escape;
    }


    public String getEscape()
    {
        return escape;
    }


    @Override
    public String toString()
    {
        return escape;
    }
}
