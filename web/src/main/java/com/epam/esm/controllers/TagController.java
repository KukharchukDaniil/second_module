package com.epam.esm.controllers;

import com.epam.esm.entities.Certificate;
import com.epam.esm.entities.Tag;
import com.epam.esm.exceptions.service.ServiceException;
import com.epam.esm.exceptions.service.TagNotFoundException;
import com.epam.esm.exceptions.validation.ValidationErrorMessage;
import com.epam.esm.services.TagService;
import com.epam.esm.validation.TagValidator;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Provides REST API functionality for {@link Tag}
 */
@RestController
@RequestMapping(value = "/tags", produces = {MediaType.APPLICATION_JSON_VALUE})
public class TagController {


    private static final String CREATED = "Created";
    private static final String ERROR_MESSAGE = "Invalid tag id {id = %s}";
    private static final String ERROR_DETAILS = "Id should be positive number";
    private final TagService tagService;
    private final TagValidator tagValidator;

    @Autowired
    public TagController(TagService tagService, TagValidator tagValidator) {
        this.tagService = tagService;
        this.tagValidator = tagValidator;
    }

    /**
     * Method provide functionality for getting {@link Tag} collection.
     * <p> GET /web/tags
     *
     * @return <ul>ResponseEntity containing {@link List<Certificate>} which contains all certificates in data source.
     * Response status: 200 OK</ul>
     */
    @GetMapping
    public ResponseEntity getAllTags(@RequestAttribute(name = "name", required = false) String name) {
        ResponseEntity responseEntity;
        if (StringUtils.isEmpty(name)) {
            responseEntity = ResponseEntity.ok(tagService.getAll());
        } else {
            responseEntity = ResponseEntity.ok(tagService.getByName(name));
        }
        return responseEntity;
    }

    /**
     * Returns single {@link Tag} object with corresponding id
     * <p> GET /web/tags/{id}
     *
     * @param id identifier of {@link Tag} to be returned
     * @return <ul>ResponseEntity containing single {@link Tag}.
     * Response status: 200 OK</ul>
     *     <ul>ResponseEntity containing ErrorInfo(when no Tag was found). Response status: 404 Not Found</ul>
     */
    @GetMapping("/{id}")
    public ResponseEntity getById(@PathVariable String id) throws ServiceException {
        if (!isIdValid(id)) {
            return ResponseEntity.badRequest().body(new ValidationErrorMessage(String.format(ERROR_MESSAGE, id),
                    ERROR_DETAILS));
        }
        return ResponseEntity.ok(tagService.getById(Long.parseLong(id.trim())));
    }

    /**
     * Creates new record in data source with corresponding info
     * <p> POST /web/tags
     * <p> Content type: application/json
     *
     * @param tag {@link Tag} object to be added
     * @return <ul>ResponseEntity. Response status: 201 Created</ul>
     * <ul>ResponseEntity containing ErrorInfo, if Tag with provided name already exists.
     * <p>Response status: 500 Internal Server Error</ul>
     */
    @PostMapping
    public ResponseEntity createTag(@RequestBody Tag tag) throws ServiceException {
        tagService.create(tag);
        return new ResponseEntity(CREATED, HttpStatus.NO_CONTENT);
    }

    /**
     * Deletes existing record in data source
     * <p> DELETE /web/tags/{id}
     *
     * @param id id of {@link Tag} object to be deleted
     * @return <li>ResponseEntity. Response status: 204 No content </li>
     * <li>ResponseEntity. Response status: 404 Not found. When no tag with this id was found</li>
     */
    @DeleteMapping("/{id}")
    public ResponseEntity deleteTagById(@PathVariable String id) throws ServiceException {
        if (!isIdValid(id)) {
            return ResponseEntity.badRequest().body(new ValidationErrorMessage(String.format(ERROR_MESSAGE, id),
                    ERROR_DETAILS));
        }
        tagService.deleteById(Long.parseLong(id.trim()));
        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * Handles exception and returns ErrorInfo object
     *
     * @param exception {@link TagNotFoundException exception} to handle
     * @return ErrorInfo object containing exception info with errorCode = 40001. Response status: 404 Not Found.
     */


    private boolean isIdValid(String id) {

        return (id != null && NumberUtils.isParsable(id.trim()) && Long.parseLong(id.trim()) >= 0);
    }


}
