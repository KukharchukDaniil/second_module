package com.epam.esm.controllers;

import com.epam.esm.entities.Certificate;
import com.epam.esm.enums.CertificateSortingOrder;
import com.epam.esm.errors.ErrorInfo;
import com.epam.esm.exceptions.dao.DaoException;
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


    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createCertificate(
            @RequestBody Certificate certificate
    ) throws ServiceException {
        certificateService.create(certificate);
        return new ResponseEntity(CREATED, HttpStatus.CREATED);
    }

    @PatchMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateCertificate(
            @RequestBody Certificate certificate
    ) throws ServiceException, DaoException {
        certificateService.update(certificate);
        return new ResponseEntity(CREATED, HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteCertificate(
            @PathVariable long id
    ) throws ServiceException {
        certificateService.delete(id);
        return new ResponseEntity(CREATED, HttpStatus.NO_CONTENT);
    }


    @GetMapping(value = "/{id}")
    public ResponseEntity<Certificate> getById(@PathVariable("id") long id) throws ServiceException {
        return ResponseEntity.ok(certificateService.getById(id));
    }

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
