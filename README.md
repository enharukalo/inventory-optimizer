# Inventory Optimizer

Inventory Optimizer is a software solution developed as part of the ENM320 Production and Operations Planning II & BIM214 Computer Programming IV Term Project. The software determines the optimal lot size (Q) and reorder point (R) for a given data set, providing valuable insights for inventory management.

## Features

The software calculates and provides the following decision variables and performance measures:

- Optimal lot size and reorder point
- The number of iterations the software run to obtain the optimal lot size and reorder point
- The safety stock
- Average annual holding, setup and penalty costs
- Average time between the placement of orders
- The proportion of order cycles in which no stock out occurs
- The proportion of demands that are not met

## Installation

To install the Inventory Optimizer, follow these steps:

```bash
git clone https://github.com/enharukalo/inventory-optimizer.git
cd inventory-optimizer
mvn clean install
```

## Usage

To use the Inventory Optimizer, navigate to the target directory and run the jar file:

```bash
  cd target
  java -jar inventory-optimizer.jar
```

## Test Problem

Test the software with the following problem.

Harvey’s Specialty Shop sells a popular mustard. The mustard costs Harvey $20 a jar. Replenishment lead time is 4 months. Harvey uses a 25 percent annual interest rate and estimates the loss of goodwill cost as $20 per jar in case of stockout. Bookkeeping expenses for placing an order is $100. During the four-month replenishment period, Harvey estimates that he sells an average of 500 jars. and the standard deviation of demand is 100.  Assume that demand is described by a normal distribution. How should Harvey control the replenishment of the mustard?

## Contributing

Contributions to the Inventory Optimizer are welcome. 

## License

This project is licensed under the terms of the MIT license. See the [LICENSE](LICENSE) file for details.