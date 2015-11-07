package at.sporty.team1.misc.converters;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by sereGkaluv on 06-Nov-15.
 */

@Converter(autoApply = true)
public class SQLDateConverter implements AttributeConverter<String, Date> {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final SimpleDateFormat DATE_FORMATTER =  new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public Date convertToDatabaseColumn(String attribute) {
        try {
            return new java.sql.Date(DATE_FORMATTER.parse(attribute).getTime());
        } catch (ParseException e) {
            LOGGER.error("Error occurs while parsing date \"{}\".", attribute, e);
            return null;
        }
    }

    @Override
    public String convertToEntityAttribute(Date dbData) {
        return dbData != null ? DATE_FORMATTER.format(dbData) : null;
    }
}
