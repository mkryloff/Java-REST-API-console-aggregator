package mayhem_project;

import mayhem_project.processing.AutoModProcessor;
import mayhem_project.processing.InteractiveModProcessor;

public class Main
{
    public static void main(String[] args)
    {
        if (args.length == 1 && args[0].equalsIgnoreCase("INTERACTIVE"))
        {
            InteractiveModProcessor.userRun();
        }
        else if (args.length >= 5 && args[0].equalsIgnoreCase("AUTO"))
        {
            AutoModProcessor.autoRun(args);
        }
        else
        {
            System.err.println("""
                    Incorrect set of command line arguments:
                    2 mods available:
                     1. INTERACTIVE\s
                     2. AUTO <list of APIs sep by comma> <output file type (JSON or CSV)> <amount of max threads> <time interval in seconds for api requesting>""");
        }
    }
}

