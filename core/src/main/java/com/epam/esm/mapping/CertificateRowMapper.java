package com.epam.esm.mapping;

import com.epam.esm.entities.Certificate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * Maps {@link Certificate} from current row in {@link ResultSet}
 */
public class CertificateRowMapper implements RowMapper<Certificate> {

    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String PRICE = "price";
    public static final String DURATION = "duration";
    public static final String CREATE_DATE = "create_date";
    public static final String LAST_UPDATE_DATE = "last_update_date";

    @Override
    public Certificate mapRow(ResultSet resultSet, int rowNum) throws SQLException {

        Certificate certificate = new Certificate();
        certificate.setId(resultSet.getLong(ID));
        certificate.setName(resultSet.getString(NAME));
        certificate.setDescription(resultSet.getString(DESCRIPTION));
        certificate.setPrice(resultSet.getInt(PRICE));
        certificate.setDuration(resultSet.getInt(DURATION));

        String dateString = resultSet.getString(CREATE_DATE);
        LocalDateTime localDateTime = LocalDateTime.parse(dateString);
        certificate.setCreateDate(localDateTime);

        dateString = resultSet.getString(LAST_UPDATE_DATE);
        localDateTime = LocalDateTime.parse(dateString);
        certificate.setLastUpdateDate(localDateTime);
        return certificate;
    }
}
