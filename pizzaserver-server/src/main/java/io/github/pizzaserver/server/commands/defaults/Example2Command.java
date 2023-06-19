package io.github.pizzaserver.server.commands.defaults;

import com.nukkitx.protocol.bedrock.data.command.CommandParamData;
import io.github.pizzaserver.api.commands.CommandSender;
import io.github.pizzaserver.server.commands.ImplCommand;

import java.util.Arrays;

public class Example2Command extends ImplCommand {

    public Example2Command() {
        super("example2"); // Name for the command
        setDescription("This is for testing console autocompletion");
        setAliases(new String[]{"alias122"}); // A string array of aliases to be used

        // The declaration below is saying to have 2 different "paths" to choose, and each of those "paths" have 2 more
        // options to choose from
        overloads = new CommandParamData[2][2];

        // The below is the first group of parameters, param 1 will be values 001/002 while param 2 will be values 011/012
        // The first parameter is the name of the parameter as a whole, then the next is a boolean for being optional
        // Third are the options the parameter contains, the fourth is the type of parameter
        // The fifth is a postfix for your command (Such as /xp has l or e),
        // The last is a list of options to use which are enums in the CommandParamOption class
/*        overloads[0][0] = new CommandParamData(
                "name00", true, new CommandEnumData("CommandEnumDataName", new String[]{"values001", "values002"}, true),
                CommandParam.TEXT, null, new ArrayList<>()
        );
        overloads[0][1] = new CommandParamData(
                "name01", true, new CommandEnumData("CommandEnumDataName1", new String[]{"values011", "values012"}, true),
                CommandParam.TEXT, null, new ArrayList<>()
        );

        //The below is in the second group of parameters, param 1 will be values 101/102 while param 2 will be values 111/112
        registerParameter(1, 0, new CommandParamData(
                "name10", true, new CommandEnumData("CommandEnumDataName2", new String[]{"values101", "values102"}, true),
                CommandParam.TEXT, null, new ArrayList<>()
        ));
        registerParameter(1, 1, new CommandParamData(
                "name11", true, new CommandEnumData("CommandEnumDataName3", new String[]{"values111", "values112"}, true),
                CommandParam.TEXT, null, new ArrayList<>()
        ));*/

        // Simplest way to register a command parameter
        registerParameter(0, 0, "path0ID", new String[]{"path0Choice1", "path0Choice2"});
        registerParameter(0, 1, "path0IDNextPos", new String[]{"pos01", "pos02"});
        registerParameter(1, 0, "path2ID", new String[]{"path2Choice1", "path2Choice2"});
        registerParameter(1, 1, "path2IDNextPos", new String[]{"pos21", "pos22"});
    }

    @Override
    public void execute(CommandSender sender, String[] args, String label) {
        sender.sendMessage("Information222: " + Arrays.toString(args));
    }
}
