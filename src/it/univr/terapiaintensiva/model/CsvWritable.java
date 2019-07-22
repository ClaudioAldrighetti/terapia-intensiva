package it.univr.terapiaintensiva.model;

/**
 * @author ClaudioAldrighetti
 * Guarantees compatibility with csv format.
 * All classes that have to perform operations with csv files must implement this interface.
 */
public interface CsvWritable {
    /**
     * @return {@link String} of values comma-separated.
     * @author ClaudioAldrighetti
     * {@link String} returned contains class fields that must respect the csv format of the class.
     */
    String toCsv();

    /**
     * @return {@link String} rapresenting the order of values in records of csv file.
     * @author ClaudioAldrighetti
     * This format must be written on first line of each csv file.
     * All classes that implement {@link CsvWritable} must implement an own csvFormat() method based on class fields.
     */
    static String csvFormat(){
        return "";
    }
}
