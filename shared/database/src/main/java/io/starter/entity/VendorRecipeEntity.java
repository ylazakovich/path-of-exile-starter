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
@Table(name = "vendor_recipes")
public class VendorRecipeEntity extends WithJoinLeague {

  private @NotNull String name;
  private @NotNull Double chaosEquivalentPrice;
  private @NotNull Double chaosEquivalentProfit;
}
