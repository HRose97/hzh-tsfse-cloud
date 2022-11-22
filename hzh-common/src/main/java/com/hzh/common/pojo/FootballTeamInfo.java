package com.hzh.common.pojo;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class FootballTeamInfo {

  private long teamId;
  private String teamName;
  private String teamType;
  private String teamCountry;
  private String teamDate;
  private String teamLogo;
  private String teamLocal;
  private String teamHome;
  private String teamLeague;
  private String teamOverallChampion;
  private String teamPartitionChampion;
  private String teamPlayoffs;
  private String teamAdministrator;
  private String teamCoach;

}
