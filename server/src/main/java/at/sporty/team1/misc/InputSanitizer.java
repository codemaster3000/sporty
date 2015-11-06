package at.sporty.team1.misc;

import at.sporty.team1.rmi.exceptions.DataType;
import com.sun.org.apache.xerces.internal.impl.xpath.regex.RegularExpression;

import java.util.regex.PatternSyntaxException;

/**
 * Validation class evaluates different InputForm types.
 */
public class InputSanitizer {
    private static DataType lastFailedValidation;

    public InputSanitizer() {
    }

    /**
     * Checks if a given value matches the defined rules assigned to it's type.
     *
     * @param value the value that needs to be tested
     * @param type  the Type of Format that shall be matched
     * @return Boolean True if matched, false if not
     * @throws PatternSyntaxException
     *********************************************************************************/
    public boolean isValid(String value, DataType type) throws PatternSyntaxException {

        switch (type) {

            case USERNAME: {
                RegularExpression expr = new RegularExpression("[a-zA-Z0-9]{1,30}");
                lastFailedValidation = DataType.USERNAME;
                return !isNull(value) && value.length() >= 1 && expr.matches(value);
            }

            case DAY_DATE: {
                // 13.12.2003 e.g.
                // this monster regex *should* match all dates of format: dd.mm.yyyy OR dd-mm-yyyy including schaltjahre :) T
                RegularExpression expr = new RegularExpression("^(?:(?:31(\\/|-|\\.)(?:0?[13578]|1[02]))\\1|(?:(?:29|30)(\\/|-|\\.)(?:0?[1,3-9]|1[0-2])\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$|^(?:29(\\/|-|\\.)0?2\\3(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))$|^(?:0?[1-9]|1\\d|2[0-8])(\\/|-|\\.)(?:(?:0?[1-9])|(?:1[0-2]))\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$");
                //return true if matches, false if not
                lastFailedValidation = DataType.DAY_DATE;
                return isNull(value) || value.length() <= 10 && expr.matches(value);
            }

            case SQL_DATE: {
                //yyyy-mm-dd
                RegularExpression expression = new RegularExpression("^\\d{4}-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01])$");
                lastFailedValidation = DataType.SQL_DATE;
                return  isNull(value) || value.length() == 10 && expression.matches(value);
            }

            //this matches e.g.:
            //Manuel-Max Mustermann
            //Sarah Submit
            //Heinz-Michael
            //Maximilian
            //Ölaf-Test van ölüfää O'Ström
            case NAME: {
                RegularExpression expr = new RegularExpression("([ \\u00c0-\\u01ffa-zA-Z'\\-])+");
                lastFailedValidation = DataType.NAME;
                return isNull(value) || value.length() <= 100 && expr.matches(value);
            }

            case INTEGER_TYPE: {
                RegularExpression expr = new RegularExpression("[0-9]{0,11}");
                lastFailedValidation = DataType.INTEGER_TYPE;
                return isNull(value) || value.length() <= 11 && expr.matches(value);
            }

            case SIN: {
                // socialInsuranceIDs, 10 digits format on E-Card --- but others?
                RegularExpression expr = new RegularExpression("[a-zA-Z0-9]{1,66}");
                lastFailedValidation = DataType.SIN;
                return isNull(value) || value.length() <= 66 && expr.matches(value);
            }

            case FLOAT_TYPE: {
                RegularExpression expr = new RegularExpression("[-+]?([0-9]*\\.[0-9]+|[0-9]+)");
                lastFailedValidation = DataType.FLOAT_TYPE;
                return isNull(value) || value.length() <= 11 && expr.matches(value);
            }

            case TEXT: {
                lastFailedValidation = DataType.TEXT;
                return isNull(value) || value.length() <= 1000;
            }

            case PASSWORD: {
                lastFailedValidation = DataType.PASSWORD;
                return !isNull(value) && value.length() >= 6 && value.length() <= 30;
            }

            case EMAIL: {
                RegularExpression expr = new RegularExpression("[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]*");
                lastFailedValidation = DataType.EMAIL;
                return isNull(value) || value.length() >= 5 && expr.matches(value);
            }

            case ZIP: {
                RegularExpression expression = new RegularExpression("[a-zA-Z0-9]{1,6}");
                lastFailedValidation = DataType.ZIP;
                return isNull(value) || value.length() >= 1 && expression.matches(value);
            }

            case PHONE_NUMBER: {
                // telephone, like: "0043 69917183173"
                RegularExpression expr = new RegularExpression("[0-9]{4} ?[0-9]{3,12}");
                lastFailedValidation = DataType.PHONE_NUMBER;
                return isNull(value) || value.length() >= 4 && expr.matches(value);
            }

            case ADDRESS: {
                RegularExpression expression = new RegularExpression(".{1,255}");
                lastFailedValidation = DataType.ADDRESS;
                return isNull(value) || value.length() >= 1 && expression.matches(value);
            }

            case GENDER: {
                RegularExpression expression = new RegularExpression("F|M");
                lastFailedValidation = DataType.GENDER;
                return isNull(value) || value.length() == 1 && expression.matches(value);
            }

            case STREET_NUMBER: {
                lastFailedValidation = DataType.STREET_NUMBER;
                return isNull(value) || value.length() >= 1 && value.length() <= 30;
            }

            default: return false;

        }
    }

    public String getLastFailedValidation() {
        return lastFailedValidation.name();
    }

    public boolean isNull(String value) {
       return value == null;
    }
}