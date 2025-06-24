package io.starter.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString
@Entity
@Table(name = "skills")
public class SkillEntity extends WithJoinLeague {

  private @NotNull String name;
  private @NotNull String variant;
  private @NotNull Boolean corrupted;
  private @NotNull Integer gemLevel;
  private @NotNull Integer gemQuality;
  private @NotNull Double chaosEquivalent;
  private @NotNull Double divineEquivalent;
}
