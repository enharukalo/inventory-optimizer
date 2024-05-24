package edu.estu;

import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * The ZChartLoader class is responsible for loading ZChart data.
 * ZChart data is used to calculate the Z value for a given FR value.
 * The data is loaded from a file and stored in a TreeMap.
 * The class provides methods to get the Z value and L(Z) value for a given FR value.
 */
public class ZChartLoader {
    // TreeMap to store the ZChart data
    private final TreeMap<Double, Double[]> zChartMap;

    /**
     * Constructor for the ZChartLoader class.
     * Initializes the TreeMap and calls the method to load the ZChart data.
     */
    public ZChartLoader() {
        zChartMap = new TreeMap<>();
        loadZChart();
    }

    /**
     * Method to load the ZChart data from a file.
     * The data is read line by line and stored in the TreeMap.
     * Each line is split into three values: Z, f(Z), and L(Z).
     * These values are stored as a Double array in the TreeMap, with f(Z) as the key.
     */
    private void loadZChart() {
        Path path = Paths.get("classes/ZChart.tsv");
        try (BufferedReader br = Files.newBufferedReader(path)) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("#")) continue; // Skip header
                String[] values = line.split("\\t");
                Double z = Double.valueOf(values[0]);
                Double fZ = Double.valueOf(values[1]);
                Double lZ = Double.valueOf(values[2]);
                zChartMap.put(fZ, new Double[]{z, lZ});
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to get the Z value for a given FR value.
     * The method uses the ceilingEntry method of the TreeMap to get the closest value.
     * @param Fr The FR value for which the Z value is to be found.
     * @return The Z value corresponding to the given FR value.
     */
    public double getZValueForFR(double Fr) {
        return zChartMap.ceilingEntry(Fr).getValue()[0];
    }

    /**
     * Method to get the L(Z) value for a given FR value.
     * The method uses the ceilingEntry method of the TreeMap to get the closest value.
     * @param Fr The FR value for which the L(Z) value is to be found.
     * @return The L(Z) value corresponding to the given FR value.
     */
    public double getLZValueForFR(double Fr) {
        return zChartMap.ceilingEntry(Fr).getValue()[1];
    }
}