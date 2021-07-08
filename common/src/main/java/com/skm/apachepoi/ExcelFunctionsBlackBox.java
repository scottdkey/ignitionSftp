package com.skm.apachepoi;

import com.inductiveautomation.ignition.common.Dataset;

import java.util.Date;

public interface ExcelFunctionsBlackBox {
  int multiply(int arg0, int arg1);

  int sub(int arg0, int arg1);

  int add(int arg0, int arg1);

  int divide(int arg0, int arg1);

  byte[] getParkMeadowsSheet(Date reportDate, Dataset turbidityData, Dataset Hours, Dataset WQData) throws Exception;


  byte[] getCreekside(Date reportDate, Dataset fiveMinData, Dataset turbData, Dataset hours) throws Exception;


  byte[] getJSSD(Date reportDate, Dataset fiveMinData, Dataset turbData, Dataset hours) throws Exception;


  byte[] getCreeksideUVDaily(Date reportDate, Dataset runHours, Dataset totalProd, Dataset redData, Dataset offSpecData) throws Exception;


  byte[] getCreeksideUVMonthly(Date reportDate, Dataset runHours, Dataset totalProd, Dataset offSpecData) throws Exception;


  byte[] getCreeksideUVOffSpec(Date reportDate, Dataset offSpecData) throws Exception;


  byte[] getQuinnsSheetMnO2(Date reportDate, Dataset FiveMinData, Dataset rackResults, Dataset WQData) throws Exception;


  byte[] getQuinnsSheet(Date reportDate, Dataset FiveMinData, Dataset rackResults, Dataset WQData) throws Exception;


  byte[] getQuinnsFlows(Date reportDate, Dataset SewerFlows) throws Exception;


  byte[] getGroundWaterDisinfection(Date reportDate, Dataset groundWaterData, Dataset hypoSpeed) throws Exception;


  byte[] getGroundWaterDisinfectionNoPM(Date reportDate, Dataset groundWaterData, Dataset hypoSpeed) throws Exception;


  byte[] getOgdensSheet(Date reportDate, Dataset FiveMinData, Dataset rackResults, Dataset WQData, Dataset turbidity, Dataset rack2Results, Dataset rack3Results, Dataset rack4Results, Dataset rack5Results, Dataset rack6Results, Dataset rack7Results, Dataset rack8Results, Dataset rack9Results) throws Exception;


  byte[] getQuinnsMonitoring(Date reportDate, Dataset production, Dataset rackResults, Dataset chemicals) throws Exception;


  byte[] getMembraneReport(Date reportDate, Dataset production, Dataset rackResults, Dataset IT_data) throws Exception;


}
