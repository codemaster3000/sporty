package at.sporty.team1.misc;

import at.sporty.team1.logging.Loggers;
import com.sun.org.apache.xerces.internal.impl.xpath.regex.RegularExpression;

import java.util.regex.PatternSyntaxException;

/**
 * evaluates different InputForm types
 * ...is a Singleton.
 */
public class InputSanitizer {

    private static InputSanitizer instance = null;
    public static String lastFailedValidation = null;

    // maybe make this ENUM a own class itself..
    public enum TYPE {
        username, exactdate, daydate, name, integertype, ssn, floattype, text, password, email, zip, telephone, address, gender, streetnumber,
    }

    private InputSanitizer() {
    }

    private static InputSanitizer getInstance() {
        if(instance == null)
        instance  = new InputSanitizer();

        return  instance;
    }

    /**
     **********************************************************************************
     * only method of this class - checks if a Value matches the type it should
     *
     * @param[in] value the value that needs to be tested
     * @param[in] type  the Type of Format that shall be matched
     *
     * @return Boolean True if matched, false if not
     *
     * @throws PatternSyntaxException
     *********************************************************************************/
    public static boolean check(String value, TYPE type) throws PatternSyntaxException {

        Loggers.GUI.debug("InputSanitizer.check(" + value + "," + type.toString() + ")" + "called");

        if (value == null)
            return true;

        if (type == TYPE.username) {
            RegularExpression expr = new RegularExpression("[a-zA-Z0-9]{1,30}");
            lastFailedValidation = "username";
            return value.length() >= 1 && expr.matches(value);
        }

        if (type == TYPE.address) {
            RegularExpression expression = new RegularExpression(".{1,255}");
            lastFailedValidation = "address";
            return value.length() >= 1 && expression.matches(value);
        }

        if (type == TYPE.zip) {
            RegularExpression expression = new RegularExpression("[a-zA-Z0-9]{1,6}");
            lastFailedValidation = "zip";
            return value.length() >= 1 && expression.matches(value);
        }
        // telephone, like: "0043 69917183173"
        if (type == TYPE.telephone) {
            RegularExpression expr = new RegularExpression("[0-9]{4} ?[0-9]{3,12}");
            lastFailedValidation = "telephone";
            return value.length() >= 4 && expr.matches(value);
        }
        if (type == TYPE.email) {
            RegularExpression expr = new RegularExpression("[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]*");
            lastFailedValidation = "email";
            return value.length() >= 5 && expr.matches(value);
        }

        // names
        if (type == TYPE.name) {
            RegularExpression expr = new RegularExpression("([ \\u00c0-\\u01ffa-zA-Z'\\-])+");
            lastFailedValidation = "name";
//            this matches e.g.:
//            Manuel-Max Mustermann
//            Sarah Submit
//            Heinz-Michael
//            Maximilian
//            Ölaf-Test van ölüfää O'Ström

            return value.length() <= 100 && expr.matches(value);
        }

        // password; anything longer than 6, smaller than what sha512 can handle; can be anything, so no regex
        if (type == TYPE.password) {
            lastFailedValidation = "password";
            return value.length() >= 6 && value.length() <= 30;
        }

        // exact dates
        if (type == TYPE.exactdate) {
            // must be format like: TODO
            RegularExpression expr = new RegularExpression("TODO");
            lastFailedValidation = "exactdate";
            return value.length() <= 20 && expr.matches(value);
        }

        // integers
        if (type == TYPE.integertype) {
            RegularExpression expr = new RegularExpression("[0-9]{0,11}");
            lastFailedValidation = "integertype";
            return value.length() <= 11 && expr.matches(value);
        }

        // float-alike numbers
        if (type == TYPE.floattype) {
            RegularExpression expr = new RegularExpression("[-+]?([0-9]*\\.[0-9]+|[0-9]+)");
            lastFailedValidation = "floattype";
            return value.length() <= 11 && expr.matches(value);
        }

        // socialsecurityIDs, 10 digits format on E-Card --- but others?
        if (type == TYPE.ssn) {
            //RegularExpression expr = new RegularExpression("[0-9]{10}");
            RegularExpression expr = new RegularExpression("[a-zA-Z0-9]{1,66}");
            return value.length() <= 66 && expr.matches(value);
        }

        // 13.12.2003 e.g.
        if (type == TYPE.daydate) {
            // this monster regex *should* match all dates of format: dd.mm.yyyy OR dd-mm-yyyy including schaltjahre :) T
            RegularExpression expr = new RegularExpression("^(?:(?:31(\\/|-|\\.)(?:0?[13578]|1[02]))\\1|(?:(?:29|30)(\\/|-|\\.)(?:0?[1,3-9]|1[0-2])\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$|^(?:29(\\/|-|\\.)0?2\\3(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))$|^(?:0?[1-9]|1\\d|2[0-8])(\\/|-|\\.)(?:(?:0?[1-9])|(?:1[0-2]))\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$");
            //return true if matches, false if not
            lastFailedValidation = "daydate";
            return value.length() <= 10 && expr.matches(value);
        }

        if (type == TYPE.text) {
            // RegularExpression expr = new RegularExpression("");
            lastFailedValidation = "text";
            return value.length() <= 1000; //&& expr.matches(value);
        }
        if (type == TYPE.gender) {
            RegularExpression expression = new RegularExpression("FEMALE|MALE|NONE");
            lastFailedValidation = "gender";
            return value.length() >= 4 && expression.matches(value);
        }

        if (type == TYPE.streetnumber) {
            lastFailedValidation = "streetnumber";
            return value.length() >= 1 && value.length() <= 30;
        }
        return false;
    }
}