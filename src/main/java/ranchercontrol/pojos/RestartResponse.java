package ranchercontrol.pojos;

public final class RestartResponse {
    public final String id;
    public final String type; // "service",
    public final String name; // "redis",
    public final String state; // "restarting",
    public final String created; // "2019-06-20T13:18:15Z",
    public final long createdTS; // 1561036695000,
    public final String healthState; // "healthy",
    public final int scale; // 1,
    public final String transitioning; // "yes",
    public final String transitioningMessage; // "In Progress",

    public RestartResponse(final String id, final String type, final String name, final String state
            , final String created, final long createdTS, final String healthState, final int scale
            , final String transitioning, final String transitioningMessage) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.state = state;
        this.created = created;
        this.createdTS = createdTS;
        this.healthState = healthState;
        this.scale = scale;
        this.transitioning = transitioning;
        this.transitioningMessage = transitioningMessage;
    }
}
