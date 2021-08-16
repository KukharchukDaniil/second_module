package com.epam.esm.mappers;

import com.epam.esm.entities.Certificate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class CertificateRowMapper implements RowMapper<Certificate> {
    @Override
    public Certificate mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
        dateFormat.setTimeZone(timeZone);

        Certificate certificate = new Certificate();
        certificate.setId(resultSet.getLong("id"));
        certificate.setName(resultSet.getString("name"));
        certificate.setDescription(resultSet.getString("description"));
        certificate.setPrice(resultSet.getInt("price"));
        certificate.setDuration(resultSet.getInt("duration"));

        String formattedDate = dateFormat.format(resultSet.getDate("create_date"));
        certificate.setCreateDate(formattedDate);

        formattedDate = dateFormat.format(resultSet.getDate("last_update_date"));
        certificate.setLastUpdateDate(formattedDate);
        return certificate;
    }
}
