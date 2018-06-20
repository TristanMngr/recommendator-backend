package com.isep.recommendator.app.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name= "history")
public class History {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long history_id;

  @NotBlank
  private String name;

  @NotBlank
  @Column
  private String type;

  @NotBlank
  @Column(length = 1000000)
  private String json;

  @ManyToOne
  @JoinColumn(name = "histories")
  @JsonBackReference
  private User user;

  public History(String name, String type, String json, User user){
    this.name = name;
    this.type = type;
    this.json = json;
    this.user = user;
  }

  public History(){

  }

  public Long getHistory_id() {
    return history_id;
  }

  public void setHistory_id(Long history_id) {
    this.history_id = history_id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getJson() {
    return json;
  }

  public void setJson(String json) {
    this.json = json;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }
}
