package com.skm.apachepoi.gateway;

import com.inductiveautomation.ignition.common.Dataset;
import com.skm.apachepoi.AbstractScriptModule;
import com.skm.apachepoi.ExcelFunctions;
import com.skm.apachepoi.ObjectDatasetWrapper;

import java.util.Date;

public class GatewayScriptModule extends AbstractScriptModule {
  //test functions to work as a baseline
  @Override
  protected int multiplyImpl(int arg0, int arg1) {
    return arg0 * arg1;
  }

  @Override
  protected int addImpl(int arg0, int arg1) {
    return arg0 + arg1;
  }

  @Override
  protected int subImpl(int arg0, int arg1) {
    return arg0 - arg1;
  }

  @Override
  protected int divideImpl(int arg0, int arg1) {
    return arg0 / arg1;
  }

  @Override
  protected byte[] _getParkMeadowsSheet(Date reportDate, Dataset turbidityData, Dataset Hours, Dataset WQData) throws Exception {
      return ExcelFunctions._getParkMeadowsSheet(reportDate, new ObjectDatasetWrapper(turbidityData), new ObjectDatasetWrapper(Hours), new ObjectDatasetWrapper(WQData));
    }

  @Override
  protected byte[] _getCreekside(Date reportDate, Dataset fiveMinData, Dataset turbData, Dataset hours) throws Exception {
    return ExcelFunctions._getCreekside(reportDate, new ObjectDatasetWrapper(fiveMinData), new ObjectDatasetWrapper(turbData), new ObjectDatasetWrapper(hours));
  }

  @Override
  public byte[] _getJSSD(Date reportDate, Dataset fiveMinData, Dataset turbData, Dataset hours) throws Exception {
    return ExcelFunctions._getJSSD(reportDate, new ObjectDatasetWrapper(fiveMinData), new ObjectDatasetWrapper(turbData), new ObjectDatasetWrapper(hours));
  }

  @Override
  protected byte[] _getCreeksideUVDaily(Date reportDate, Dataset runHours, Dataset totalProd, Dataset redData, Dataset offSpecData) throws Exception {
    return ExcelFunctions._getCreeksideUVDaily(reportDate, new ObjectDatasetWrapper(runHours), new ObjectDatasetWrapper(totalProd), new ObjectDatasetWrapper(redData), new ObjectDatasetWrapper(offSpecData));
  }

  @Override
  protected byte[] _getCreeksideUVMonthly(Date reportDate, Dataset runHours, Dataset totalProd, Dataset offSpecData) throws Exception {
    return ExcelFunctions._getCreeksideUVMonthly(reportDate, new ObjectDatasetWrapper(runHours), new ObjectDatasetWrapper(totalProd), new ObjectDatasetWrapper(offSpecData));
  }

  @Override
  protected byte[] _getCreeksideUVOffspec(Date reportDate, Dataset offSpecData) throws Exception {
    return ExcelFunctions._getCreeksideUVOffspec(reportDate, new ObjectDatasetWrapper(offSpecData));
  }

  @Override
  protected byte[] _getQuinnsSheetMnO2(Date reportDate, Dataset FiveMinData, Dataset rackResults, Dataset WQData) throws Exception {
    return ExcelFunctions._getQuinnsSheetMnO2(reportDate, new ObjectDatasetWrapper(FiveMinData), new ObjectDatasetWrapper(rackResults), new ObjectDatasetWrapper(WQData));
  }

  @Override
  protected byte[] _getQuinnsSheet(Date reportDate, Dataset FiveMinData, Dataset rackResults, Dataset WQData) throws Exception {
    return ExcelFunctions._getQuinnsSheet(reportDate, new ObjectDatasetWrapper(FiveMinData), new ObjectDatasetWrapper(rackResults), new ObjectDatasetWrapper(WQData));
  }

  @Override
  protected byte[] _getGroundWaterDisinfection(Date reportDate, Dataset groundWaterData, Dataset hypoSpeed) throws Exception {
    return ExcelFunctions._getGroundWaterDisinfection(reportDate, new ObjectDatasetWrapper(groundWaterData), new ObjectDatasetWrapper(hypoSpeed));
  }

  @Override
  protected byte[] _getGroundWaterDisinfectionNoPM(Date reportDate, Dataset groundWaterData, Dataset hypoSpeed) throws Exception {
    return ExcelFunctions._getGroundWaterDisinfectionNoPM(reportDate, new ObjectDatasetWrapper(groundWaterData), new ObjectDatasetWrapper(hypoSpeed));
  }

  @Override
  protected byte[] _getQuinnsFlows(Date reportDate, Dataset SewerFlows) throws Exception {
    return ExcelFunctions._getQuinnsFlows(reportDate, new ObjectDatasetWrapper(SewerFlows));
  }

  @Override
  protected byte[] _getOgdensSheet(Date reportDate, Dataset FiveMinData, Dataset WQData, Dataset turbidity, Dataset rackResults, Dataset rack2Results, Dataset rack3Results, Dataset rack4Results, Dataset rack5Results, Dataset rack6Results, Dataset rack7Results, Dataset rack8Results, Dataset rack9Results) throws Exception {
    return ExcelFunctions._getOgdensSheet(reportDate, new ObjectDatasetWrapper(FiveMinData), new ObjectDatasetWrapper(WQData), new ObjectDatasetWrapper(turbidity), new ObjectDatasetWrapper(rackResults), new ObjectDatasetWrapper(rack2Results), new ObjectDatasetWrapper(rack3Results), new ObjectDatasetWrapper(rack4Results), new ObjectDatasetWrapper(rack5Results), new ObjectDatasetWrapper(rack6Results), new ObjectDatasetWrapper(rack7Results), new ObjectDatasetWrapper(rack8Results), new ObjectDatasetWrapper(rack9Results)
    );
  }

  @Override
  protected byte[] _getQuinnsMonitoringData(Date reportDate, Dataset production, Dataset rackResults, Dataset chemicals) throws Exception {
    return ExcelFunctions._getQuinnsMonitoringData(reportDate, new ObjectDatasetWrapper(production), new ObjectDatasetWrapper(rackResults), new ObjectDatasetWrapper(chemicals));
  }

  @Override
  protected byte[] _getMembraneReport(Date reportDate, Dataset production, Dataset rackResults, Dataset IT_data) throws Exception {
    return ExcelFunctions._getMembraneReport(reportDate, new ObjectDatasetWrapper(production), new ObjectDatasetWrapper(rackResults), new ObjectDatasetWrapper(IT_data));
  }
}
