package edu.estu;

import java.text.DecimalFormat;
import java.util.Scanner;

/**
 * Main class to run the application.
 */
public class InventoryManagementApp {
    public static void main(String[] args) {
        DecimalFormat twoDecimalFormat = new DecimalFormat("###.##");
        DecimalFormat fiveDecimalFormat = new DecimalFormat("#.#####");

        InventoryCalculator calculator = new InventoryCalculator();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Please enter the costs (unit cost, ordering cost, penalty cost) and interest rate with space between each number: ");
        double unitCost = scanner.nextDouble();
        double orderingCost = scanner.nextDouble();
        double penaltyCost = scanner.nextDouble();
        double interestRate = scanner.nextDouble();

        System.out.println("Please enter the lead time, lead time demand and lead time standard deviation: ");
        double leadTime = scanner.nextDouble();
        double leadTimeDemand = scanner.nextDouble();
        double leadTimeStandardDeviation = scanner.nextDouble();

        double holdingCost = calculator.calculateHoldingCost(unitCost, interestRate);
        double annualDemand = calculator.calculateAnnualDemand(leadTime, leadTimeDemand);
        double orderQuantity = calculator.calculateInitialOrderQuantity(orderingCost, annualDemand, holdingCost);
        double reorderPoint = calculator.calculateInitialReorderPoint(leadTimeDemand, leadTimeStandardDeviation, orderQuantity, holdingCost, annualDemand, unitCost);

        double[] results = calculator.calculateOptimalOrderQuantityAndReorderPoint(orderQuantity, reorderPoint, leadTimeDemand, leadTimeStandardDeviation, orderingCost, annualDemand, holdingCost, unitCost);
        int iterationCount = (int) results[0];
        orderQuantity = results[1];
        reorderPoint = results[2];

        System.out.println("Q = " + Math.round(orderQuantity));
        System.out.println("R = " + Math.round(reorderPoint));
        System.out.println("Number of iterations the software run to obtain the optimal lot size and reorder point: " + iterationCount);

        double safetyStock = Math.round(Math.round(reorderPoint) - leadTimeDemand);
        System.out.println("The safety stock: " + safetyStock);

        double averageAnnualHoldingCost = holdingCost * (Math.round(orderQuantity) / 2 + Math.round(reorderPoint) - leadTimeDemand);
        double averageAnnualOrderingCost = (orderingCost * annualDemand) / Math.round(orderQuantity);
        double averageAnnualPenaltyCost = (unitCost * annualDemand * calculator.calculateDemandDuringLeadTime(leadTimeStandardDeviation, orderQuantity, holdingCost, annualDemand, unitCost)) / Math.round(orderQuantity);

        System.out.println("Average annual holding, setup and penalty costs are: " + twoDecimalFormat.format(averageAnnualHoldingCost) + ", " + twoDecimalFormat.format(averageAnnualOrderingCost) + ", " + twoDecimalFormat.format(averageAnnualPenaltyCost));

        double averageTimeBetweenOrders = (Math.round(orderQuantity) / annualDemand) * 12;
        System.out.println("Average time between the placement of orders: " + twoDecimalFormat.format(averageTimeBetweenOrders) + " months");

        double proportionOfOrderCyclesWithoutStockOut = calculator.calculateProportionOfOrderCyclesWithoutStockOut(orderQuantity, holdingCost, annualDemand, unitCost);
        System.out.println("The proportion of order cycles in which no stock out occurs: " + twoDecimalFormat.format(proportionOfOrderCyclesWithoutStockOut * 100) + "%");

        double proportionOfUnmetDemand = calculator.calculateDemandDuringLeadTime(leadTimeStandardDeviation, orderQuantity, holdingCost, annualDemand, unitCost) / orderQuantity;
        System.out.println("The proportion of demands that are not met: " + fiveDecimalFormat.format(proportionOfUnmetDemand));

        scanner.close();
    }
}