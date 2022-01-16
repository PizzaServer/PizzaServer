package io.github.pizzaserver.api.player.form;

public abstract class Form {

    public static int ID = 0;

    protected final transient int id;
    protected final FormType type;
    protected final String title;

    protected Form(FormType formType, String title) {
        this.id = ID++;
        this.type = formType;
        this.title = title;
    }

    public FormType getType() {
        return this.type;
    }

    public int getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }
}
