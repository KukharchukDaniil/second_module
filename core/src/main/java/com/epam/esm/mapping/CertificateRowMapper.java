package com.epam.esm.mapping;

import com.epam.esm.entities.Certificate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Maps {@link Certificate} from current row in {@link ResultSet}
 */
@Component
public class CertificateRowMapper implements RowMapper<Certificate> {

    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";
    private static final String PRICE = "price";
    private static final String DURATION = "duration";
    private static final String CREATE_DATE = "create_date";
    private static final String LAST_UPDATE_DATE = "last_update_date";

    @Override
    public Certificate mapRow(ResultSet resultSet, int rowNum) throws SQLException {

        Certificate certificate = new Certificate();
        certificate.setId(resultSet.getLong(ID));
        certificate.setName(resultSet.getString(NAME));
        certificate.setDescription(resultSet.getString(DESCRIPTION));
        certificate.setPrice(resultSet.getBigDecimal(PRICE));
        certificate.setDuration(resultSet.getInt(DURATION));

        String dateString = resultSet.getString(CREATE_DATE);
        String localDateTime = dateString;
        certificate.setCreateDate(localDateTime);

        dateString = resultSet.getString(LAST_UPDATE_DATE);
        certificate.setLastUpdateDate(dateString);
        return certificate;
    }
}
