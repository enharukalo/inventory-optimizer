package edu.estu;

import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * Class to load ZChart data.
 */
public class ZChartLoader {
    private final TreeMap<Double, Double[]> zChartMap;

    public ZChartLoader() {
        zChartMap = new TreeMap<>();
        loadZChart();
    }

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

    public double getZValueForFR(double Fr) {
        return zChartMap.ceilingEntry(Fr).getValue()[0];
    }

    public double getLZValueForFR(double Fr) {
        return zChartMap.ceilingEntry(Fr).getValue()[1];
    }

    public TreeMap<Double, Double[]> getZChartMap() {
        return zChartMap;
    }
}
