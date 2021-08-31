package com.epam.esm.controllers;

import com.epam.esm.entities.Certificate;
import com.epam.esm.entities.Tag;
import com.epam.esm.enums.CertificateSortingOrder;
import com.epam.esm.errors.ErrorInfo;
import com.epam.esm.exceptions.dao.MultipleRecordsWereFoundException;
import com.epam.esm.exceptions.service.CertificateNotFoundException;
import com.epam.esm.exceptions.service.ServiceException;
import com.epam.esm.services.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Locale;

/**
 * Provides REST API functionality for {@link Certificate}
 */
@RestController
@RequestMapping(value = "/certificates", produces = MediaType.APPLICATION_JSON_VALUE)
public class CertificateController {

    private static final int MULTIPLE_RECORDS_WHERE_FOUND_ERROR_CODE = 50002;
    private static final int CERTIFICATE_NOT_FOUND_ERROR_CODE = 40402;
    public static final String CREATED = "Created";
    public static final String ERROR_MESSAGE = "can't process both tag and name params";
    public static final int ERROR_CODE = 400;
    private final CertificateService certificateService;

    @Autowired
    public CertificateController(CertificateService certificateService) {
        this.certificateService = certificateService;
    }

    /**
     * Method provide functionality for getting {@link Certificate} collection according to parameters.
     * It's forbidden to use namePart and tagName at the same time.
     * <p> GET /web/certificates
     *
     * @param sortingOrderString enum value which defines order of data sorting
     * @param namePart           part of Certificate name, needed for data filtering. Method returns
     *                           Certificate containing namePart in the name field when this parameter provided.
     * @param tagName            {@link Tag} name, needed for data filtering. Method returns all
     *                           *                 {@link Certificate} attached to {@link Tag} with corresponding name when this parameter provided.
     * @return <ul>ResponseEntity containing {@link List<Certificate>} filtered by name part, when namePart provided.
     *     Response status: 200 OK</ul>
     *     <ul>ResponseEntity containing {@link List<Certificate>} filtered by tag, when tagName provided.
     *     Response status: 200 OK</ul>
     *     <ul>ResponseEntity containing {@link List<Certificate>} which contains all certificates in data source.
     *     Response status: 200 OK</ul>
     *     <ul>ResponseEntity containing ErrorInfo,namePart and tagName provided at the same time .
     *     Response status: 400 Bad Request</ul>
     */
    @GetMapping
    public ResponseEntity getAll(
            @RequestParam(value = "sort", required = false) String sortingOrderString,
            @RequestParam(value = "name", required = false) String namePart,
            @RequestParam(value = "tag", required = false) String tagName) {
        if (namePart != null && tagName != null) {
            return new ResponseEntity(new ErrorInfo(HttpStatus.BAD_REQUEST, ERROR_CODE, ERROR_MESSAGE), HttpStatus.BAD_REQUEST);
        }
        List<Certificate> resultList;
        CertificateSortingOrder certificateSortingOrder = sortingOrderString != null ?
                CertificateSortingOrder.valueOf(sortingOrderString.toUpperCase(Locale.ROOT))
                : CertificateSortingOrder.NONE;
        if (namePart != null && !namePart.isEmpty()) {
            resultList = certificateService.getByNamePartSorted(certificateSortingOrder, namePart);
            ;
        } else {
            resultList = certificateService.getAll(certificateSortingOrder);
        }
        return ResponseEntity.ok(resultList);
    }


    /**
     * Creates new record in data source with corresponding info
     * <p> POST /web/certificates
     * <p> Content type: application/json
     *
     * @param certificate {@link Certificate} object to be added
     * @return ResponseEntity. Response status: 201 Created
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createCertificate(
            @RequestBody Certificate certificate
    ) {
        certificateService.create(certificate);
        return new ResponseEntity(CREATED, HttpStatus.CREATED);
    }


    /**
     * Updates existing record in data source with corresponding info
     * <p> PATCH /web/certificates
     * <p> Content type: application/json
     *
     * @param certificate {@link Certificate} object to be updated
     * @return <li>ResponseEntity. Response status: 204 No Content </li>
     * <li>ResponseEntity. Response status: 404 Not Found. When no certificate with this id were found</li>
     */
    @PatchMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateCertificate(
            @RequestBody Certificate certificate
    ) {
        certificateService.update(certificate);
        return new ResponseEntity(CREATED, HttpStatus.NO_CONTENT);
    }

    /**
     * Deletes existing record in data source
     * <p> DELETE /web/certificates/{id}
     *
     * @param id id of {@link Certificate} object to be deleted
     * @return <li>ResponseEntity. Response status: 204 No Content </li>
     * <li>ResponseEntity. Response status: 404 Not Found. When no certificate with this id was found</li>
     */
    @DeleteMapping("/{id}")
    public ResponseEntity deleteCertificate(
            @PathVariable long id
    ) throws ServiceException {
        certificateService.delete(id);
        return new ResponseEntity(CREATED, HttpStatus.NO_CONTENT);
    }

    /**
     * Returns  single record from data source with corresponding id
     * <p> GET /web/certificates/{id}
     *
     * @param id id of {@link Certificate} object to be returned
     * @return <li>ResponseEntity. Response status: 400 OK </li>
     * <li>ResponseEntity. Response status: 404 Not Found. When no certificate with this id were found</li>
     */
    @GetMapping(value = "/{id}")
    public ResponseEntity<Certificate> getById(@PathVariable("id") long id) throws ServiceException {
        return ResponseEntity.ok(certificateService.getById(id));
    }

    /**
     * Handles exception and returns ErrorInfo object
     *
     * @param exception {@link MultipleRecordsWereFoundException exception} to handle
     * @return ErrorInfo object containing exception info with errorCode = 50002. Response status: 500 Internal Server Error.
     */
    @ExceptionHandler(MultipleRecordsWereFoundException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    private ErrorInfo handleMultipleRecordsWereFoundException(MultipleRecordsWereFoundException exception) {
        return new ErrorInfo(
                HttpStatus.INTERNAL_SERVER_ERROR,
                MULTIPLE_RECORDS_WHERE_FOUND_ERROR_CODE,
                exception.getLocalizedMessage()
        );
    }

    /**
     * Handles exception and returns ErrorInfo object
     *
     * @param exception {@link CertificateNotFoundException exception} to handle
     * @return ErrorInfo object containing exception info with errorCode = 40002. Response status: 404 Not Found.
     */
    @ExceptionHandler(CertificateNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    private ErrorInfo handleCertificateNotFoundException(CertificateNotFoundException exception) {
        return new ErrorInfo(
                HttpStatus.NOT_FOUND,
                CERTIFICATE_NOT_FOUND_ERROR_CODE,
                exception.getLocalizedMessage()
        );
    }

}