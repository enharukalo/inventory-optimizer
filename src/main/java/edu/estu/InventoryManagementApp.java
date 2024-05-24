package edu.estu;

/**
 * Main class to run the application.
 */
public class InventoryManagementApp {
    public static void main(String[] args) {
        ZChartLoader zChartLoader = new ZChartLoader();
        InventoryCalculator calculator = new InventoryCalculator(zChartLoader);

        // Test problem data
        double unitCost = 20; // C
        double orderingCost = 100; // K
        double penaltyCost = 20; // p
        double interestRate = 0.25; // I
        double leadTime = 4; // 4 months = 0.3 year
        double leadTimeDemand = 500;
        double leadTimeStandardDeviation = 100;

        // Calculate holding cost and annual demand
        double holdingCost = calculator.calculateHoldingCost(unitCost, interestRate);
        double annualDemand = calculator.calculateAnnualDemand(leadTime, leadTimeDemand);

        // Calculate initial order quantity and reorder point
        double orderQuantity = calculator.calculateInitialOrderQuantity(orderingCost, annualDemand, holdingCost);
        double reorderPoint = calculator.calculateInitialReorderPoint(leadTimeDemand, leadTimeStandardDeviation, orderQuantity, holdingCost, annualDemand, penaltyCost);

        // Iteratively calculate optimal order quantity and reorder point
        // until the difference between the current and previous reorder points is less than 0.01
        double[] results = calculator.calculateOptimalOrderQuantityAndReorderPoint(orderQuantity, reorderPoint, leadTimeDemand, leadTimeStandardDeviation, orderingCost, annualDemand, holdingCost, penaltyCost);
        orderQuantity = results[0];
        reorderPoint = results[1];

        // Print the results
        // Math.round is used to round the values to the nearest integer
        // String.format is used to format the values
        System.out.println("Q, R = (" + Math.round(orderQuantity) + ", " + Math.round(reorderPoint) + ")");
        System.out.println("Safety Cost: " + Math.round(Math.round(reorderPoint) - leadTimeDemand));

        double averageAnnualHoldingCost = holdingCost * ((double) Math.round(orderQuantity) / 2 + Math.round(reorderPoint) - leadTimeDemand);
        double averageAnnualOrderingCost = (orderingCost * annualDemand) / Math.round(orderQuantity);
        double averageAnnualPenaltyCost = (penaltyCost * annualDemand * calculator.calculateDemandDuringLeadTime(leadTimeStandardDeviation, orderQuantity, holdingCost, annualDemand, penaltyCost)) / Math.round(orderQuantity);

        System.out.println("Holding Cost: " + String.format("%.0f", averageAnnualHoldingCost));
        System.out.println("Ordering Cost: " + String.format("%.2f", averageAnnualOrderingCost));
        System.out.println("Penalty Cost: " + String.format("%.4f", averageAnnualPenaltyCost));

        double averageTimeBetweenOrders = (Math.round(orderQuantity) / annualDemand) * 12;
        System.out.println("Average Time Between Orders: " + String.format("%.3f", averageTimeBetweenOrders) + " months");

        double proportionOfOrderCyclesWithoutStockOut = calculator.calculateProportionOfOrderCyclesWithoutStockOut(orderQuantity, holdingCost, annualDemand, penaltyCost);
        System.out.println("The Probability of Not Stocking Out During Lead Time: " + String.format("%.4f", proportionOfOrderCyclesWithoutStockOut));

        double proportionOfUnmetDemand = calculator.calculateDemandDuringLeadTime(leadTimeStandardDeviation, orderQuantity, holdingCost, annualDemand, penaltyCost) / orderQuantity;
        System.out.println("The Proportion of Demand Not Met from On Hand Stock in Each Cycle: " + String.format("%.5f", proportionOfUnmetDemand));
    }
}