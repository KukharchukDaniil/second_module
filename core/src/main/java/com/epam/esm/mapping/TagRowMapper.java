package com.epam.esm.mapping;

import com.epam.esm.entities.Tag;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Maps {@link Tag} from current row in {@link ResultSet}
 */
public class TagRowMapper implements RowMapper<Tag> {
    private final String idColumn;
    private final String nameColumn;

    public TagRowMapper(String idColumn, String nameColumn) {
        this.idColumn = idColumn;
        this.nameColumn = nameColumn;
    }

    @Override
    public Tag mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Tag resultTag = new Tag();
        resultTag.setId(resultSet.getLong(idColumn));
        resultTag.setName(resultSet.getString(nameColumn));
        return resultTag;
    }
}
