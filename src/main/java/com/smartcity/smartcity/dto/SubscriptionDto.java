// SubscriptionDto.java
package com.smartcity.smartcity.dto;

public class SubscriptionDto {
    private String serviceType;
    private double amountPaid;

    public String getServiceType() { return serviceType; }
    public void setServiceType(String serviceType) { this.serviceType = serviceType; }
    public double getAmountPaid() { return amountPaid; }
    public void setAmountPaid(double amountPaid) { this.amountPaid = amountPaid; }
}