package com.skm.apachepoi.client;

import com.inductiveautomation.ignition.common.Dataset;
import com.skm.apachepoi.AbstractScriptModule;
import com.inductiveautomation.ignition.client.gateway_interface.ModuleRPCFactory;
import com.skm.apachepoi.ExcelFunctionsBlackBox;

import java.util.Date;

public class ClientScriptModule extends AbstractScriptModule {

  private final ExcelFunctionsBlackBox rpc;

  public ClientScriptModule() {
    rpc = ModuleRPCFactory.create(
        "com.skm.apachepoi",
        ExcelFunctionsBlackBox.class
    );
  }

  @Override
  protected int multiplyImpl(int arg0, int arg1) {
    return rpc.multiply(arg0, arg1);
  }

  @Override
  protected int addImpl(int arg0, int arg1) {
    return rpc.add(arg0, arg1); }

  @Override
  protected int subImpl(int arg0, int arg1) {
    return rpc.sub(arg0, arg1); }

  @Override
  protected int divideImpl(int arg0, int arg1) {
    return rpc.divide(arg0, arg1); }

  @Override
  protected byte[] _getParkMeadowsSheet(Date reportDate, Dataset turbidityData, Dataset Hours, Dataset WQData) throws Exception {
    return rpc.getParkMeadowsSheet(reportDate, turbidityData, Hours, WQData);
  }

  @Override
  protected byte[] _getCreekside(Date reportDate, Dataset fiveMinData, Dataset turbData, Dataset hours) throws Exception {
    return rpc.getCreekside(reportDate, fiveMinData, turbData, hours);
  }

  @Override
  protected byte[] _getJSSD(Date reportDate, Dataset fiveMinData, Dataset turbData, Dataset hours) throws Exception {
    return rpc.getJSSD(reportDate, fiveMinData, turbData, hours);
  }

  @Override
  protected byte[] _getCreeksideUVDaily(Date reportDate, Dataset runHours, Dataset totalProd, Dataset redData, Dataset offSpecData) throws Exception {
    return rpc.getCreeksideUVDaily(reportDate, runHours, totalProd, redData, offSpecData);
  }

  @Override
  protected byte[] _getCreeksideUVMonthly(Date reportDate, Dataset runHours, Dataset totalProd, Dataset offSpecData) throws Exception {
    return rpc.getCreeksideUVMonthly(reportDate, runHours, totalProd, offSpecData);
  }

  @Override
  protected byte[] _getCreeksideUVOffspec(Date reportDate, Dataset offSpecData) throws Exception {
    return rpc.getCreeksideUVOffSpec(reportDate, offSpecData);
  }

  @Override
  protected byte[] _getQuinnsSheetMnO2(Date reportDate, Dataset FiveMinData, Dataset rackResults, Dataset WQData) throws Exception {
    return rpc.getQuinnsSheetMnO2(reportDate, FiveMinData, rackResults, WQData);
  }

  @Override
  protected byte[] _getQuinnsSheet(Date reportDate, Dataset FiveMinData, Dataset rackResults, Dataset WQData) throws Exception {
    return rpc.getQuinnsSheet(reportDate, FiveMinData, rackResults, WQData);
  }

  @Override
  protected byte[] _getGroundWaterDisinfection(Date reportDate, Dataset groundWaterData, Dataset hypoSpeed) throws Exception {
    return rpc.getGroundWaterDisinfection(reportDate, groundWaterData, hypoSpeed);
  }

  @Override
  protected byte[] _getGroundWaterDisinfectionNoPM(Date reportDate, Dataset groundWaterData, Dataset hypoSpeed) throws Exception {
    return rpc.getGroundWaterDisinfectionNoPM(reportDate, groundWaterData, hypoSpeed);
  }

  @Override
  protected byte[] _getQuinnsFlows(Date reportDate, Dataset SewerFlows) throws Exception {
    return rpc.getQuinnsFlows(reportDate, SewerFlows);
  }

  @Override
  protected byte[] _getOgdensSheet(
      Date reportDate, Dataset FiveMinData, Dataset WQData, Dataset turbidity, Dataset rackResults, Dataset rack2Results, Dataset rack3Results, Dataset rack4Results, Dataset rack5Results, Dataset rack6Results, Dataset rack7Results, Dataset rack8Results,
      Dataset rack9Results) throws Exception {
    return rpc.getOgdensSheet(reportDate, FiveMinData, WQData, turbidity, rackResults, rack2Results, rack3Results, rack4Results, rack5Results, rack6Results, rack7Results, rack8Results, rack9Results);
  }

  @Override
  protected byte[] _getQuinnsMonitoringData(Date reportDate, Dataset production, Dataset rackResults, Dataset chemicals) throws Exception {
    return rpc.getQuinnsMonitoring(reportDate, production, rackResults, chemicals);
  }

  @Override
  protected byte[] _getMembraneReport(Date reportDate, Dataset production, Dataset rackResults, Dataset IT_data) throws Exception {
    return rpc.getMembraneReport(reportDate, production, rackResults, IT_data);
  }

}
