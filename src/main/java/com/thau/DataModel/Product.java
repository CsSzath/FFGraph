package com.thau.DataModel;

public class Product {

private String name;
private String companyName;
private String machineId;
private String dateTime; 

public Product(String name, String companyName, String machineId, int date, double time) {
    this.name = name;
    this.companyName = companyName;
    this.machineId = machineId;
    java.time.LocalDateTime dateTime = java.time.LocalDateTime.of(
        java.time.LocalDate.of(1900, 1, 1).plusDays(date), 
        java.time.LocalTime.ofSecondOfDay((long) (time * 60 * 60 * 24)));
    this.dateTime = dateTime.toString();
}

public String getName() {
    return name;
}
public String getCompanyName() {
    return companyName;
}
public String getMachineId() {
    return machineId;
}
public String getDateTime() {
    return dateTime;
}
public String toString() {
    return "Product{" +
            "name='" + name + '\'' +
            ", companyName='" + companyName + '\'' +
            ", machineId='" + machineId + '\'' +
            ", dateTime='" + dateTime + '\'' +
            '}';
}

}
