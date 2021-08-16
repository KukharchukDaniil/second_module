package com.epam.esm.dao;

import com.epam.esm.entities.Tag;
import com.epam.esm.mappers.TagRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TagDao extends AbstractDao<Tag> {

    private static final String UPDATE_TAG = "UPDATE tag SET name=? WHERE id = ?";
    private static final String SAVE_TAG = "INSERT INTO tag(NAME) VALUES(?)";
    private static final String TABLE_NAME = "tag";
    private static final String GET_ALL_BY_CERTIFICATE_ID =
                    "SELECT id, name FROM tag JOIN certificate_tag" +
                    " ON (certificate_id = ? and certificate_tag.tag_id = id)";


    @Autowired
    public TagDao(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    protected RowMapper<Tag> getRowMapper() {
        return new TagRowMapper();
    }

    @Override
    protected String getTableName() {
        return TABLE_NAME;
    }

    public List<Tag> getAllByCertificateId(Long id) {
        return jdbcTemplate.query(GET_ALL_BY_CERTIFICATE_ID, new TagRowMapper());
    }

    @Override
    public void update(Tag entity) {
        jdbcTemplate.update(UPDATE_TAG, entity.getName(), entity.getId());
    }

    @Override
    public void save(Tag entity) {
        jdbcTemplate.update(SAVE_TAG, entity.getName());
    }
}
