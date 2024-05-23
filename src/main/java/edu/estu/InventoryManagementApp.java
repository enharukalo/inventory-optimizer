package edu.estu;

/**
 * Main class to run the application.
 */
public class InventoryManagementApp {
    public static void main(String[] args) {
        ZChartLoader zChartLoader = new ZChartLoader();
        InventoryCalculator calculator = new InventoryCalculator(zChartLoader);

        // Test problem
        double unitCost = 20; // C
        double orderingCost = 100; // K
        double penaltyCost = 20; // p
        double interestRate = 0.25; // I
        double leadTime = 4; // 4 months = 0.3 year
        double leadTimeDemand = 500;
        double leadTimeStandardDeviation = 100;

        double holdingCost = calculator.calculateHoldingCost(unitCost, interestRate); // h
        double annualDemand = calculator.calculateAnnualDemand(leadTime, leadTimeDemand); // lambda
        double orderQuantity = calculator.calculateInitialOrderQuantity(orderingCost, annualDemand, holdingCost); // Q
        double reorderPoint = calculator.calculateInitialReorderPoint(leadTimeDemand, leadTimeStandardDeviation, orderQuantity, holdingCost, annualDemand, penaltyCost); // R
        double[] results = calculator.calculateOptimalOrderQuantityAndReorderPoint(orderQuantity, reorderPoint, leadTimeDemand, leadTimeStandardDeviation, orderingCost, annualDemand, holdingCost, penaltyCost);
        orderQuantity = results[0];
        reorderPoint = results[1];

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