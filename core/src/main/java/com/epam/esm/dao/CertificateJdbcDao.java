package com.epam.esm.dao;

import com.epam.esm.entities.Certificate;
import com.epam.esm.mapping.CertificateResultSetExtractor;
import com.epam.esm.services.CertificateDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Implements {@link CertificateDao}.
 * <p>Uses {@link JdbcTemplate} for implementing {@link CertificateDao} methods operations
 */
@Repository
public class CertificateJdbcDao implements CertificateDao {

    private static final String MULTIPLE_RECORDS_WERE_FOUND_BY_ID = "certificate.multipleId";
    private static final String GET_BY_ID = "SELECT * FROM gift_certificate LEFT JOIN (" +
            " SELECT tag.name as tag_name, tag.id as tag_id, certificate_id FROM certificate_tag" +
            " JOIN tag ON (tag_id = tag.id)) AS new_tag ON (gift_certificate.id = new_tag.certificate_id) WHERE id = ?";

    private static final String ATTACH_CERTIFICATE_TO_TAG = "INSERT INTO certificate_tag(tag_id, certificate_id) VALUES(?,?)";
    private static final String DETACH_CERTIFICATE_FROM_TAG = "DELETE FROM certificate_tag WHERE certificate_id = ? and " +
            "tag_id not in (?)";

    private static final String UPDATE_CERTIFICATE =
            "UPDATE gift_certificate SET name = coalesce(?,name), description = coalesce(?,description)," +
                    " price = coalesce(?,price), duration = coalesce(?,duration)," +
                    " create_date = coalesce(?,create_date), last_update_date = coalesce(?,last_update_date) WHERE id = ?";
    private static final String INSERT_CERTIFICATE =
            "INSERT INTO gift_certificate(name, description, price, duration, create_date," +
                    " last_update_date) values(?, ?, ?, ?, ?, ?)";
    private static final String GET_BY_TAG_NAME =
            "SELECT * FROM gift_certificate JOIN (" +
                    " SELECT tag.name as tag_name, tag.id as tag_id, certificate_id FROM certificate_tag" +
                    " JOIN tag ON (tag_id = tag.id) WHERE tag.name = ?) " +
                    " AS new_tag ON (gift_certificate.id = new_tag.certificate_id)" +
                    " ORDER BY gift_certificate.id";
    private static final String GET_ALL =
            "SELECT * FROM gift_certificate LEFT JOIN (" +
                    " SELECT tag.name as tag_name, tag.id as tag_id, certificate_id FROM certificate_tag" +
                    " JOIN tag ON (tag_id = tag.id)) AS new_tag ON (gift_certificate.id = new_tag.certificate_id)" +
                    " ORDER BY gift_certificate.id";

    private static final String GET_BY_NAME_PART =
            "SELECT * FROM gift_certificate JOIN (" +
                    " SELECT tag.name as tag_name, tag.id as tag_id, certificate_id FROM certificate_tag" +
                    " JOIN tag ON (tag_id = tag.id)) AS new_tag ON (gift_certificate.id = new_tag.certificate_id)" +
                    " WHERE name LIKE ? ORDER BY gift_certificate.id";
    private static final String DELETE_BY_ID =
            "DELETE FROM gift_certificate WHERE id = ?";
    private static final String FIND_CERTIFICATE_TAG =
            "SELECT COUNT(*) FROM certificate_tag WHERE certificate_id = ? AND tag_id = ?";

    private static final String ID = "ID";
    private static final String FORMAT_CHAR = "%";
    private static final String DETACH_ALL_TAGS_FROM_CERTIFICATE =
            "DELETE FROM certificate_tag WHERE certificate_id = ?";
    private final CertificateResultSetExtractor certificateResultSetExtractor;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CertificateJdbcDao(CertificateResultSetExtractor certificateResultSetExtractor, JdbcTemplate jdbcTemplate) {
        this.certificateResultSetExtractor = certificateResultSetExtractor;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Certificate> getAll() {
        return jdbcTemplate.query(GET_ALL, certificateResultSetExtractor);
    }

    @Override
    public List<Certificate> getByTagName(String tagName) {
        return jdbcTemplate.query(GET_BY_TAG_NAME, certificateResultSetExtractor, tagName);
    }

    @Override
    public List<Certificate> getByNamePart(String namePart) {
        String formattedNamePart = FORMAT_CHAR + namePart + FORMAT_CHAR;
        return jdbcTemplate.query(GET_BY_NAME_PART, certificateResultSetExtractor, formattedNamePart);
    }

    @Override
    public Optional<Certificate> getById(Long id) {
        List<Certificate> query = jdbcTemplate.query(GET_BY_ID, certificateResultSetExtractor, id);
        return query.stream().findAny();
    }

    @Override
    public Certificate update(Certificate entity) {
        LocalDateTime createDate = entity.getCreateDate();
        LocalDateTime lastUpdateDate = entity.getLastUpdateDate();
        jdbcTemplate.update(UPDATE_CERTIFICATE,
                entity.getName(),
                entity.getDescription(),
                entity.getPrice(),
                entity.getDuration(),
                createDate != null ? createDate.toString() : null,
                lastUpdateDate != null ? lastUpdateDate.toString() : null,
                entity.getId());
        return entity;
    }

    @Override
    public Long create(Certificate entity) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_CERTIFICATE, new String[]{ID});
            preparedStatement.setString(1, entity.getName());
            preparedStatement.setString(2, entity.getDescription());
            preparedStatement.setBigDecimal(3, entity.getPrice());
            preparedStatement.setInt(4, entity.getDuration());
            preparedStatement.setString(5, entity.getCreateDate().toString());
            preparedStatement.setString(6, entity.getLastUpdateDate().toString());
            return preparedStatement;
        }, keyHolder);


        return keyHolder.getKey().longValue();
    }

    @Override
    public boolean isAttachedToTag(Long certificateId, Long tagId) {
        Integer counter = jdbcTemplate.queryForObject(FIND_CERTIFICATE_TAG, Integer.class, certificateId, tagId);
        return counter == 1;
    }

    @Override
    public void attachCertificateToTag(Long certificateId, Long tagId) {
        jdbcTemplate.update(ATTACH_CERTIFICATE_TO_TAG, tagId, certificateId);
    }

    @Override
    public void detachTagsFromCertificateExceptPresented(Long certificateId, Long[] tagIds) {

        jdbcTemplate.update(DETACH_CERTIFICATE_FROM_TAG, certificateId, tagIds);
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update(DELETE_BY_ID, id);
    }

    @Override
    public void detachAllTagsFromCertificate(Long certificateId) {
        jdbcTemplate.update(DETACH_ALL_TAGS_FROM_CERTIFICATE, certificateId);
    }
}
