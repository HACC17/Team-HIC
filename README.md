## Our Goal

To create an online application that makes it easy for the public to view OHA's grant data.

## How We Achieved Our Goal

- By looking at the table and charts, the public can **find answers to questions** such as "How much money did the University of Hawaii receive from 2013 to 2016?" and "How many Native Hawaiians on the island of Oahu were served by grants in 2015?"
- Organizations can **make business decisions** based on the data. For example, those that are applying for grants that will help improve the quality of education for Native Hawaiians can look at the data for the previous year and see which location needs more funding in the upcoming year.
- By making grant data available on [Hawaii's Open Data Portal](http://data.hawaii.gov), we are helping the State of Hawaii **achieve its goal of government transparency**. After the data is published, it can be used by other developers who also want to help the state government achieve this goal.

## Features

- Apply multiple filters to the grant data in the table and charts
- Export filtered or unfiltered grant data in the table to a CSV, Microsoft Excel, or PDF file
- Switch between bar and pie charts
- Choose the data to display in all the charts
- Drilldown in a bar or pie chart to view more detailed data
- Import grant data from a CSV or Microsoft Excel file
- Upload grant data to [Hawaii's Open Data Portal](http://data.hawaii.gov) manually or automatically every day at midnight

## Quick Start Guide

Go to [test-ohagrants.ehawaii.gov](http://test-ohagrants.ehawaii.gov).

- Select one or more filters in the left panel to filter out all the grants that **do not** meet the selected conditions.
- Under **Chart Settings**, pick the chart type and then the data to display in the charts: amount, total number of people served, or number of Native Hawaiians served.
- Click on a bar or pie slice to open a drilldown chart and view more detailed data. Choose the data to display in the drilldown chart from the dropdown menu below the chart.
- Administrators can add a new grant, import grant data from a CSV or Microsoft Excel file, and also upload grant data to [Hawaii's Open Data Portal](http://data.hawaii.gov).

## For Developers

If you are interested in helping us develop the application, please make sure you have
the following installed on your development machine first:

- **Java 8**
- **Gradle 4.0**
- **MySQL 5.7**
- **Tomcat 8**

On the command line, execute the command `gradle eclipse` to download and install all
the libraries required to develop the application. The command will also generate the
`.classpath` and `.project` files so you will be able to import the application as an
[Eclipse](https://eclipse.org) project.

OHA Grants is a very modern, robust application that uses **Spring 4.3.11** on the backend, and **Bootstrap**, **jQuery**, and **Highcharts** on the frontend.

### Grant Data 

Both files below contain the same grant data. Either can be used to populate the database on your development machine.

- [CSV File](http://www.bjpeterdelacruz.com/files/2013_2016_data.csv) (MD5: f56ab7f4cb648539cff806f913c3c821)
- [Microsoft Excel File](http://www.bjpeterdelacruz.com/files/2013_2016_data.xlsx) (MD5: edf383e8a9df1fa7d1644e11f7353014)

## Team Members

- BJ Peter DeLaCruz (Team Captain)
- Christopher Cosner
- Zheng Fang
- Megan Nichols