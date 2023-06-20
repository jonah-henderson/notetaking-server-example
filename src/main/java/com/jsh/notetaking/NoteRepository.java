package com.jsh.notetaking;

import org.springframework.data.repository.CrudRepository;

/**
 * In this interface, I add some additional find operations beyond the
 * default findById(). Hibernate automatically generates the definitions
 * as long as the methods are named correctly. If they are not, it will
 * generate an error during build time
 */
public interface NoteRepository extends CrudRepository<Note, Integer> {

  // Because a Note has an author, which is of type User, I can find a note
  // by a field that exists on the User entity. In this case, I simply use
  // the User ID. As long as I use "author" correctly so that it matches
  // the field in the Note entity, I can then refer to any of the fields on
  // User
  //
  // For example, although it might not be very useful, I could define a
  // new method, findByAuthorEmail(String authorEmail) to be able to search
  // for Notes that have an author with a specific email
  Iterable<Note> findByAuthorId(Integer authorId);

  // Notice how the name of the function has to *exactly* match the field in Note
  // If I wrote 'findByTagId' instead, Hibernate would fail to generate the code
  // and it would result in an error
  Iterable<Note> findByTagsId(Integer tagId);

  Iterable<Note> findByTagsName(String tagName);
}
