package io.github.pizzaserver.api.commands;

import java.util.HashSet;
import java.util.Set;

public class CommandEnum {

    public static final CommandEnum COMMAND_ENUM_BOOLEAN = new CommandEnum("Boolean", new HashSet<String>(){{
            this.add("true");
            this.add("false");
        }});

    private String name;
    private Set<String> values;
    private String postFix;

    public CommandEnum(String name, Set<String> values) {
        this(name, values, "");
    }

    public CommandEnum(String name, Set<String> values, String postFix) {
        this.name = name;
        this.values = values;
        this.postFix = postFix;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<String> getValues() {
        return values;
    }

    public void setValues(Set<String> values) {
        this.values = values;
    }

    public String getPostFix() {
        return postFix;
    }

    public void setPostFix(String postFix) {
        this.postFix = postFix;
    }

    public boolean hasPostFix() {
        return !(postFix == null || postFix.trim().equals(""));
    }
}
