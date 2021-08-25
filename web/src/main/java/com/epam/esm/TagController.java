package com.epam.esm;

import com.epam.esm.entities.Tag;
import com.epam.esm.exceptions.service.ServiceException;
import com.epam.esm.exceptions.service.TagNotFoundException;
import com.epam.esm.services.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class TagController {

    private final TagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping("/tags")
    public ResponseEntity<List<Tag>> getAllTags() {
        return new ResponseEntity<>(tagService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/tags/{name}")
    public ResponseEntity<Tag> getByTagName(@PathVariable String name) throws ServiceException {
        Optional<Tag> tagOptional = tagService.getByName(name);
        if (!tagOptional.isPresent()) {
            throw new TagNotFoundException();
        }
        return new ResponseEntity<>(tagOptional.get(), HttpStatus.OK);
    }

    @GetMapping("/tags/{id}")
    public ResponseEntity<Tag> getById(@PathVariable long id) throws ServiceException {
        Optional<Tag> tagOptional = tagService.getById(id);
        if (!tagOptional.isPresent()) {
            throw new TagNotFoundException();
        }
        return new ResponseEntity<>(tagOptional.get(),HttpStatus.OK);
    }

    @PostMapping("/tags/{name}")
    public ResponseEntity createTag(@PathVariable String name) throws ServiceException {
        Tag tag = new Tag(name);
        tagService.create(tag);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PatchMapping("/tags")
    public ResponseEntity updateTag(
            @RequestParam(name = "name", required = true) String name,
            @RequestParam(name = "id", required = true) long id) throws ServiceException {
        Tag tag = new Tag();
        tag.setName(name);
        tag.setId(id);
        tagService.update(tag);
        return new ResponseEntity(HttpStatus.OK);
    }
    @DeleteMapping("/tags/{id}")
    public ResponseEntity deleteTagById(@PathVariable long id) throws ServiceException {
        tagService.deleteById(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}
