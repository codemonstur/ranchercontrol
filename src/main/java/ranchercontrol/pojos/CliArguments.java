package ranchercontrol.pojos;

import jcli.annotations.CliOption;

public final class CliArguments {
    @CliOption(name='f', defaultValue = ".env")
    public String propertiesFile;
    @CliOption(name='s', isMandatory = true)
    public String serviceId;
    @CliOption(name='a', isMandatory = true)
    public String action;
}
