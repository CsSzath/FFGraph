# FFGraph - Heat Treatment Data Evaluation and Storage

## Introduction
FFGraph is an application designed to graphically evaluate and store the exported CSV heat-treatment files of an industrial smoker chambers made by KORAX Gépgyár Kft. The app ensures compliance with legal requirements by allowing users to analyze and store critical heat treatment data.

## Features
- Import and store CSV files exported by the machine's Control PLC.
- Graphically evaluate heat treatment data, including:
  - Air temperature
  - Core temperature
  - Humidity
  - Process steps
- Ensure compliance with legal requirements for data storage and evaluation.

## Data Structure
Each record in the CSV file contains:
- **#**: <int> index
- **Date**: <int> converted to YY/MM/DD
- **Time**: <float> converted to HH:MM:SS
- **Milliseconds**: milliseconds in TimeOfDay
- **Day of Week**: <int>
- **Interval Time ms**: <int> time since process started
- **Sub Interval Time ms**: <int> time since last record saved
- **Triggered**: <int> 0 - if record was saved automatically (time interval), 1 - if the save event was triggiered by program
- **Kamra hőm. Values**: <int> Air temperature inside the machine [00.0°C]
- **Mag hőm. Value**: <int> Core temperature of the product [00.0°C]
- **Pára**: <int> Inside the chamber [%]
- **Lépés típus Values**: <int> (e.g., Drying, Cooking, Smoking, etc.)
- **Kamra beáll. Values**: <int> Set air temperature in the treatment program [00°C]
- **Mag beáll. Values**: <int> Set core temperature in the treatment program [00°C]
- **Pára beáll. Values**: <int> Set Humidity in the treatment program [%]

## Legal Compliance
According to regulations, the heat treatment data must:
- Prove that the product received the necessary heat treatment.
- Demonstrate that the required bacteria-killing thresholds were met.
- Be stored and made available for inspection by officials if required.
- Using the software, maintaining the integrity of the database and providing non-falsified data for the program are sole responsibilities of the user!

## Example CSV 
Data Sampler1
Fst.gyp.kötözött
Szárítás
#,Date,Time,Milliseconds,Day of week,Interval Time ms,Sub Interval Time ms,Triggered,Kamra Hőm. Values,Mag Hőm Values,Pára Values,Lépés típus Values,Kamra beáll. Values,Mag beáll. Values,Pára beáll. Values,
1,44277,0.35125,0,2,60004,60000,0,316,80,92,1,80,0,0,

