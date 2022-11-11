package org.tomo.domain;

import java.io.Serial;
import java.io.Serializable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * User domain entity.
 */
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class User implements Serializable {
  @Serial
  private static final long serialVersionUID = 6605370583786852202L;

  /**
   * User's id.
   *
   * <p>User's id is mandatory. Once set, can't be changed.
   */
  @NonNull
  @Setter(AccessLevel.NONE)
  private long id;

  private String guid;

  private String name;

  /**
   * Create a new user which is copy of user argument.
   */
  public User(@NonNull final User user) {
    this.id = user.getId();
    this.guid = user.getGuid();
    this.name = user.getName();
  }
}