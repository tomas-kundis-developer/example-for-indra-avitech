package org.tomo.infrastructure.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.cdimascio.dotenv.Dotenv;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.tomo.domain.User;
import org.tomo.infrastructure.datasource.SqlDataSource;

class UserJdbcRepositoryTest {

  @BeforeEach
  void beforeEach() throws InterruptedException {
    // TODO 2022-11-13 TOKU: Extract as separate DB initialization routine for tests.

    Dotenv dotenv = Dotenv.load();

    // Customized JDBC URL and used PUBLIC schema instead of custom schema.

    String schemaFile =
        Objects.requireNonNull(UserJdbcRepositoryTest.class.getResource("/h2-schema.sql")).getFile()
            .substring(1);
    String testJdbcUrl = "jdbc:h2:mem:test;INIT=RUNSCRIPT FROM '" + schemaFile + "'";

    SqlDataSource.SqlDataSourceBuilder sqlDataSourceBuilder =
        new SqlDataSource.SqlDataSourceBuilder();

    sqlDataSourceBuilder.setJdbcUrl(testJdbcUrl);

    // Schema must exist during Hikari pool creating. h2-schema.sql is started later.
    sqlDataSourceBuilder.setSchema("PUBLIC");

    // Other database parameters same as in production environment.

    sqlDataSourceBuilder.setUsername(dotenv.get("DB_USERNAME"));
    sqlDataSourceBuilder.setPassword(dotenv.get("DB_PASSWORD"));
    sqlDataSourceBuilder.setHikariPropertiesFile(dotenv.get("HIKARI_PROPERTIES_FILE"));

    sqlDataSourceBuilder.build();

    // Sometimes some tests fail because table susers doesn't exist.
    // It seems that there is problem with long/late running H2 schema initialization script.
    // It seems that this workaround helps.
    // TODO 2022-11-14 TOKU: Fix SonarLint: "Thread.sleep" should not be used in tests (java:S2925)
    Thread.sleep(2000);
  }

  @AfterEach
  void afterEach() {
    SqlDataSource.shutdown();
  }

  @Test
  void add() {
    UserJdbcRepository repo = UserJdbcRepository.getInstance();

    long id = 11333L;
    String guid = "fdeb9455-7b56-4cc4-bd41-4feccb3b54b1";
    repo.add(new User(id, guid, "Test User 11-333"));

    User savedUser = repo.findAll().iterator().next();

    assertThat(savedUser.getId()).isEqualTo(id);
    assertThat(savedUser.getGuid()).isEqualTo(guid);
    assertThat(savedUser.getName()).isEqualTo("Test User 11-333");
  }

  @Test
  void addNewGenerateId() {
    UserJdbcRepository repo = UserJdbcRepository.getInstance();

    String guid = "6029cc29-e212-409b-9995-b243ee7d88d9";
    User testUser1 = repo.addNewGenerateId(new User(-99999L, guid, "Test User 1"));

    assertThat(testUser1.getId()).isNotEqualTo(-99999L);
    assertThat(testUser1.getGuid()).isEqualTo(guid);
    assertThat(testUser1.getName()).isEqualTo("Test User 1");
  }

  @Test
  void findAll() {
    UserJdbcRepository repo = UserJdbcRepository.getInstance();

    List<User> usersList1 = new ArrayList<>();
    repo.findAll().forEach(usersList1::add);

    assertThat(usersList1).isEmpty();

    repo.addNewGenerateId(new User(-99999L, "4b2022ef-a48f-410c-813d-3524aa73987c", "Test User 2"));

    List<User> usersList2 = new ArrayList<>();
    repo.findAll().forEach(usersList2::add);

    assertThat(usersList2).hasSize(1);
  }

  @Test
  void deleteAll() {
    UserJdbcRepository repo = UserJdbcRepository.getInstance();

    repo.addNewGenerateId(
        new User(-99999L, "9fc51180-e5eb-4ef8-8b65-e3e227befcb1", "Test User 501"));
    repo.addNewGenerateId(
        new User(-99999L, "22415873-5872-4117-8abe-6e4f9f41ee39", "Test User 502"));
    repo.addNewGenerateId(
        new User(-99999L, "5388bf94-d3ca-42eb-b92a-23e00e44c299", "Test User 503"));

    List<User> usersList1 = new ArrayList<>();
    repo.findAll().forEach(usersList1::add);

    assertThat(usersList1).hasSize(3);

    repo.deleteAll();

    List<User> usersList2 = new ArrayList<>();
    repo.findAll().forEach(usersList2::add);

    assertThat(usersList2).isEmpty();

    repo.addNewGenerateId(
        new User(-99999L, "1e8deb9a-ea57-4211-a6e8-208357c1ef03", "Test User 504"));
    repo.addNewGenerateId(
        new User(-99999L, "1d170ae3-41d5-4816-948a-3ddaa59eca1c", "Test User 505"));

    List<User> usersList3 = new ArrayList<>();
    repo.findAll().forEach(usersList3::add);

    assertThat(usersList3).hasSize(2);

    repo.deleteAll();

    List<User> usersList4 = new ArrayList<>();
    repo.findAll().forEach(usersList4::add);

    assertThat(usersList4).isEmpty();
  }
}