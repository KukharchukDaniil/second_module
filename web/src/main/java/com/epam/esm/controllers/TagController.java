package com.epam.esm.controllers;

import com.epam.esm.entities.Certificate;
import com.epam.esm.entities.Tag;
import com.epam.esm.errors.ErrorInfo;
import com.epam.esm.exceptions.dao.MultipleRecordsWereFoundException;
import com.epam.esm.exceptions.service.ServiceException;
import com.epam.esm.exceptions.service.TagAlreadyExistsException;
import com.epam.esm.exceptions.service.TagNotFoundException;
import com.epam.esm.services.TagService;
import com.epam.esm.validation.ValidationErrorMessage;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Provides REST API functionality for {@link Tag}
 */
@RestController
@RequestMapping(value = "/tags", produces = {MediaType.APPLICATION_JSON_VALUE})
public class TagController {

    private static final String TAG_NOT_FOUND_ERROR_CODE = "40401";
    private static final String MULTIPLE_RECORDS_WHERE_FOUND_ERROR_CODE = "50001";
    private static final String CREATED = "Created";
    private static final String ERROR_MESSAGE = "Invalid tag id {id = %s}";
    private static final String ERROR_DETAILS = "Id should be positive long number";
    private final TagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
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
        if (!NumberUtils.isCreatable(id) || id.startsWith("-")) {
            return ResponseEntity.badRequest().body(new ValidationErrorMessage(String.format(ERROR_MESSAGE, id),
                    ERROR_DETAILS));
        }
        return ResponseEntity.ok(tagService.getById(Long.parseLong(id)));
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
        if (!NumberUtils.isCreatable(id) || id.startsWith("-")) {
            return ResponseEntity.badRequest().body(new ValidationErrorMessage(String.format(ERROR_MESSAGE, id),
                    ERROR_DETAILS));
        }
        tagService.deleteById(Long.parseLong(id));
        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * Handles exception and returns ErrorInfo object
     *
     * @param exception {@link TagNotFoundException exception} to handle
     * @return ErrorInfo object containing exception info with errorCode = 40001. Response status: 404 Not Found.
     */
    @ExceptionHandler(TagNotFoundException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    private ErrorInfo handleTagNotFoundException(TagNotFoundException exception) {
        return new ErrorInfo(
                HttpStatus.NOT_FOUND,
                TAG_NOT_FOUND_ERROR_CODE,
                exception.getLocalizedMessage()
        );
    }

    /**
     * Handles exception and returns ErrorInfo object
     *
     * @param exception {@link TagAlreadyExistsException exception} to handle
     * @return ErrorInfo object containing exception info with errorCode = 50001. Response status: 500 Internal Server Error.
     */
    @ExceptionHandler(TagAlreadyExistsException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    private ErrorInfo handleTagAlreadyExistsException(TagAlreadyExistsException exception) {
        return new ErrorInfo(
                HttpStatus.CONFLICT,
                MULTIPLE_RECORDS_WHERE_FOUND_ERROR_CODE,
                exception.getLocalizedMessage()
        );
    }

    /**
     * Handles exception and returns ErrorInfo object
     *
     * @param exception {@link MultipleRecordsWereFoundException exception} to handle
     * @return ErrorInfo object containing exception info with errorCode = 50001. Response status: 500 Internal Server Error.
     */
    @ExceptionHandler({MultipleRecordsWereFoundException.class})
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    private ErrorInfo handleMultipleRecordsWereFoundException(MultipleRecordsWereFoundException exception) {
        return new ErrorInfo(
                HttpStatus.INTERNAL_SERVER_ERROR,
                MULTIPLE_RECORDS_WHERE_FOUND_ERROR_CODE,
                exception.getLocalizedMessage()
        );
    }
}
