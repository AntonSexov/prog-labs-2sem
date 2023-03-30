package ifmo.requests;

import ifmo.commands.Command;

import java.io.Serializable;

public abstract class Request implements Serializable {
    private String name;
    private String arguments;

    public Request(String name, String arguments){
        this.name = name;
        this.arguments = arguments;
    }

    public String getCommandName() {
        return name;
    }

    public String getArguments() {
        return arguments;
    }
}
