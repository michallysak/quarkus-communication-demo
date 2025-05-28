package pl.michallysak.utils;

import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports.Binding;
import org.eclipse.microprofile.config.ConfigProvider;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.utility.DockerImageName;

public class TestMongoContainer {

    private final static String MONGODB_CONNECTION_STRING_PROPERTY_NAME = "quarkus.mongodb.connection-string";
    private final static String FULL_MONGO_IMAGE = "docker.io/library/mongo:7.0";
    private final static String MONGO_IMAGE = "mongo";
    private final static int TEST_CONTAINERS_MONGO_PORT_IN = 27017;

    public static MongoDBContainer create() {
        int mongoPort = getMongoPort();

        return new MongoDBContainer(
            DockerImageName.parse(FULL_MONGO_IMAGE).asCompatibleSubstituteFor(MONGO_IMAGE)
        )
            .withExposedPorts(TEST_CONTAINERS_MONGO_PORT_IN)
            .withCreateContainerCmdModifier(cmd -> cmd.withHostConfig(
                new HostConfig().withPortBindings(
                    new PortBinding(
                        Binding.bindPort(mongoPort),
                        new ExposedPort(TEST_CONTAINERS_MONGO_PORT_IN)
                    )
                )
            ));
    }

    private static int getMongoPort() {
        return ConfigProvider.getConfig().getOptionalValue(MONGODB_CONNECTION_STRING_PROPERTY_NAME, String.class)
            .map(splittedConnectionString -> splittedConnectionString.split(":"))
            .map(portPart -> portPart[portPart.length - 1])
            .map(Integer::parseInt)
            .orElseThrow();
    }

}
