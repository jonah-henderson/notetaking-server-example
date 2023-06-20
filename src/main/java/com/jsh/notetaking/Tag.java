package com.jsh.notetaking;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;

@Entity
public class Tag {
  @Id
  @GeneratedValue
  private Integer id;
  private String name;

  /**
   * Because I didn't set @JsonIgnore on the 'tags' field of Notes,
   * it is extremely important that this side of the relationship
   * *does* have JsonIgnore. Otherwise, when sending a Note back
   * to a client, it would try to include the Tags, which would try
   * to include the Notes again, which would then try to include Tags
   * again, and so on until we hit a stack overflow and caused a crash
   */
  @JsonIgnore
  @ManyToMany(mappedBy = "tags")
  private Set<Note> notes;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Set<Note> getNotes() {
    return notes;
  }

  public void setNotes(Set<Note> notes) {
    this.notes = notes;
  }

  /**
   * TODO: add equals() and hashCode() overrides for Tag
   */

}
