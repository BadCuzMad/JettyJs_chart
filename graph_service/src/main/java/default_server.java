import org.eclipse.jetty.server.*;
@SuppressWarnings({"NotNullNullableValidation", "FieldCanBeLocal"})
public final class default_server {
    private final Server server = new Server();
    private static final int port = 8080;

    public Server build() {
        return build(port);
    }

    public Server build(int port) {
        final HttpConfiguration httpConfig = new HttpConfiguration();
        final HttpConnectionFactory httpConnectionFactory = new HttpConnectionFactory(httpConfig);
        final ServerConnector serverConnector = new ServerConnector(server, httpConnectionFactory);
        serverConnector.setHost("localhost");
        serverConnector.setPort(port);
        server.setConnectors(new Connector[]{serverConnector});
        return server;
    }
}
