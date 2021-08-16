package com.epam.esm.mappers;

import com.epam.esm.entities.Tag;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TagRowMapper implements RowMapper<Tag> {
    @Override
    public Tag mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Tag resultTag = new Tag();
        resultTag.setId(resultSet.getLong("id"));
        resultTag.setName(resultSet.getString("name"));
        return resultTag;
    }
}
