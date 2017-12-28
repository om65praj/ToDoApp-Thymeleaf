package com.springthymeleaf.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.springthymeleaf.model.Note;
import com.springthymeleaf.model.User;
import com.springthymeleaf.service.NoteService;
import com.springthymeleaf.service.UserService;

/**
 * @author Om Prajapati
 *
 */
@Controller
@RequestMapping(value="/user")
public class NoteController {

	@Autowired
	NoteService noteService;

	@Autowired
	UserService userService;

	
	@RequestMapping(value="/addNote",method = RequestMethod.POST)
	public ModelAndView addNote(Note note,HttpServletRequest request) {
		int userId = (int) request.getAttribute("userId");
		User user = userService.getUserById(userId);
		Date date = new Date();
		note.setCreatedDate(date);
		note.setModifiedDate(date);
		note.setUser(user);
		if(!"".equalsIgnoreCase(note.getTitle())  && !"".equalsIgnoreCase(note.getDescription())) {
			noteService.createNote(note);
		}
		ModelAndView modelAndView=new ModelAndView();
		modelAndView.setViewName("home");
		modelAndView.addObject("note",note);
		List<Note> allNotes =noteService.getAllNotes(user);
		modelAndView.addObject("allNotes", allNotes);
		return modelAndView;
	}
	
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
	public ModelAndView deleteNote(@PathVariable("id") int noteId ,HttpServletRequest request) {
		System.out.println("hello");
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("home");
		int id = (int) request.getAttribute("userId");
		User user = userService.getUserById(id);
		
		Note note = new Note();
		note.setNoteId(noteId);
		note.setUser(user);
		
		boolean delete = noteService.deleteNote(note);
		
		if (delete != true) {
			modelAndView.addObject("user",user);
			modelAndView.addObject("note",note);
			List<Note> allNotes =noteService.getAllNotes(user);
			modelAndView.addObject("allNotes", allNotes);
			return modelAndView;
		} else {
		
			List<Note> allNotes =noteService.getAllNotes(user);
			modelAndView.addObject("allNotes", allNotes);
			return modelAndView;
		}
	}
	
	
	
}
