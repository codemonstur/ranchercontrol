package ranchercontrol.pojos;

public final class ContainerInformation {

    public final String id;
    public final String type;
    public final String baseType;
    public final String name;
    public final String state;
    public final String accountId;
    public final String created;
    public final long createdTS;
    public final String description;
    public final String firstRunning;
    public final long firstRunningTS;
    public final String hostId;
    public final String hostname;
    public final String kind;
    public final String primaryIpAddress;
    public final String primaryNetworkId;

    public ContainerInformation(final String id, final String type, final String baseType, final String name
            , final String state, final String accountId, final String created, final long createdTS
            , final String description, final String firstRunning, final long firstRunningTS, final String hostId
            , final String hostname, final String kind, final String primaryIpAddress, final String primaryNetworkId) {
        this.id = id;
        this.type = type;
        this.baseType = baseType;
        this.name = name;
        this.state = state;
        this.accountId = accountId;
        this.created = created;
        this.createdTS = createdTS;
        this.description = description;
        this.firstRunning = firstRunning;
        this.firstRunningTS = firstRunningTS;
        this.hostId = hostId;
        this.hostname = hostname;
        this.kind = kind;
        this.primaryIpAddress = primaryIpAddress;
        this.primaryNetworkId = primaryNetworkId;
    }
}