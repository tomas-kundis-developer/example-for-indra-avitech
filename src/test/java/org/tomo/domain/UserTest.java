package org.tomo.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserTest {

  private User user1;
  private User user2;
  private User user3;

  @BeforeEach
  void beforeEach() {
    user1 = new User(1001L);
    user1.setGuid("2aebcddc-8190-4b53-9d4c-ced7cb1aeee3");
    user1.setName("Imre Anderovsky");

    user2 = new User(1001L);
    user2.setGuid("2aebcddc-8190-4b53-9d4c-ced7cb1aeee3");
    user2.setName("Imre Anderovsky");

    user3 = new User(2002L);
    user3.setGuid("2aebcddc-8190-4b53-9d4c-ced7cb1aeee3");
    user3.setName("Imre Anderovsky");
  }

  @Test
  void testEquals() {
    assertThat(user1.equals(user2)).isTrue();

    assertThat(user3.equals(user1)).isFalse();
    assertThat(user3.equals(user2)).isFalse();
  }

  @Test
  void testHashCode() {
    assertThat(user1.hashCode()).hasSameHashCodeAs(user2.hashCode());

    assertThat(user3.hashCode()).isNotEqualTo(user1.hashCode());
    assertThat(user3.hashCode()).isNotEqualTo(user2.hashCode());
  }
}