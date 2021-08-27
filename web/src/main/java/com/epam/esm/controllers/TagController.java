package com.epam.esm.controllers;

import com.epam.esm.entities.Tag;
import com.epam.esm.exceptions.service.ServiceException;
import com.epam.esm.exceptions.service.TagNotFoundException;
import com.epam.esm.services.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/tags", produces = {MediaType.APPLICATION_JSON_VALUE})
public class TagController {

    private final TagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping(value = "/")
    public @ResponseBody
    ResponseEntity<List<Tag>> getAllTags() {
        return new ResponseEntity<>(tagService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<Tag> getByTagName(@RequestAttribute(value = "name", required = true) String name)
            throws ServiceException {
        Optional<Tag> tagOptional = tagService.getByName(name);
        if (!tagOptional.isPresent()) {
            throw new TagNotFoundException();
        }
        return new ResponseEntity<>(tagOptional.get(), HttpStatus.OK); //return valid status
    }

    @GetMapping("/tags/{id}")
    public ResponseEntity<Tag> getById(@PathVariable long id) throws ServiceException {
        Optional<Tag> tagOptional = tagService.getById(id);
        if (!tagOptional.isPresent()) {
            throw new TagNotFoundException();
        }
        return new ResponseEntity<>(tagOptional.get(), HttpStatus.OK);
    }

    @PostMapping("/tags/")
    public ResponseEntity createTag(@RequestBody Tag tag) throws ServiceException {
        tagService.create(tag);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PatchMapping("/tags")
    public ResponseEntity updateTag(
            @RequestBody Tag tag) throws ServiceException {
        tagService.update(tag);
        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping("/tags/{id}")
    public ResponseEntity deleteTagById(@PathVariable long id) throws ServiceException {
        tagService.deleteById(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}
