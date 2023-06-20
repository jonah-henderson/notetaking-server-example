package com.jsh.notetaking;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;

@Entity
public class Note {
  @Id
  @GeneratedValue
  private Integer id;
  private String text;

  @ManyToOne(fetch = FetchType.LAZY)
  @JsonIgnore
  private User author;

  /**
   * Because a Note may have many Tags and a Tag may have many Notes,
   * we use a many-to-many mapping here. The details of how this is handled
   * are abstracted away by Hibernate, but it may useful to know that
   * this usually creates an extra table named something like notes_tags, with two
   * columns: one for a note ID and one for a related tag ID
   * 
   * The options for this annotation are very similar to the other relationship
   * annotations:
   * - fetch: don't automatically load tags in order to improve performance
   * 
   * - cascade: in this case, we specifically do not want a Tag to be deleted if
   * the Note
   * it is associated with is deleted, so we use only a subset of the available
   * cascade types.
   */
  @ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })

  /**
   * Notice in this case I did not use @JsonIgnore: I thought it would be
   * reasonable
   * to always load the tags, since we are almost always going to want to know
   * what
   * tags a Note has when we load it.
   */
  private Set<Tag> tags;

  Note() {
  }

  Note(User author) {
    this.author = author;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public User getAuthor() {
    return author;
  }

  public void setAuthor(User author) {
    this.author = author;
  }

  public Set<Tag> getTags() {
    return tags;
  }

  public void setTags(Set<Tag> tags) {
    this.tags = tags;
  }

  /*
   * These work similarly to what we did for Users and Notes:
   * If we add a tag, that tag should also refer to this Note
   */
  public void addTag(Tag tag) {
    this.tags.add(tag);
    tag.getNotes().add(this);
  }

  /**
   * If we remove a Tag from this Note, we should also remove
   * this Note from the Tag
   */
  public void removeTag(Tag tag) {
    this.tags.remove(tag);
    tag.getNotes().remove(this);
  }

  /**
   * The following help Hibernate managed entities work with Java Sets.
   * For an application as simple as this one, we would likely not
   * observe the potential problems, but I've included them here for
   * an example of what may be necessary for a more complex application
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Note)) {
      return false;
    }
    return id != null && id.equals(((Note) o).getId());
  }

  @Override
  public int hashCode() {
    return getClass().hashCode(); // unique number for an object
  }

}
