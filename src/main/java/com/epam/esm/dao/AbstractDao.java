package com.epam.esm.dao;

import com.epam.esm.entities.Entity;
import com.epam.esm.entities.Tag;
import com.epam.esm.mappers.TagRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public abstract class AbstractDao<T extends Entity> implements Dao<T> {
    private static String GET_ALL_QUERY = "SELECT * FROM %s";
    private static String DELETE_BY_ID_QUERY = "DELETE FROM %s WHERE id=?";

    protected final JdbcTemplate jdbcTemplate;


    protected abstract RowMapper<T> getRowMapper();
    protected abstract String getTableName();


    protected AbstractDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void create(T entity) {
        if (entity.getId() == null) {
            save(entity);
        } else {
            update(entity);
        }
    }

    @Override
    public List<T> getAll() {
        String query = String.format(GET_ALL_QUERY, getTableName());
        return jdbcTemplate.query(query, getRowMapper());
    }

    @Override
    public void deleteById(Long id) {
        String query = String.format(DELETE_BY_ID_QUERY, getTableName());
        jdbcTemplate.update(query, id);
    }
}
