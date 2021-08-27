package com.epam.esm.controllers;

import com.epam.esm.entities.Certificate;
import com.epam.esm.enums.SortingOrder;
import com.epam.esm.exceptions.service.ServiceException;
import com.epam.esm.services.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CertificateController {

    private final CertificateService certificateService;

    @Autowired
    public CertificateController(CertificateService certificateService) {
        this.certificateService = certificateService;
    }

    @GetMapping("/certificates")
    public ResponseEntity<List<Certificate>> getAll(@RequestAttribute(name = "sort", required = true)
                                                            String sortingOrderString) throws ServiceException {
        SortingOrder sortingOrder = SortingOrder.valueOf(sortingOrderString);
        return ResponseEntity.ok(certificateService.getAll(sortingOrder));
    }

    @GetMapping("/certificates")
    public ResponseEntity<List<Certificate>> getAllByTagName(@RequestAttribute(name = "tagName", required = true)
                                                                     String tagName) {
        return ResponseEntity.ok(certificateService.getByTagName(tagName));
    }

    @GetMapping("/certificates")
    public ResponseEntity<List<Certificate>> getByNamePart(@RequestAttribute(name = "namePart", required = true)
                                                                   String namePart) {
        return ResponseEntity.ok(certificateService.getByNamePart(namePart));
    }

    @PostMapping("/certificates/")
    public ResponseEntity createCertificate(
            @RequestBody Certificate certificate, @RequestBody String[] tags
    ) throws ServiceException {
        certificateService.create(certificate, tags);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PatchMapping("/certificates")
    public ResponseEntity updateCertificate(
            @RequestBody Certificate certificate, @RequestBody String[] tags
    ) throws ServiceException {
        certificateService.update(certificate, tags);
        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping("/certificates/{id}")
    public ResponseEntity deleteCertificate(
            @PathVariable long id
    ) throws ServiceException {
        certificateService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }


    @GetMapping("/certificates/{id}")
    public ResponseEntity<Certificate> getById(@PathVariable long id) throws ServiceException {
        return ResponseEntity.ok(certificateService.getById(id));
    }

}
