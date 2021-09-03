package com.epam.esm.controllers;

import com.epam.esm.entities.Certificate;
import com.epam.esm.entities.Tag;
import com.epam.esm.enums.CertificateSortingOrder;
import com.epam.esm.errors.ErrorInfo;
import com.epam.esm.exceptions.dao.MultipleRecordsWereFoundException;
import com.epam.esm.exceptions.service.ServiceException;
import com.epam.esm.exceptions.validation.ValidationErrorMessage;
import com.epam.esm.services.CertificateService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Locale;

/**
 * Provides REST API functionality for {@link Certificate}
 */
@RestController
@RequestMapping(value = "/certificates", produces = MediaType.APPLICATION_JSON_VALUE)
public class CertificateController {

    private static final String TAG_AND_NAME_ERROR_MESSAGE = "Can't process both tag and name params";
    private static final String PARAMETERS_ERROR_CODE = "parameters-02";
    private static final String ERROR_DETAILS = "Id should be positive number";
    private static final String ID_ERROR_MESSAGE = "Invalid tag id {id = %s}";
    public static final String NONE = "NONE";
    public static final String ASC = "ASC";
    public static final String DESC = "DESC";
    private static final String INVALID_SORTING_ORDER = "Invalid sorting order {sort = %s}";

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
     * Response status: 200 OK</ul>
     *     <ul>ResponseEntity containing {@link List<Certificate>} filtered by tag, when tagName provided; or {@code null}
     *     if no Certificates were found.
     *     Response status: 200 OK</ul>
     *     <ul>ResponseEntity containing {@link List<Certificate>} which contains all certificates in data source;
     *     or {@code null} if no Certificates were found.
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
            return new ResponseEntity(
                    new ErrorInfo(HttpStatus.BAD_REQUEST, PARAMETERS_ERROR_CODE, TAG_AND_NAME_ERROR_MESSAGE),
                    HttpStatus.BAD_REQUEST);
        }
        if (!isSortingOrderStringValid(sortingOrderString)) {
            return new ResponseEntity(
                    new ErrorInfo(HttpStatus.BAD_REQUEST, PARAMETERS_ERROR_CODE,
                            String.format(INVALID_SORTING_ORDER, sortingOrderString)),
                    HttpStatus.BAD_REQUEST);
        }

        List<Certificate> resultList;
        CertificateSortingOrder certificateSortingOrder = getCertificateSortingOrder(sortingOrderString);
        if (StringUtils.isEmpty(namePart)) {
            resultList = certificateService.getAll(certificateSortingOrder);
        } else {
            resultList = certificateService.getByNamePartSorted(certificateSortingOrder, namePart);
        }
        return ResponseEntity.ok(resultList);
    }

    private CertificateSortingOrder getCertificateSortingOrder(String sortingOrderString) {
        return sortingOrderString != null ?
                CertificateSortingOrder.valueOf(sortingOrderString.toUpperCase(Locale.ROOT))
                : CertificateSortingOrder.NONE;
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
        return new ResponseEntity(HttpStatus.CREATED);
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
        return new ResponseEntity(HttpStatus.NO_CONTENT);
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
            @PathVariable String id
    ) throws ServiceException {
        if (!isIdValid(id)) {
            return ResponseEntity.badRequest().body(new ValidationErrorMessage(String.format(ID_ERROR_MESSAGE, id),
                    ERROR_DETAILS));
        }
        certificateService.delete(Long.parseLong(id.trim()));
        return new ResponseEntity(HttpStatus.NO_CONTENT);
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
    public ResponseEntity getById(@PathVariable("id") String id) {

        if (!isIdValid(id)) {
            return ResponseEntity.badRequest().body(new ValidationErrorMessage(String.format(ID_ERROR_MESSAGE, id),
                    ERROR_DETAILS));
        }
        return ResponseEntity.ok(certificateService.getById(Long.parseLong(id.trim())));
    }

    /**
     * Handles exception and returns ErrorInfo object
     *
     * @param exception {@link MultipleRecordsWereFoundException exception} to handle
     * @return ErrorInfo object containing exception info with errorCode = 50002. Response status: 500 Internal Server Error.
     */

    private boolean isIdValid(String id) {
        return (id != null && NumberUtils.isParsable(id.trim()) && Long.parseLong(id.trim()) >= 0);
    }

    private boolean isSortingOrderStringValid(String sortingOrderString) {
        switch (sortingOrderString.toUpperCase()) {
            case NONE:
            case ASC:
            case DESC:
                return true;
            default:
                return false;
        }
    }

}
