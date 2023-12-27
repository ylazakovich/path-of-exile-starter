package io.automation.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "gems")
public class GemEntity implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(nullable = false, updatable = false)
  private Long id;
  @NonNull
  private String name;
  @NonNull
  private String variant;
  @NonNull
  private boolean corrupted;
  @NonNull
  private int gemLevel;
  @NonNull
  private int gemQuality;
  @NonNull
  private double chaosValue;
}
