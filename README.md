The purpose of the app is to graphically evaluate and store the exported csv heat-treatment files of an industrial smoker machine made by KORAX Gépgyár Kft. 

The app should be able to import and atore the content of the csv files exported by the Control PLC of the machine.
These export files contain the most important data about the heat treatment process:
  - name of the product
Every record consists of:
  - time of the heat treatment and creation of the record by date (YY/MM/DD) and time (HH:MM:SS)
  - Air temperature inside the machine
  - setAir temperature
  - Core temperature of the product
  - set Core temperature
  - Humidity inside the chamber
  - set Humidity
  - type of the process step (nothing - Drying - Cooking - Smoking - Keep temperature - cleaning - shower - waiting)
  - elapsed process time in (ms)

According to law this data should be evaluated by the user to prove, that the product got the necessary heat treatment 
and the necessary bacteria-killing numbers were reached. Also according to law, these process records has to be kept as proof 
and if required to be shown to the officials.

