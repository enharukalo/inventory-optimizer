# Inventory Optimizer

This project involves developing a software that determines the optimal lot size (Q) and reorder point (R) for a given data. It's a part of ENM320 Production and Operations Planning II & BIM214 Computer Programming IV Term Project.

## Features

The software calculates the following decision variables and performance measures:

- Optimal lot size and reorder point
- The number of iterations the software run to obtain the optimal lot size and reorder point
- The safety stock
- Average annual holding, setup and penalty costs
- Average time between the placement of orders
- The proportion of order cycles in which no stock out occurs
- The proportion of demands that are not met

## Installation

```bash
  git clone
  mvn clean install  
```

## Usage

```bash
  java -jar target/inventory-optimizer.jar
```

## Test Problem

Test the software with the following problem.

Harvey’s Specialty Shop sells a popular mustard. The mustard costs Harvey $20 a jar. Replenishment lead time is 4 months. Harvey uses a 25 percent annual interest rate and estimates the loss of goodwill cost as $20 per jar in case of stockout. Bookkeeping expenses for placing an order is $100. During the four-month replenishment period, Harvey estimates that he sells an average of 500 jars. and the standard deviation of demand is 100.  Assume that demand is described by a normal distribution. How should Harvey control the replenishment of the mustard?