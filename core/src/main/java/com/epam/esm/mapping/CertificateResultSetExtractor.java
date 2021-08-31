package com.epam.esm.mapping;

import com.epam.esm.entities.Certificate;
import com.epam.esm.entities.Tag;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Extracts Certificate objects from ResultSet. Fills the tagList field.
 */
public class CertificateResultSetExtractor implements ResultSetExtractor<List<Certificate>> {

    public static final String ID = "id";
    public static final String TAG_ID = "tag_id";
    public static final String TAG_NAME = "tag_name";

    private CertificateRowMapper certificateRowMapper;
    private TagRowMapper tagRowMapper;

    public CertificateResultSetExtractor() {
        certificateRowMapper = new CertificateRowMapper();
        tagRowMapper = new TagRowMapper(TAG_ID, TAG_NAME);
    }

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
        Long bufferId = null;
        List<Tag> tagList = null;

        do {
            long certificateId = resultSet.getLong(ID);
            if (bufferId != null && certificateId == bufferId) {
                addTag(resultSet, tagList, tagRowMapper);
            } else {
                if (certificate != null) {
                    certificateList.add(certificate);
                }
                certificate = certificateRowMapper.mapRow(resultSet, 0);
                tagList = new ArrayList<>();
                certificate.setTagList(tagList);
                addTag(resultSet, tagList, tagRowMapper);
                bufferId = certificate.getId();
            }
        } while (resultSet.next());
        certificateList.add(certificate);
    }

    private void addTag(ResultSet resultSet, List<Tag> tagList, TagRowMapper tagRowMapper) throws SQLException {
        Tag tag = tagRowMapper.mapRow(resultSet, 0);
        if (tag != null && tag.getName() != null) {
            tagList.add(tagRowMapper.mapRow(resultSet, 0));
        }
    }
}
