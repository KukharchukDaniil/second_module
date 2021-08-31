package com.epam.esm.controllers;

import com.epam.esm.entities.Tag;
import com.epam.esm.errors.ErrorInfo;
import com.epam.esm.exceptions.dao.MultipleRecordsWereFoundException;
import com.epam.esm.exceptions.service.ServiceException;
import com.epam.esm.exceptions.service.TagAlreadyExistsException;
import com.epam.esm.exceptions.service.TagNotFoundException;
import com.epam.esm.services.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/tags", produces = {MediaType.APPLICATION_JSON_VALUE})
public class TagController {

    public static final String ERROR_MESSAGE = "errorMessage";
    public static final String ERROR_CODE = "errorCode";
    public static final int TAG_NOT_FOUND_ERROR_CODE = 40401;
    public static final int TAG_ALREADY_EXISTS_ERROR_CODE = 42201;
    public static final int MULTIPLE_RECORDS_WHERE_FOUND_ERROR_CODE = 50001;
    private static final String CREATED = "Created";
    private final TagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping
    public ResponseEntity getAllTags(@RequestAttribute(name = "name", required = false) String name) {
        ResponseEntity responseEntity;
        if (name == null || name.isEmpty()) {
            responseEntity = ResponseEntity.ok(tagService.getAll());
        } else {
            responseEntity = ResponseEntity.ok(tagService.getByName(name));
        }
        return responseEntity;
    }


    @GetMapping("/{id}")
    public ResponseEntity<Tag> getById(@PathVariable long id) throws ServiceException {
        return ResponseEntity.ok(tagService.getById(id));
    }

    @PostMapping
    public ResponseEntity createTag(@RequestBody Tag tag) throws ServiceException {
        tagService.create(tag);
        return new ResponseEntity(CREATED, HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteTagById(@PathVariable long id) throws ServiceException {
        tagService.deleteById(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @ExceptionHandler(TagNotFoundException.class)
    @ResponseBody
    private ErrorInfo handleTagNotFoundException(TagNotFoundException exception) {
        return new ErrorInfo(
                HttpStatus.NOT_FOUND,
                TAG_NOT_FOUND_ERROR_CODE,
                exception.getLocalizedMessage()
        );
    }

    @ExceptionHandler(TagAlreadyExistsException.class)
    @ResponseBody
    private ErrorInfo handleTagAlreadyExistsException(TagAlreadyExistsException exception) {
        return new ErrorInfo(
                HttpStatus.UNPROCESSABLE_ENTITY,
                MULTIPLE_RECORDS_WHERE_FOUND_ERROR_CODE,
                exception.getLocalizedMessage()
        );
    }

    @ExceptionHandler({MultipleRecordsWereFoundException.class})
    @ResponseBody
    private ErrorInfo handleMultipleRecordsWereFoundException(MultipleRecordsWereFoundException exception) {
        return new ErrorInfo(
                HttpStatus.INTERNAL_SERVER_ERROR,
                MULTIPLE_RECORDS_WHERE_FOUND_ERROR_CODE,
                exception.getLocalizedMessage()
        );
    }
}
