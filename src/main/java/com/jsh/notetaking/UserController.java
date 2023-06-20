package com.jsh.notetaking;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping("/users")
public class UserController {

  /**
   * With how I have chosen to design this API, the controller needs both a
   * UserRepository and a NoteRepository to function, so I include them both
   * here as Autowired members
   */
  @Autowired
  private UserRepository userRepository;

  @Autowired
  private NoteRepository noteRepository;

  @GetMapping(path = "/")
  @ResponseBody
  Iterable<User> getUsers() {
    return userRepository.findAll();
  }

  @PostMapping(path = "/")
  @ResponseBody
  User addUser(@RequestBody User newUser) {
    return userRepository.save(newUser);
  }

  /**
   * I decided to design my API around the idea that a user "owns" a collection of
   * Notes, so they are actually accessed through the user's specific path at
   * /users/{userId}/notes
   * 
   * This is not the only way to approach this problem, but it is convenient
   * because it makes sure that we always have the user ID handy when we need to
   * add a new note.
   * 
   * One alternate approach might be to have this path in the NoteController, but
   * then we'd have make sure we include the user ID in the request params or
   * something similar
   */
  @PostMapping(path = "{userId}/notes")
  @ResponseBody
  Note addNote(@PathVariable Integer userId, @RequestBody Note newNote) {

    // First try to find the user
    Optional<User> user = userRepository.findById(userId);
    if (!user.isPresent()) {
      // If not, we'll send back a 404 not found error
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User " + userId + " not found");
    }
    // Otherwise, we can use our helper function to add the new note and have it
    // automatically get the correct author
    user.get().addNote(newNote);

    // Because we set the cascade option, we only need to save the user and the note
    // will automatically be persisted as well.
    userRepository.save(user.get());
    return newNote;
  }

  @GetMapping(path = "{userId}/notes")
  @ResponseBody
  Iterable<Note> getUserNotes(@PathVariable Integer userId) {
    // Here I use the additional method I defined on the NoteRepository to find all
    // the notes for a given author ID, instead of the default find by Note ID.
    return noteRepository.findByAuthorId(userId);
  }
}
