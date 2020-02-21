package ranchercontrol.pojos.dtos;

import jcli.annotations.CliOption;

public final class CliArguments {
    @CliOption(name='s', longName = "service", isMandatory = true)
    public String serviceId;
    @CliOption(name='a', longName = "action", isMandatory = true)
    public String action;
}
