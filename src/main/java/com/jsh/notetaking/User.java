package com.jsh.notetaking;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class User {
  @Id
  @GeneratedValue()
  private Integer id;

  private String name;
  private String email;

  /**
   * A User can own many Notes, but a Note may only ever have one author (User),
   * so
   * we represent this relation with a one-to-many mapping
   * - mappedBy: tells Hibernate that this Entity owns the relationship, and the
   * related field on the other Entity is
   * called "author"
   * 
   * - fetch: tells Hibernate not to automatically load all the associated Notes.
   * If we need them, we will request them
   * ourselves with additional code
   * 
   * - cascade: tells Hibernate that any changes made to a User should also affect
   * its Notes. For example, this will
   * delete a user's associated Notes if a User is deleted
   * 
   * - orphanRemoval: tells Hibernate that a Note without an author is not
   * allowed--it's an "orphan" and should be
   * removed from the database
   */
  @OneToMany(mappedBy = "author", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  @JsonIgnore
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

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  // Just a regular getter for notes
  public Set<Note> getNotes() {
    return notes;
  }

  // Just a regular setter for notes
  public void setNotes(Set<Note> notes) {
    this.notes = notes;
  }

  // A helper function that will make it easier for us to keep Users and Notes in
  // sync
  // If we add a Note to a User, this function will automatically set the Note's
  // author
  // to be that User
  public void addNote(Note note) {

    note.setAuthor(this);

    this.notes.add(note);
  }

  // Similarly, if we remove a Note from a User, we would like that Note to
  // automatically have its
  // author removed as well.
  public void removeNote(Note note) {
    note.setAuthor(null);
    this.notes.remove(note);
  }
}
