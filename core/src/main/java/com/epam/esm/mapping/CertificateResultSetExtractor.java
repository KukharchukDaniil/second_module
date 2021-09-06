package com.epam.esm.mapping;

import com.epam.esm.entities.Certificate;
import com.epam.esm.entities.Tag;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Extracts Certificate objects from ResultSet. Fills the tagList field.
 */
@Component
public class CertificateResultSetExtractor implements ResultSetExtractor<List<Certificate>> {

    private static final String ID = "id";
    private static final String TAG_ID = "tag_id";
    private static final String TAG_NAME = "tag_name";

    @Resource
    private CertificateRowMapper certificateRowMapper;

    @Resource(name = "extractorTagRowMapper")
    private TagRowMapper tagRowMapper;

    @Override
    public List<Certificate> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
        List<Certificate> certificateList = new ArrayList<>();

        if (resultSet.next()) {
            processResultSet(resultSet, certificateList);
        }
        return certificateList;
    }

    private void processResultSet(ResultSet resultSet, List<Certificate> certificateList) throws SQLException {
        Certificate certificate = null;
        do {
            certificate = getCertificate(resultSet, certificateList, certificate);
        } while (resultSet.next());
        certificateList.add(certificate);
    }

    private Certificate getCertificate(ResultSet resultSet, List<Certificate> certificateList, Certificate certificate) throws SQLException {
        Long currentId = resultSet.getLong(ID);
        if (certificate != null && currentId == certificate.getId()) {
            processTagInCurrentRow(resultSet, certificate, tagRowMapper);
        } else {
            finishCurrentCertificateMapping(certificateList, certificate);
            certificate = certificateRowMapper.mapRow(resultSet, 0);
            setNewCertificateTagList(resultSet, certificate);
        }
        return certificate;
    }

    private List<Tag> setNewCertificateTagList(ResultSet resultSet, Certificate certificate) throws SQLException {
        List<Tag> tagList;
        tagList = new ArrayList<>();
        certificate.setTagList(tagList);
        processTagInCurrentRow(resultSet, certificate, tagRowMapper);
        return tagList;
    }

    private void finishCurrentCertificateMapping(List<Certificate> certificateList, Certificate certificate) {
        if (certificate != null) {
            certificateList.add(certificate);
        }
    }

    private void processTagInCurrentRow(ResultSet resultSet, Certificate certificate, TagRowMapper tagRowMapper) throws SQLException {
        Tag tag = tagRowMapper.mapRow(resultSet, 0);
        List<Tag> tagList = certificate.getTagList();
        if (tag != null && tag.getName() != null) {
            tagList.add(tagRowMapper.mapRow(resultSet, 0));
        }
    }
}
