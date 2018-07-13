package com.example.ibwev.testprint;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by ibwev on 9/26/2017.
 */

public class Transaction implements Comparable<Transaction> {

    GregorianCalendar datePaid, dateRentTo;
    int amountPaid, owedToRentTo, payID, rentCollected, depositCollected,lateCollected, otherCollected, drawAmount;
    String description, transactionType, address, collectorFirstName;
    SimpleDateFormat dateFormat;
    boolean isPrePay, isChecked, isAutoDeposit;

    public Transaction(GregorianCalendar datePaid, int amountPaid, String description, GregorianCalendar rentToDate, int owedToRentTo, String transactionType){
        this.datePaid = datePaid;
        this.amountPaid = amountPaid;
        this.description = description;
        this.dateRentTo = rentToDate;
        this.owedToRentTo = owedToRentTo;
        this.transactionType = transactionType;
        isPrePay = false;

        dateFormat = new SimpleDateFormat("MMM dd, yyyy");
    }

    public Transaction(GregorianCalendar datePaid, int amountPaid, String description, String transactionType){
        this.datePaid = datePaid;
        this.amountPaid = amountPaid;
        this.description = description;
        this.transactionType = transactionType;
        this.isPrePay = true;

        dateFormat = new SimpleDateFormat("MMM dd, yyyy");
    }



    public Transaction(int payID, String address, GregorianCalendar datePaid, int rentCollected, int depositCollected, int lateCollected, int otherCollected, String collectorFirstName, boolean isAutoDeposit ){
        this.payID = payID;
        this.address = address;
        this.datePaid = datePaid;
        this.rentCollected = rentCollected;
        this.depositCollected = depositCollected;
        this.lateCollected = lateCollected;
        this.otherCollected = otherCollected;
        this.collectorFirstName = collectorFirstName;
        this.isAutoDeposit = isAutoDeposit;
    }

    public Transaction(String drawDescription, int drawAmount){
        this.description = drawDescription;
        this.drawAmount = drawAmount;

    }

    public boolean isAutoDeposit() {
        return isAutoDeposit;
    }

    public void setAutoDeposit(boolean autoDeposit) {
        isAutoDeposit = autoDeposit;
    }

    public int getDrawAmount() {
        return drawAmount;
    }

    public void setDrawAmount(int drawAmount) {
        this.drawAmount = drawAmount;
    }

    public int totalCollected(){
        int total = rentCollected + depositCollected + lateCollected + otherCollected;
        return total;
    }

    public String getCollectorFirstName() {
        return collectorFirstName;
    }

    public void setCollectorFirstName(String collectorFirstName) {
        this.collectorFirstName = collectorFirstName;
    }

    public int getPayID() {
        return payID;
    }

    public void setPayID(int payID) {
        this.payID = payID;
    }

    public int getRentCollected() {
        return rentCollected;
    }

    public void setRentCollected(int rentCollected) {
        this.rentCollected = rentCollected;
    }

    public int getDepositCollected() {
        return depositCollected;
    }

    public void setDepositCollected(int depositCollected) {
        this.depositCollected = depositCollected;
    }

    public int getLateCollected() {
        return lateCollected;
    }

    public void setLateCollected(int lateCollected) {
        this.lateCollected = lateCollected;
    }

    public int getOtherCollected() {
        return otherCollected;
    }

    public void setOtherCollected(int otherCollected) {
        this.otherCollected = otherCollected;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public SimpleDateFormat getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(SimpleDateFormat dateFormat) {
        this.dateFormat = dateFormat;
    }

    public boolean isPrePay() {
        return isPrePay;
    }

    public void setPrePay(boolean prePay) {
        isPrePay = prePay;
    }

    public boolean getPrePay(){
        return isPrePay;
    }

    public GregorianCalendar getDatePaid() {
        return datePaid;
    }

    public void setDatePaid(GregorianCalendar datePaid) {
        this.datePaid = datePaid;
    }

    public GregorianCalendar getDateRentTo() {
        return dateRentTo;
    }

    public void setDateRentTo(GregorianCalendar dateRentTo) {
        this.dateRentTo = dateRentTo;
    }

    public int getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(int amountPaid) {
        this.amountPaid = amountPaid;
    }

    public int getOwedToRentTo() {
        return owedToRentTo;
    }

    public void setOwedToRentTo(int owedToRentTo) {
        this.owedToRentTo = owedToRentTo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public int compareTo(Transaction o) {
        int oYear = o.getDatePaid().get(Calendar.YEAR);
        int oMonth = o.getDatePaid().get(Calendar.MONTH);
        int oDayOfMonth = o.getDatePaid().get(Calendar.DAY_OF_MONTH);

        int cYear = getDatePaid().get(Calendar.YEAR);
        int cMonth = getDatePaid().get(Calendar.MONTH);
        int cDayOfMonth = getDatePaid().get(Calendar.DAY_OF_MONTH);

        GregorianCalendar gco = new GregorianCalendar(oYear, oMonth, oDayOfMonth);
        GregorianCalendar gcc = new GregorianCalendar(cYear, cMonth, cDayOfMonth);

        return -1*(gco.compareTo(gcc)); //-1 is to get most recent date at top of ListView

    }

    public String stringDatePaid(){

        return dateFormat.format(datePaid.getTime());
    }

    public void setChecked(Boolean checked) {
        isChecked = checked;
    }

    public String stringRentToDate(){
        return  dateFormat.format(dateRentTo.getTime());
    }
}

