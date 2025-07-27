# mapfintech_exercise

# Java & PHP Sales Dashboard  

## Project Overview  
This project was built to help **Manufacturer X** track how sales are performing each quarter for three products across four continents.  

The work covers the core requirements and also explores some optional features:  

- **Sales data stored in MySQL** with a proper relational schema.  
- **Java program** that connects to the database, retrieves sales data, and exports it into a clean JSON file.  
- *(Optional)* A **PHP dashboard** that reads the JSON file and displays average quarterly sales on a chart.  
- *(Optional)* A small **REST API** using SparkJava so that PHP can fetch live data directly instead of relying only on the JSON file.  

## Project Structure  
- **`/src`** – Java source files (JDBC code for exporting sales, plus the REST API server).  
- **`/lib`** – External libraries (MySQL Connector, Gson, SparkJava).  
- **`/sales_data.json`** – JSON export created by the Java program.  
- **`/index.php`** – PHP dashboard to view and filter sales data.  
- **`mapfintech_exercise.sql`** – MySQL database dump (schema + data).  

## How to Set Up  

### **Import the Database**  
1. Open a terminal (or MySQL Workbench).  
2. Run:  
   ```bash
   mysql -u root -p
   CREATE DATABASE mapfintech_exercise;
   exit;
