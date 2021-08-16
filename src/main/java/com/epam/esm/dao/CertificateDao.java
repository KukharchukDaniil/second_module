package com.epam.esm.dao;

import com.epam.esm.entities.Certificate;
import com.epam.esm.mappers.CertificateRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public class CertificateDao extends AbstractDao<Certificate> {

    private static final String UPDATE_CERTIFICATE =
            "UPDATE gift_certificate SET name = ?, description = ?, price = ?, duration = ?," +
                    "create_date = ?, last_update_date = ? WHERE id = ?";
    private static final String INSERT_CERTIFICATE =
            "INSERT INTO gift_certificate(name, description, price, duration, create_date," +
                    " last_update_date) values(?, ?, ?, ?, ?, ?)";
    private static final String GET_BY_TAG_NAME =
            "SELECT * FROM gift_certificate\n" +
            "WHERE ID IN(\n" +
            "    SELECT certificate_id FROM certificate_tag\n" +
            "    JOIN tag ON (name = ?)\n" +
            "    WHERE tag_id = tag.id\n" +
            "    )";
    private static final String TABLE_NAME = "gift_certificate";

    @Autowired
    protected CertificateDao(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    protected RowMapper<Certificate> getRowMapper() {
        return new CertificateRowMapper();
    }

    @Override
    protected String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public void update(Certificate entity) {
        jdbcTemplate.update(UPDATE_CERTIFICATE, entity.getName(), entity.getDescription(),
                entity.getPrice(), entity.getDuration(), entity.getCreateDate(), entity.getLastUpdateDate(),
                entity.getId());
    }

    @Override
    public void save(Certificate entity) {
        jdbcTemplate.update(INSERT_CERTIFICATE, entity.getName(), entity.getDescription(),
                entity.getPrice(), entity.getDuration(), entity.getCreateDate(), entity.getLastUpdateDate());
    }

    public List<Certificate> getByTagName(String tagName) {
        List<Certificate> result = jdbcTemplate.query(GET_BY_TAG_NAME, new CertificateRowMapper(), tagName);
        return result ;
    }
}
