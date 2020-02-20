package ranchercontrol.core;

import com.google.gson.Gson;
import ranchercontrol.pojos.ContainerInformation;
import ranchercontrol.pojos.RestartResponse;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.util.Base64;
import java.util.Properties;

import static java.nio.charset.StandardCharsets.UTF_8;

public final class RancherClient {
    private static final String
        CATTLE_SCHEME = "CATTLE_SCHEME",
        CATTLE_HOST = "CATTLE_HOST",
        CATTLE_PORT = "CATTLE_PORT",
        CATTLE_ACCESS_KEY = "CATTLE_ACCESS_KEY",
        CATTLE_SECRET_KEY = "CATTLE_SECRET_KEY",
        CATTLE_ACCOUNT = "CATTLE_ACCOUNT";

    private static final String
        BASE_URL = "%s://%s:%s/v2-beta/projects/%s/services/%s/",
        START_CONTAINER = "?action=activate",
        RESTART_CONTAINER = "?action=restart";

    private final Properties properties;
    private final HttpClient http;
    private final Gson gson;
    private final String accountId;

    public RancherClient(final Properties properties, final HttpClient http, final Gson gson) {
        this(properties, http, gson, properties.getProperty(CATTLE_ACCOUNT));
    }
    public RancherClient(final Properties properties, final HttpClient http, final Gson gson, final String accountId) {
        this.properties = properties;
        this.http = http;
        this.gson = gson;
        this.accountId = accountId;
    }

    public ContainerInformation getContainerInformation(final String serviceId) throws IOException {
        final var request = newRancherRequest(properties, accountId, serviceId, "").GET().build();
        try {
            final var response = http.send(request, HttpResponse.BodyHandlers.ofString());
            if (!isSuccessful(response)) throw new IOException("Server returned wrong error code: " + response.statusCode());
            return gson.fromJson(response.body(), ContainerInformation.class);
        } catch (InterruptedException e) {
            throw new IOException("Interrupted during IO", e);
        }
    }

    private static boolean isSuccessful(final HttpResponse<?> response) {
        return response.statusCode() >= 200 && response.statusCode() <= 299;
    }

    public void runContainer(final String serviceId) throws IOException, RancherApiError {
        final ContainerInformation info = getContainerInformation(serviceId);
        switch (info.state) {
            case "inactive": startContainer(serviceId); break;
            case "active": restartContainer(serviceId); break;
            default: throw new IllegalStateException("Service " + serviceId + " is in invalid state " + info.state);
        }
    }

    public void startContainer(final String serviceId) throws IOException, RancherApiError {
        final var request = newRancherRequest(properties, accountId, serviceId, START_CONTAINER)
                .POST(BodyPublishers.noBody()).build();
        try {
            final var response = http.send(request, HttpResponse.BodyHandlers.ofString());
            if (!isSuccessful(response))
                throw new RancherApiError(response, "Starting service " + serviceId + " failed");
        } catch (InterruptedException e) {
            throw new IOException("Interrupted during IO", e);
        }
    }

    public void restartContainer(final String serviceId) throws IOException, RancherApiError {
        final var request = newRancherRequest(properties, accountId, serviceId, RESTART_CONTAINER)
                .POST(newRestartBody(1, 2000)).build();
        try {
            final var response = http.send(request, HttpResponse.BodyHandlers.ofString());
            if (!isSuccessful(response))
                throw new RancherApiError(response, "Restarting service " + serviceId + " failed");

            final RestartResponse restart = gson.fromJson(response.body(), RestartResponse.class);
            System.out.println(String.format("[%s] %s - %s", restart.created, restart.name, restart.transitioningMessage));
        } catch (InterruptedException e) {
            throw new IOException("Interrupted during IO", e);
        }
    }

    private static class RestartBody {
        private final RestartStrategy rollingRestartStrategy;
        private RestartBody(final int batchSize, final int intervalMillis) {
            this.rollingRestartStrategy = new RestartStrategy(batchSize, intervalMillis);
        }
    }
    private static class RestartStrategy {
        private final int batchSize;
        private final int intervalMillis;
        private RestartStrategy(final int batchSize, final int intervalMillis) {
            this.batchSize = batchSize;
            this.intervalMillis = intervalMillis;
        }
    }
    private BodyPublisher newRestartBody(final int batchSize, final int intervalMillis) {
        return BodyPublishers.ofString(gson.toJson(new RestartBody(batchSize, intervalMillis)));
    }

    private static String toCredentials(final Properties props) {
        return encodeBase64(props.getProperty(CATTLE_ACCESS_KEY) + ":" + props.getProperty(CATTLE_SECRET_KEY));
    }

    private static String encodeBase64(final String input) {
        return Base64.getEncoder().encodeToString(input.getBytes(UTF_8));
    }

    private static HttpRequest.Builder newRancherRequest(final Properties properties, final String accountId, final String serviceId, final String action) {
        return HttpRequest.newBuilder()
            .uri(URI.create(toContainerBaseUrl(properties, accountId, serviceId) + action))
            .header("Authorization", "Basic " + toCredentials(properties))
            .header("Accept", "application/json");
    }

    private static String toContainerBaseUrl(final Properties props, final String accountId, final String appId) {
        return String.format
            ( BASE_URL
            , props.getProperty(CATTLE_SCHEME)
            , props.getProperty(CATTLE_HOST)
            , props.getProperty(CATTLE_PORT)
            , accountId
            , appId
            );
    }

    public void stopContainer(final String serviceId) {
        throw new IllegalArgumentException();
    }
}
