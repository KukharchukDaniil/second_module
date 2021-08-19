package com.epam.esm.dao;

import com.epam.esm.entities.Certificate;
import com.epam.esm.mapping.CertificateResultSetExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CertificateDao implements Dao<Certificate> {
    //TODO: use ResultSetHandler instead of RowMapper for mapping certificate

    private final JdbcTemplate jdbcTemplate;

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
    private static final String GET_ALL_ORDERED_BY_NAME_ASC =
                    " SELECT * FROM gift_certificate LEFT JOIN (" +
                    " SELECT tag.name as tag_name, tag.id as tag_id, certificate_id FROM certificate_tag" +
                    " JOIN tag ON (tag_id = tag.id)) AS new_tag ON (gift_certificate.id = new_tag.certificate_id)" +
                    " ORDER BY gift_certificate.name asc, gift_certificate.id asc";
    private static final String GET_ALL_ORDERED_BY_NAME_DESC =
                    " SELECT * FROM gift_certificate LEFT JOIN (" +
                    " SELECT tag.name as tag_name, tag.id as tag_id, certificate_id FROM certificate_tag" +
                    " JOIN tag ON (tag_id = tag.id)) AS new_tag ON (gift_certificate.id = new_tag.certificate_id)" +
                    " ORDER BY gift_certificate.name desc, gift_certificate.id desc";

    private static final String GET_BY_NAME_PART =
            "SELECT * FROM gift_certificate JOIN (" +
                    " SELECT tag.name as tag_name, tag.id as tag_id, certificate_id FROM certificate_tag" +
                    " JOIN tag ON (tag_id = tag.id)) AS new_tag ON (gift_certificate.id = new_tag.certificate_id)" +
                    " WHERE name LIKE ? ORDER BY gift_certificate.id";
    private static final String DELETE_BY_ID =
            "DELETE FROM gift_certificate WHERE id = ?";


    @Autowired
    public CertificateDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void create(Certificate entity) {
        jdbcTemplate.update(INSERT_CERTIFICATE, entity.getName(), entity.getDescription(), entity.getPrice(),
                entity.getDuration(), entity.getCreateDate(), entity.getLastUpdateDate());
    }

    @Override
    public List<Certificate> getAll() {
        return jdbcTemplate.query(GET_ALL, new CertificateResultSetExtractor());
    }

    @Override
    public void deleteById(long id) {
        jdbcTemplate.update(DELETE_BY_ID, id);
    }

    @Override
    public void update(Certificate entity) {
        jdbcTemplate.update(UPDATE_CERTIFICATE, entity.getName(), entity.getDescription(),
                entity.getPrice(), entity.getDuration(), entity.getCreateDate(), entity.getLastUpdateDate(),
                entity.getId());
    }


    public List<Certificate> getByTagName(String tagName) {
        return jdbcTemplate.query(GET_BY_TAG_NAME, new CertificateResultSetExtractor(), tagName);
    }

    public List<Certificate> getByNamePart(String namePart) {
        String formattedNamePart = "%" + namePart + "%";
        return jdbcTemplate.query(GET_BY_NAME_PART, new CertificateResultSetExtractor(), formattedNamePart);
    }

    public List<Certificate> getAllSortedByNameAsc(String tagName) {
        return jdbcTemplate.query( GET_ALL_ORDERED_BY_NAME_ASC, new CertificateResultSetExtractor(), tagName);
    }
    public List<Certificate> getAllSortedByNameDesc(String tagName) {
        return jdbcTemplate.query( GET_ALL_ORDERED_BY_NAME_DESC, new CertificateResultSetExtractor(), tagName);
    }

}
