package com.epam.esm.mapping;

import com.epam.esm.entities.Certificate;
import com.epam.esm.entities.Tag;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CertificateResultSetExtractor implements ResultSetExtractor<List<Certificate>> {

    public static final String ID = "id";
    public static final String TAG_ID = "tag_id";
    public static final String TAG_NAME = "tag_name";

    @Override
    public List<Certificate> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
        List<Certificate> certificateList = new ArrayList<>();
        List<Tag> tagList = new ArrayList<>();

        CertificateRowMapper certificateRowMapper = new CertificateRowMapper();
        TagRowMapper tagRowMapper = new TagRowMapper(TAG_ID, TAG_NAME);
        if (resultSet.next()) {
            Certificate certificate = certificateRowMapper.mapRow(resultSet, 0);
            certificate.setTagList(tagList);
            tagList.add(tagRowMapper.mapRow(resultSet, 0));
            long bufferId = certificate.getId();
            while (resultSet.next()) {
                long certificateId = resultSet.getLong(ID);
                if (certificateId == bufferId) {
                    Tag tag = tagRowMapper.mapRow(resultSet, 0);
                    if (tag.getName() != null) {
                        tagList.add(tagRowMapper.mapRow(resultSet, 0));
                    }
                } else {
                    certificateList.add(certificate);
                    tagList = new ArrayList<>();
                    certificate = certificateRowMapper.mapRow(resultSet, 0);
                    certificate.setTagList(tagList);
                    Tag tag = tagRowMapper.mapRow(resultSet, 0);
                    if (tag.getName() != null) {
                        tagList.add(tagRowMapper.mapRow(resultSet, 0));
                    }
                    bufferId = certificate.getId();
                }
            }
            certificateList.add(certificate);
        }
        return certificateList;
    }
}
