package com.epam.esm.dao;

import com.epam.esm.entities.Tag;
import com.epam.esm.mapping.TagRowMapper;
import com.epam.esm.services.TagDao;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

/**
 * Implements {@link TagDao < Certificate >}.
 * <p>Uses {@link JdbcTemplate} for CRD operations
 */
@Repository
public class TagJdbcDao implements TagDao {

    private static final String REMOVE_TAG_BY_ID = "DELETE FROM tag WHERE id = ?";
    private static final String INSERT_TAG = "INSERT INTO tag(NAME) VALUES(?)";
    private static final String GET_ALL = "SELECT * FROM tag";
    private static final String GET_BY_ID = "SELECT * FROM tag WHERE id = ?";
    private static final String GET_BY_NAME = "SELECT * FROM tag WHERE name = ?";
    private static final String MORE_THAN_ONE_RECORD_WERE_FOUND_NAME = "tag.multipleName";
    private static final String MORE_THAN_ONE_RECORD_WERE_FOUND_ID = "tag.multipleId";
    private static final String ID = "id";
    private static final String DAO_TAG_ROW_MAPPER = "daoTagRowMapper";

    @Resource
    private JdbcTemplate jdbcTemplate;

    @Resource(name = DAO_TAG_ROW_MAPPER)
    private TagRowMapper tagRowMapper;

    @Override
    public Long create(Tag entity) {
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
        return jdbcTemplate.query(GET_ALL, tagRowMapper);
    }

    @Override
    public Optional<Tag> getById(Long id) {
        List<Tag> tagList = jdbcTemplate.query(GET_BY_ID, tagRowMapper, id);
        return tagList.stream().findAny();
    }

    @Override
    public Optional<Tag> getByName(String name) {
        List<Tag> tagList = jdbcTemplate.query(GET_BY_NAME, tagRowMapper, name);
        return tagList.stream().findAny();
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update(REMOVE_TAG_BY_ID, id);
    }


}
