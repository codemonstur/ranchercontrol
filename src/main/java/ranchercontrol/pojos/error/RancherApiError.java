package ranchercontrol.pojos.error;

import java.net.http.HttpResponse;

public final class RancherApiError extends Exception {
    public final int statusCode;
    public final String response;

    public RancherApiError(final HttpResponse<String> response, final String message) {
        super(message);
        this.statusCode = response.statusCode();
        this.response = response.body();
    }
}
