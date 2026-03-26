package com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "environment_event")
@Getter
@NoArgsConstructor
public class EnvironmentEvent {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "environment_event_id")
  private Long environmentEventId;

}
