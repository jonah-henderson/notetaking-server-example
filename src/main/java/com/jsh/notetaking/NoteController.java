package com.jsh.notetaking;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/notes")
public class NoteController {

  @Autowired
  private NoteRepository noteRepository;

  /**
   * For this endpoint, I wanted to have the option of finding all notes
   * as well as finding all notes for a particular tag. I decided to implement
   * it with a RequestParam. That means this route can handle either of the
   * following:
   * 
   * GET /notes -> return all notes
   * GET /notes?tag=someTag =? return all notes associated with the tag "someTag"
   */
  @GetMapping(path = "")
  @ResponseBody
  Iterable<Note> getNotes(@RequestParam Optional<String> tag) {
    if (tag.isPresent()) {
      return noteRepository.findByTagsName(tag.get());
    }
    // No else needed here, as return is used inside the if
    return noteRepository.findAll();
  }
}
