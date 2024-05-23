package edu.estu;

import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * Class to perform inventory calculations.
 */
public class InventoryCalculator {
    private final TreeMap<Double, Double[]> zChartMap;

    public InventoryCalculator() {
        zChartMap = new TreeMap<>();
        loadZChart();
    }

    private void loadZChart() {
        Path path = Paths.get("ZChart.tsv");
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

    private double getZValueForFR(double Fr) {
        return zChartMap.ceilingEntry(Fr).getValue()[0];
    }

    private double getLZValueForFR(double Fr) {
        return zChartMap.ceilingEntry(Fr).getValue()[1];
    }

    public double calculateHoldingCost(double unitCost, double interestRate) {
        return unitCost * interestRate;
    }

    public double calculateAnnualDemand(double leadTime, double leadTimeDemand) {
        return leadTimeDemand / (leadTime / 12);
    }

    public double calculateInitialOrderQuantity(double setupCostPerOrder, double annualDemand, double holdingCost) {
        return Math.sqrt((2 * setupCostPerOrder * annualDemand) / holdingCost);
    }

    public double calculateInitialReorderPoint(double leadTimeDemand, double leadTimeStandardDeviation, double initialOrderQuantity, double holdingCost, double annualDemand, double unitCost) {
        double z = getZValueForFR(calculateProportionOfOrderCyclesWithoutStockOut(initialOrderQuantity, holdingCost, annualDemand, unitCost));
        return leadTimeDemand + (z * leadTimeStandardDeviation);
    }

    public double calculateDemandDuringLeadTime(double leadTimeStandardDeviation, double initialOrderQuantity, double holdingCost, double annualDemand, double unitCost) {
        double lZ = getLZValueForFR(calculateProportionOfOrderCyclesWithoutStockOut(initialOrderQuantity, holdingCost, annualDemand, unitCost));
        return leadTimeStandardDeviation * lZ;
    }

    public double calculateProportionOfOrderCyclesWithoutStockOut(double initialOrderQuantity, double holdingCost, double annualDemand, double unitCost) {
        return 1 - ((initialOrderQuantity * holdingCost) / (unitCost * annualDemand));
    }

    public double calculateNewOrderQuantity(double setupCostPerOrder, double annualDemand, double holdingCost, double unitCost, double demandDuringLeadTime) {
        return Math.sqrt((2 * annualDemand * (setupCostPerOrder + (unitCost * demandDuringLeadTime))) / holdingCost);
    }

    public double[] calculateOptimalOrderQuantityAndReorderPoint(double initialOrderQuantity, double initialReorderPoint, double leadTimeDemand, double leadTimeStandardDeviation, double setupCostPerOrder, double annualDemand, double holdingCost, double unitCost) {
        double previousReorderPoint;
        double currentReorderPoint = initialReorderPoint;
        int iterationCount = 0;

        do {
            initialOrderQuantity = calculateNewOrderQuantity(setupCostPerOrder, annualDemand, holdingCost, unitCost, calculateDemandDuringLeadTime(leadTimeStandardDeviation, initialOrderQuantity, holdingCost, annualDemand, unitCost));
            double newReorderPoint = calculateInitialReorderPoint(leadTimeDemand, leadTimeStandardDeviation, initialOrderQuantity, holdingCost, annualDemand, unitCost);
            previousReorderPoint = currentReorderPoint;
            currentReorderPoint = newReorderPoint;
            iterationCount++;
        } while (Math.abs(currentReorderPoint - previousReorderPoint) > 0.01);

        return new double[]{iterationCount, initialOrderQuantity, currentReorderPoint};
    }
}