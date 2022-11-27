package com.build.stats

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.containers.RabbitMQContainer
import org.testcontainers.utility.DockerImageName

@SpringBootTest
internal class BuildStatsApplicationTest {

    @Test
    fun createContext() = Unit

    private companion object {

        @JvmStatic
        @Suppress("unused", "UPPER_BOUND_VIOLATED_WARNING")
        val POSTGRES: PostgreSQLContainer<*> =
            PostgreSQLContainer<PostgreSQLContainer<*>>(DockerImageName.parse("postgres:latest"))
                .withDatabaseName("postgres")
                .withUsername("postgres")
                .withPassword("postgres")
                .withExposedPorts(5432)
                .apply {
                    start()
                    System.setProperty(
                        "spring.datasource.url",
                        "jdbc:postgresql://%s:%s/postgres"
                            .format(host, getMappedPort(5432))
                    )
                }

        @JvmStatic
        @Suppress("unused")
        val RABBIT: RabbitMQContainer =
            RabbitMQContainer(DockerImageName.parse("rabbitmq:3-management-alpine"))
                .withExposedPorts(5672)
                .apply {
                    start()
                    System.setProperty("spring.rabbitmq.host", host);
                    System.setProperty("spring.rabbitmq.port", getMappedPort(5672).toString());
                }
    }
}