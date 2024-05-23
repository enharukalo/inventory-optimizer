package edu.estu;

/**
 * Class to perform inventory calculations.
 */
public class InventoryCalculator {
    private final ZChartLoader zChartLoader;

    public InventoryCalculator(ZChartLoader zChartLoader) {
        this.zChartLoader = zChartLoader;
    }

    public double calculateHoldingCost(double interestRate, double unitCost) {
            return interestRate * unitCost;
    }

    public double calculateAnnualDemand(double leadTime, double leadTimeDemand) {
        return leadTimeDemand / (leadTime / 12);
    }

    public double calculateInitialOrderQuantity(double orderingCost, double annualDemand, double holdingCost) {
        return Math.sqrt((2 * orderingCost * annualDemand) / holdingCost);
    }

    public double calculateInitialReorderPoint(double leadTimeDemand, double leadTimeStandardDeviation, double initialOrderQuantity, double holdingCost, double annualDemand, double penaltyCost) {
        double z = zChartLoader.getZValueForFR(calculateProportionOfOrderCyclesWithoutStockOut(initialOrderQuantity, holdingCost, annualDemand, penaltyCost));
        return leadTimeDemand + (z * leadTimeStandardDeviation);
    }

    public double calculateDemandDuringLeadTime(double leadTimeStandardDeviation, double initialOrderQuantity, double holdingCost, double annualDemand, double penaltyCost) {
        double lZ = zChartLoader.getLZValueForFR(calculateProportionOfOrderCyclesWithoutStockOut(initialOrderQuantity, holdingCost, annualDemand, penaltyCost));
        return leadTimeStandardDeviation * lZ;
    }

    public double calculateProportionOfOrderCyclesWithoutStockOut(double initialOrderQuantity, double holdingCost, double annualDemand, double penaltyCost) {
        return 1 - ((initialOrderQuantity * holdingCost) / (penaltyCost * annualDemand));
    }

    public double calculateNewOrderQuantity(double orderingCost, double annualDemand, double holdingCost, double penaltyCost, double demandDuringLeadTime) {
        return Math.sqrt((2 * annualDemand * (orderingCost + (penaltyCost * demandDuringLeadTime))) / holdingCost);
    }

    public double[] calculateOptimalOrderQuantityAndReorderPoint(double initialOrderQuantity, double initialReorderPoint, double leadTimeDemand, double leadTimeStandardDeviation, double orderingCost, double annualDemand, double holdingCost, double penaltyCost) {
        double previousReorderPoint;
        double currentReorderPoint = initialReorderPoint;

        do {
            initialOrderQuantity = calculateNewOrderQuantity(orderingCost, annualDemand, holdingCost, penaltyCost, calculateDemandDuringLeadTime(leadTimeStandardDeviation, initialOrderQuantity, holdingCost, annualDemand, penaltyCost));
            double newReorderPoint = calculateInitialReorderPoint(leadTimeDemand, leadTimeStandardDeviation, initialOrderQuantity, holdingCost, annualDemand, penaltyCost);
            previousReorderPoint = currentReorderPoint;
            currentReorderPoint = newReorderPoint;
        } while (Math.abs(currentReorderPoint - previousReorderPoint) > 0.01);

        return new double[]{initialOrderQuantity, currentReorderPoint};
    }
}