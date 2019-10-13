package br.com.mc1.numerico;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.Locale;

/**
 *
 */
public class FormatDecimalUtils {

    private static FormatDecimalUtils singleton;

    public Character SYSTEM_GROUP_SEPARATOR;
    public Character SYSTEM_DECIMAL_SEPARATOR;

    public static final FormatDecimalUtils getInstance() {
        if (singleton == null)
            singleton = new FormatDecimalUtils();

        singleton.SYSTEM_GROUP_SEPARATOR = DecimalFormatSymbols.getInstance().getGroupingSeparator();
        singleton.SYSTEM_DECIMAL_SEPARATOR = DecimalFormatSymbols.getInstance().getDecimalSeparator();

        return singleton;
    }

    private FormatDecimalUtils() {}

    @Nullable
    public Character getDecimalDigitSeparator(String value) {
        Character decimalSeparator = SYSTEM_DECIMAL_SEPARATOR;

        if (!value.contains(decimalSeparator.toString())) {
            return null;
        }

        return decimalSeparator;
        /*int indexComma = value.lastIndexOf(',');
        int indexDot = value.lastIndexOf('.');

        if (indexComma == -1 && indexDot == -1)
            return null;

        return indexComma > indexDot ? value.charAt(indexComma) : value.charAt(indexDot);*/
    }

    @NonNull
    public String changeDecimalSeparatorToDot(String value) {
        value = value.replace('.', ',');
        int indexOfLastComma = value.lastIndexOf(',');
        StringBuilder builder = new StringBuilder(value);
        builder.setCharAt(indexOfLastComma, '.');

        return builder.toString();
    }

    /**
     * Given a string that represents a floating number, it will be parsed
     * @param value
     * @return
     * @throws ParseException
     */
    public String applyRootPattern(String value) throws ParseException {

        Character decimalSeparator = getDecimalDigitSeparator(value);
        if (decimalSeparator == null)
            return value;

        DecimalFormat defaultDF = (DecimalFormat) DecimalFormat.getInstance();
        DecimalFormat rootDF = (DecimalFormat) DecimalFormat.getInstance(Locale.ROOT);

        if (decimalSeparator.equals('.'))
            defaultDF = (DecimalFormat) DecimalFormat.getInstance(Locale.US);

        Number numVal = defaultDF.parse(value);

        return rootDF.format(numVal);
    }

    public String clearDecimalCharacters(String value) {
        return value.replaceAll("\\D", "");
    }

    @NonNull
    public DecimalFormat getDecimalFormat(int fractionDigits) {
        DecimalFormat formatter = (DecimalFormat) DecimalFormat.getInstance();
        if (fractionDigits != -1) {
            formatter.setMaximumFractionDigits(fractionDigits);
            formatter.setMinimumFractionDigits(fractionDigits);
        } else {
            // Em um teste no android 10 pelo emulador, o default tem como padrao o valor 3 para máximo de digitos decimais
            formatter.setMaximumFractionDigits(100);
        }
        return formatter;
    }

    public Double parseDouble(String value) throws ParseException {
        value = removeGroupSeparators(value);
        value = applyRootPattern(value);
        return Double.parseDouble(value);
    }

    /**
     * Remove extra decimal digits
     * @param value string representation of the value
     * @param fractionDigits amount of decimal digits to stay
     * @return
     */
    public String wrap(String value, int fractionDigits) {
        Character fractionSeparator = getDecimalDigitSeparator(value);

        if (fractionSeparator != null) {
            int indexFractionSeparator = value.indexOf(Character.toString(fractionSeparator));
            int endIndex = (indexFractionSeparator + fractionDigits) + 1;

            if (endIndex > value.length())
                endIndex = value.length();

            return value.substring(0, endIndex);
        }

        return value;
    }

    public int countGroupSeparator(String value) {
        DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance();
        Character groupSeparator = symbols.getGroupingSeparator();


        if (!value.contains(groupSeparator.toString()))
            return 0;

        return value.split(
                new StringBuilder()
                        .append("\\")
                        .append(groupSeparator.toString())
                        .toString()
        ).length-1;
    }

    public String fromUStoDefaultFormat(String value) {
        String groupSeparator = ",";
        String decimalSeparator = ".";

        value = value.replace(groupSeparator, SYSTEM_GROUP_SEPARATOR.toString());
        int indexOfDecimalSeparator = value.lastIndexOf(decimalSeparator);
        StringBuilder builder = new StringBuilder(value);

        if (indexOfDecimalSeparator != -1)
            builder.setCharAt(indexOfDecimalSeparator, SYSTEM_DECIMAL_SEPARATOR);

        return builder.toString();
    }

    public String dynamicMask(String value) throws ParseException {
        Character decimalSeparator = getDecimalDigitSeparator(value);

        if (decimalSeparator == null) {
            return getDecimalFormat(0).format(parseDouble(value));
        }

        String mantissa = value.substring(value.indexOf(decimalSeparator)+1);

        if (value.endsWith(".") || value.endsWith(",")) {
            return getDecimalFormat(mantissa.length()).format(parseDouble(value)) + value.charAt(value.length()-1);
        }

        return getDecimalFormat(mantissa.length()).format(parseDouble(value));
    }

    public String removeGroupSeparators(String value) {
        /*DecimalFormat formatter;
        Number numValue;

        try {
            formatter = (DecimalFormat) DecimalFormat.getInstance(Locale.US);
            numValue = formatter.parse(value);
        } catch (ParseException e) {
            // current format isn't root, lets try default
            try {
                formatter = (DecimalFormat) DecimalFormat.getInstance();
                numValue = formatter.parse(value);
            } catch (ParseException e1) {
                //nor local neither root worked
                return value;
            }
        }
        char groupSeparator = formatter.getDecimalFormatSymbols().getGroupingSeparator();

        return value.replace(Character.toString(groupSeparator), "");*/
        char groupSeparator = SYSTEM_GROUP_SEPARATOR;

        return value.replace(Character.toString(groupSeparator), "");
    }

    public String removeExtraSignal(String value) {
        boolean haveSignal = value.startsWith("-");

        value = value.replace("-", "");

        if (haveSignal)
            return "-"+value;
        else
            return value;
    }

    public String removeExtraDecimalSeparator(String value) {
        String decimalSeparator = SYSTEM_DECIMAL_SEPARATOR.toString();
        int firstSeparatorIndex = value.indexOf(decimalSeparator);
        value = value.replace(decimalSeparator, "");

        return new StringBuilder(value).insert(firstSeparatorIndex, decimalSeparator).toString();
    }
}
