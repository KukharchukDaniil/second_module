package com.epam.esm.dao;

import com.epam.esm.entities.Tag;
import com.epam.esm.mapping.TagRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TagDao implements Dao<Tag> {

    private static final String REMOVE_TAG_BY_ID = "DELETE FROM tag WHERE id = ?";
    private static final String UPDATE_TAG = "UPDATE tag SET name=coalesce(?,name) WHERE id = ?";
    private static final String INSERT_TAG = "INSERT INTO tag(NAME) VALUES(?)";
    private static final String GET_ALL = "SELECT * FROM tag";
    private static final String ID_COLUMN = "id";
    private static final String NAME_COLUMN = "name";


    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public TagDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void create(Tag entity) {
        jdbcTemplate.update(INSERT_TAG, entity.getName());
    }

    @Override
    public List<Tag> getAll() {
        return jdbcTemplate.query(GET_ALL, new TagRowMapper(ID_COLUMN,NAME_COLUMN));
    }

    @Override
    public void deleteById(long id) {
        jdbcTemplate.update(REMOVE_TAG_BY_ID, id);
    }

    @Override
    public void update(Tag entity) {
        jdbcTemplate.update(UPDATE_TAG, entity.getName(), entity.getId());
    }
}
