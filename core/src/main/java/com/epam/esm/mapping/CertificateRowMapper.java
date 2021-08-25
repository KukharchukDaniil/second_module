package com.epam.esm.mapping;

import com.epam.esm.entities.Certificate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class CertificateRowMapper implements RowMapper<Certificate> {

    public static final String UTC = "UTC";
    public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm'Z'";
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String PRICE = "price";
    public static final String DURATION = "duration";
    public static final String CREATE_DATE = "create_date";
    public static final String LAST_UPDATE_DATE = "last_update_date";

    @Override
    public Certificate mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        TimeZone timeZone = TimeZone.getTimeZone(UTC);
        DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        dateFormat.setTimeZone(timeZone);

        Certificate certificate = new Certificate();
        certificate.setId(resultSet.getLong(ID));
        certificate.setName(resultSet.getString(NAME));
        certificate.setDescription(resultSet.getString(DESCRIPTION));
        certificate.setPrice(resultSet.getInt(PRICE));
        certificate.setDuration(resultSet.getInt(DURATION));

        Date date = new Date(resultSet.getDate(CREATE_DATE).getTime());
        String formattedDate = dateFormat.format(date);
        certificate.setCreateDate(formattedDate);

        formattedDate = dateFormat.format(resultSet.getDate(LAST_UPDATE_DATE));
        certificate.setLastUpdateDate(formattedDate);
        return certificate;
    }
}
