package com.epam.esm.dao;

import com.epam.esm.entities.Certificate;
import com.epam.esm.entities.Tag;
import com.epam.esm.exceptions.dao.DaoException;
import com.epam.esm.exceptions.dao.MultipleRecordsWereFoundException;
import com.epam.esm.mapping.CertificateResultSetExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Implements {@link Dao<Certificate>}.
 * <p>Uses {@link JdbcTemplate} for CRUD operations
 */
@Component
public class CertificateDao implements Dao<Certificate> {

    public static final String MULTIPLE_RECORDS_WERE_FOUND_BY_ID = "Multiple records were found by id: ";
    private static final String GET_BY_ID = "SELECT * FROM gift_certificate LEFT JOIN (" +
            " SELECT tag.name as tag_name, tag.id as tag_id, certificate_id FROM certificate_tag" +
            " JOIN tag ON (tag_id = tag.id)) AS new_tag ON (gift_certificate.id = new_tag.certificate_id) WHERE id = ?";

    private static final String ATTACHE_CERTIFICATE_TO_TAG = "INSERT INTO certificate_tag(tag_id, certificate_id) VALUES(?,?)";

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

    public static final String TABLE_NAME = "gift_certificate";
    public static final String ID = "ID";

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CertificateDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public List<Certificate> getAll() {
        return jdbcTemplate.query(GET_ALL, new CertificateResultSetExtractor());
    }


    /**
     * Returns all {@link Certificate} attached to corresponding {@link Tag} in database
     *
     * @param tagName
     * @return certificates list
     */
    public List<Certificate> getByTagName(String tagName) {
        return jdbcTemplate.query(GET_BY_TAG_NAME, new CertificateResultSetExtractor(), tagName);
    }


    /**
     * Returns list of certificates which contain a namePart in the "name" field
     *
     * @param namePart
     * @return certificates list
     */
    public List<Certificate> getByNamePart(String namePart) {
        String formattedNamePart = "%" + namePart + "%";
        return jdbcTemplate.query(GET_BY_NAME_PART, new CertificateResultSetExtractor(), formattedNamePart);
    }


    @Override
    public Optional<Certificate> getById(long id) throws DaoException {
        List<Certificate> query = jdbcTemplate.query(GET_BY_ID, new CertificateResultSetExtractor(), id);
        if (query.size() > 1) {
            throw new MultipleRecordsWereFoundException(MULTIPLE_RECORDS_WERE_FOUND_BY_ID + id);
        }
        return query.stream().findAny();
    }

    @Override
    public void update(Certificate entity) {
        LocalDateTime createDate = entity.getCreateDate();
        LocalDateTime lastUpdateDate = entity.getLastUpdateDate();
        jdbcTemplate.update(UPDATE_CERTIFICATE, entity.getName(), entity.getDescription(),
                entity.getPrice(), entity.getDuration(), createDate != null ? createDate.toString() : null,
                lastUpdateDate != null ? lastUpdateDate.toString() : null,
                entity.getId());
    }


    @Override
    public long create(Certificate entity) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_CERTIFICATE, new String[]{ID});
            preparedStatement.setString(1, entity.getName());
            preparedStatement.setString(2, entity.getDescription());
            preparedStatement.setInt(3, entity.getPrice());
            preparedStatement.setInt(4, entity.getDuration());
            preparedStatement.setString(5, entity.getCreateDate() != null ?
                    entity.getCreateDate().toString() : null);
            preparedStatement.setString(6, entity.getLastUpdateDate() != null ?
                    entity.getLastUpdateDate().toString() : null);
            return preparedStatement;
        }, keyHolder);


        return keyHolder.getKey().longValue();
    }

    /**
     * @param certificateId certificate id of corresponding row in DB
     * @param tagId         {@link Tag} id of corresponding row in DB
     * @return true, if there is a record in "certificate_tag" table with corresponding data. Otherwise returns false.
     */
    public boolean isAttachedToTag(Long certificateId, Long tagId) {
        Integer counter = jdbcTemplate.queryForObject(FIND_CERTIFICATE_TAG, Integer.class, certificateId, tagId);
        return counter == 1;
    }


    /**
     * Creates a new record in "certificate_tag" table
     *
     * @param certificateId identifier of certificate to be inserted
     * @param tagId         identifier of {@link Tag} to be inserted
     */
    public void attachCertificateToTag(long certificateId, long tagId) {
        jdbcTemplate.update(ATTACHE_CERTIFICATE_TO_TAG, tagId, certificateId);
    }

    @Override
    public void deleteById(long id) {
        jdbcTemplate.update(DELETE_BY_ID, id);
    }
}
