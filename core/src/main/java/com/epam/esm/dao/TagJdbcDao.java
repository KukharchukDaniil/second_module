package com.epam.esm.dao;

import com.epam.esm.entities.Tag;
import com.epam.esm.exceptions.dao.MultipleRecordsWereFoundException;
import com.epam.esm.mapping.TagRowMapper;
import com.epam.esm.services.TagDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

/**
 * Implements {@link TagDao < Certificate >}.
 * <p>Uses {@link JdbcTemplate} for CRD operations
 */
@Component
public class TagJdbcDao implements TagDao {

    private static final String REMOVE_TAG_BY_ID = "DELETE FROM tag WHERE id = ?";
    private static final String INSERT_TAG = "INSERT INTO tag(NAME) VALUES(?)";
    private static final String GET_ALL = "SELECT * FROM tag";
    private static final String GET_BY_ID = "SELECT * FROM tag WHERE id = ?";
    private static final String GET_BY_NAME = "SELECT * FROM tag WHERE name = ?";
    private static final String ID_COLUMN = "id";
    private static final String NAME_COLUMN = "name";
    private static final String MORE_THAN_ONE_RECORD_WERE_FOUND_NAME = "More than one record were found {name = %s}";
    public static final String MORE_THAN_ONE_RECORD_WERE_FOUND_ID = "More than one record were found {id = %s}";
    private static final String ID = "id";


    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public TagJdbcDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public long create(Tag entity) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_TAG, new String[]{ID});
            preparedStatement.setString(1, entity.getName());
            return preparedStatement;
        }, keyHolder);
        return keyHolder.getKey().longValue();
    }

    @Override
    public List<Tag> getAll() {
        return jdbcTemplate.query(GET_ALL, new TagRowMapper(ID_COLUMN, NAME_COLUMN));
    }

    @Override
    public Optional<Tag> getById(long id) throws MultipleRecordsWereFoundException {
        List<Tag> tagList = jdbcTemplate.query(GET_BY_ID, new TagRowMapper(ID_COLUMN, NAME_COLUMN), id);
        if (tagList.size() > 1) {
            throw new MultipleRecordsWereFoundException(String.format(MORE_THAN_ONE_RECORD_WERE_FOUND_ID, id));
        }
        return tagList.stream().findAny();
    }

    @Override
    public Optional<Tag> getByName(String name) {
        List<Tag> tagList = jdbcTemplate.query(GET_BY_NAME, new TagRowMapper(ID_COLUMN, NAME_COLUMN), name);
        if (tagList.size() > 1) {
            throw new MultipleRecordsWereFoundException(String.format(MORE_THAN_ONE_RECORD_WERE_FOUND_NAME, name));
        }
        return tagList.stream().findAny();
    }

    @Override
    public void deleteById(long id) {
        jdbcTemplate.update(REMOVE_TAG_BY_ID, id);
    }


}
