package it.univr.terapiaintensiva.model;

/**
 * Guarantees compatibility with csv format.
 * All classes that have to perform operations with csv files must implement this interface.
 * @author mizukami
 */
public interface CsvWritable {
    /**
     * {@link String} returned contains class fields that must respect the csv format of the class.
     * @return {@link String} of values comma-separated.
     */
    String toCsv();

    /**
     * This format must be written on first line of each csv file.
     * All classes that implement {@link CsvWritable} must implement an own csvFormat() method based on class fields.
     * @return {@link String} rapresenting the order of values in records of csv file.
     */
    static String csvFormat(){
        return "";
    }
}
