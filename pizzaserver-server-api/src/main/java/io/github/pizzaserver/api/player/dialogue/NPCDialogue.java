package io.github.pizzaserver.api.player.dialogue;

public class NPCDialogue {

    public static int ID = 0;

    protected final int id;


    public NPCDialogue() {
        this.id = ID++;
    }

    public int getId() {
        return this.id;
    }

}
