package ranchercontrol;

import bobthebuildtool.pojos.buildfile.Project;
import com.google.gson.Gson;
import jcli.errors.InvalidCommandLine;
import ranchercontrol.core.RancherApiError;
import ranchercontrol.core.RancherClient;
import ranchercontrol.pojos.Action;
import ranchercontrol.pojos.CliArguments;

import java.io.IOException;
import java.net.http.HttpClient;
import java.util.Map;

import static jcli.CliParserBuilder.newCliParser;
import static ranchercontrol.core.CliParser.loadProperties;

public enum BobPlugin {;

    public static void installPlugin(final Project project) {
        project.addCommand("rc", "Send commands to rancher instance", BobPlugin::ranchercontrol);
    }

    private static int ranchercontrol(final Project project, final Map<String, String> environment, final String[] args)
            throws IOException, InvalidCommandLine, RancherApiError {
        final var arguments = newCliParser(CliArguments::new).parse(args);

        final var action = Action.valueOf(arguments.action);
        final var props = loadProperties(arguments.propertiesFile);
        final var http = HttpClient.newHttpClient();
        final var gson = new Gson();

        final var rancher = new RancherClient(props, http, gson);
        switch (action) {
            case start: rancher.startContainer(arguments.serviceId); break;
            case stop: rancher.stopContainer(arguments.serviceId); break;
            case restart: rancher.restartContainer(arguments.serviceId); break;
            case run: rancher.runContainer(arguments.serviceId); break;
        }
        return 0;
    }
}
