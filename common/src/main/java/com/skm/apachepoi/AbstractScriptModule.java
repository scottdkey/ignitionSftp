package com.skm.apachepoi;

import com.inductiveautomation.ignition.common.BundleUtil;
import com.inductiveautomation.ignition.common.Dataset;
import com.inductiveautomation.ignition.common.script.hints.ScriptArg;
import com.inductiveautomation.ignition.common.script.hints.ScriptFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public abstract class AbstractScriptModule implements ExcelFunctionsBlackBox {
  public static final String MODULE_ID = "com.skm.apachepoi";
  static final Logger logger = LoggerFactory.getLogger("POI Functions");
  static {
    BundleUtil.get().addBundle(
        AbstractScriptModule.class.getSimpleName(),
        AbstractScriptModule.class.getClassLoader(),
        AbstractScriptModule.class.getName().replace('.', '/')
    );
  }

  @Override
  @ScriptFunction(docBundlePrefix = "ScriptModule")
  public int multiply(@ScriptArg("arg0") int arg0, @ScriptArg("arg1") int arg1) {
    return multiplyImpl(arg0, arg1);
  }
  protected abstract int multiplyImpl(int arg0, int arg1);

  @Override
  @ScriptFunction(docBundlePrefix = "ScriptModule")
  public int add(@ScriptArg("arg0") int arg0, @ScriptArg("arg1") int arg1) {
    return addImpl(arg0, arg1);
  }
  protected abstract int addImpl(int arg0, int arg1);

  @Override
  @ScriptFunction(docBundlePrefix = "ScriptModule")
  public int sub(@ScriptArg("arg0") int arg0, @ScriptArg("arg1") int arg1) {
    return subImpl(arg0, arg1);
  }
  protected abstract int subImpl(int arg0, int arg1);

  @Override
  @ScriptFunction(docBundlePrefix = "ScriptModule")
  public int divide(@ScriptArg("arg0") int arg0, @ScriptArg("arg1") int arg1) {
    return divideImpl(arg0, arg1);
  }
  protected abstract int divideImpl(int arg0, int arg1);

  @Override
  @ScriptFunction(docBundlePrefix = "ScriptModule")
  public byte[] getParkMeadowsSheet(
      @ScriptArg("reportDate") Date reportDate,
      @ScriptArg("turbidityData") Dataset turbidityData,
      @ScriptArg("Hours") Dataset Hours,
      @ScriptArg("WGData") Dataset WQData)
      throws Exception {
    return _getParkMeadowsSheet(reportDate, turbidityData, Hours, WQData);
  }
  protected abstract byte[] _getParkMeadowsSheet(
      Date reportDate,
      Dataset turbidityData,
      Dataset Hours,
      Dataset WQData)
      throws Exception;


  @Override
  @ScriptFunction(docBundlePrefix = "ScriptModule")
  public byte[] getCreekside(
      @ScriptArg("reportDate") Date reportDate,
      @ScriptArg("fiveMinData") Dataset fiveMinData,
      @ScriptArg("turbData") Dataset turbData,
      @ScriptArg("hours") Dataset hours)
      throws Exception {
    return _getCreekside(reportDate, fiveMinData, turbData, hours);
  }


  protected abstract byte[] _getCreekside(
      Date reportDate,
      Dataset fiveMinData,
      Dataset turbData,
      Dataset hours)
      throws Exception;

  @Override
  @ScriptFunction(docBundlePrefix = "ScriptModule")
  public byte[] getJSSD(
      @ScriptArg("reportDate") Date reportDate,
      @ScriptArg("fiveMinData") Dataset fiveMinData,
      @ScriptArg("turbData") Dataset turbData,
      @ScriptArg("hours") Dataset hours)
      throws Exception {
    return _getJSSD(reportDate, fiveMinData, turbData, hours);
  }

  protected abstract byte[] _getJSSD(
      Date reportDate,
      Dataset fiveMinData,
      Dataset turbData,
      Dataset hours)
      throws Exception;

  @Override
  @ScriptFunction(docBundlePrefix = "ScriptModule")
  public byte[] getCreeksideUVDaily(
      @ScriptArg("reportDate") Date reportDate,
      @ScriptArg("runHours") Dataset runHours,
      @ScriptArg("totalProd") Dataset totalProd,
      @ScriptArg("redData") Dataset redData,
      @ScriptArg("offSpecData") Dataset offSpecData)
      throws Exception {
    return _getCreeksideUVDaily(
        reportDate,
        runHours,
        totalProd,
        redData,
        offSpecData);
  }


  protected abstract byte[] _getCreeksideUVDaily(
      Date reportDate,
      Dataset runHours,
      Dataset totalProd,
      Dataset redData,
      Dataset offSpecData)
      throws Exception;

  @Override
  @ScriptFunction(docBundlePrefix = "ScriptModule")
  public byte[] getCreeksideUVMonthly(
      @ScriptArg("reportDate") Date reportDate,
      @ScriptArg("runHours") Dataset runHours,
      @ScriptArg("totalProd") Dataset totalProd,
      @ScriptArg("offSpecData") Dataset offSpecData)
      throws Exception {
    return _getCreeksideUVMonthly(
        reportDate,
        runHours,
        totalProd,
        offSpecData);
  }
  protected abstract byte[] _getCreeksideUVMonthly(
      Date reportDate,
      Dataset runHours,
      Dataset totalProd,
      Dataset offSpecData)
      throws Exception;

  @Override
  @ScriptFunction(docBundlePrefix = "ScriptModule")
  public byte[] getCreeksideUVOffSpec(
      @ScriptArg("reportDate") Date reportDate, @ScriptArg("offSpecData") Dataset offSpecData)
      throws Exception {
    return _getCreeksideUVOffspec(reportDate, offSpecData);
  }

  protected abstract byte[] _getCreeksideUVOffspec(Date reportDate, Dataset offSpecData)
      throws Exception;

  // Quinns Method Declarations

  @Override
  @ScriptFunction(docBundlePrefix = "ScriptModule")
  public byte[] getQuinnsSheetMnO2(
      @ScriptArg("reportDate") Date reportDate,
      @ScriptArg("FiveMinData") Dataset FiveMinData,
      @ScriptArg("rackResults") Dataset rackResults,
      @ScriptArg("WQData") Dataset WQData)
      throws Exception {
    return _getQuinnsSheetMnO2(
        reportDate, FiveMinData, rackResults, WQData);
  }

  protected abstract byte[] _getQuinnsSheetMnO2(
      Date reportDate,
      Dataset FiveMinData,
      Dataset rackResults,
      Dataset WQData)
      throws Exception;

  @Override
  @ScriptFunction(docBundlePrefix = "ScriptModule")
  public byte[] getQuinnsSheet(
      @ScriptArg("reportDate") Date reportDate,
      @ScriptArg("FiveMinData") Dataset FiveMinData,
      @ScriptArg("rackResults") Dataset rackResults,
      @ScriptArg("WQData") Dataset WQData)
      throws Exception {
    return _getQuinnsSheet(reportDate, FiveMinData, rackResults, WQData);
  }

  protected abstract byte[] _getQuinnsSheet(Date reportDate, Dataset FiveMinData, Dataset rackResults, Dataset WQData) throws Exception;

  @Override
  @ScriptFunction(docBundlePrefix = "ScriptModule")
  public byte[] getGroundWaterDisinfection(
      @ScriptArg("reportDate") Date reportDate,
      @ScriptArg("groundWaterData") Dataset groundWaterData,
      @ScriptArg("hypoSpeed") Dataset hypoSpeed)
      throws Exception {
    return _getGroundWaterDisinfection(reportDate, groundWaterData, hypoSpeed);
  }

  protected abstract byte[] _getGroundWaterDisinfection(
      Date reportDate, Dataset groundWaterData, Dataset hypoSpeed)
      throws Exception;

  @Override
  @ScriptFunction(docBundlePrefix = "ScriptModule")
  public byte[] getGroundWaterDisinfectionNoPM(
      @ScriptArg("reportDate") Date reportDate,
      @ScriptArg("groundWaterData") Dataset groundWaterData,
      @ScriptArg("hypoSpeed") Dataset hypoSpeed)
      throws Exception {
    return _getGroundWaterDisinfectionNoPM(
        reportDate, groundWaterData, hypoSpeed);
  }

  protected abstract byte[] _getGroundWaterDisinfectionNoPM(
      Date reportDate, Dataset groundWaterData, Dataset hypoSpeed)
      throws Exception;

  @Override
  @ScriptFunction(docBundlePrefix = "ScriptModule")
  public byte[] getQuinnsFlows(
      @ScriptArg("reportDate") Date reportDate, @ScriptArg("SewerFlows") Dataset SewerFlows)
      throws Exception {
    return _getQuinnsFlows(reportDate, SewerFlows);
  }

  protected abstract byte[] _getQuinnsFlows(Date reportDate, Dataset SewerFlows)
      throws Exception;

  @Override
  @ScriptFunction(docBundlePrefix = "ScriptModule")
  public byte[] getOgdensSheet(
      @ScriptArg("reportDate") Date reportDate,
      @ScriptArg("FiveMinData") Dataset FiveMinData,
      @ScriptArg("WQData") Dataset WQData,
      @ScriptArg("turbidity") Dataset turbidity,
      @ScriptArg("rackResults") Dataset rackResults,
      @ScriptArg("rack2Results") Dataset rack2Results,
      @ScriptArg("rack3Results") Dataset rack3Results,
      @ScriptArg("rack4Results") Dataset rack4Results,
      @ScriptArg("rack5Results") Dataset rack5Results,
      @ScriptArg("rack6Results") Dataset rack6Results,
      @ScriptArg("rack7Results") Dataset rack7Results,
      @ScriptArg("rack8Results") Dataset rack8Results,
      @ScriptArg("rack9Results") Dataset rack9Results)
      throws Exception {
    return _getOgdensSheet(
        reportDate,
        FiveMinData,
        rackResults,
        WQData,
        turbidity,
        rack2Results,
        rack3Results,
        rack4Results,
        rack5Results,
        rack6Results,
        rack7Results,
        rack8Results,
        rack9Results);
  }

  protected abstract byte[] _getOgdensSheet(
      Date reportDate,
      Dataset FiveMinData,
      Dataset WQData,
      Dataset turbidity,
      Dataset rackResults,
      Dataset rack2Results,
      Dataset rack3Results,
      Dataset rack4Results,
      Dataset rack5Results,
      Dataset rack6Results,
      Dataset rack7Results,
      Dataset rack8Results,
      Dataset rack9Results)
      throws Exception;

  // Quinns Monitoring data
  @Override
  @ScriptFunction(docBundlePrefix = "ScriptModule")
  public byte[] getQuinnsMonitoring(
      @ScriptArg("reportDate") Date reportDate,
      @ScriptArg("production") Dataset production,
      @ScriptArg("rackResults") Dataset rackResults,
      @ScriptArg("chemicals") Dataset chemicals)
      throws Exception {
    return _getQuinnsMonitoringData(
        reportDate,
        production,
        rackResults,
        chemicals);
  }

  protected abstract byte[] _getQuinnsMonitoringData(Date reportDate, Dataset production, Dataset rackResults, Dataset chemicals) throws Exception;

  // New
  @Override
  @ScriptFunction(docBundlePrefix = "ScriptModule")
  public byte[] getMembraneReport(
      @ScriptArg("reportDate") Date reportDate,
      @ScriptArg("production") Dataset production,
      @ScriptArg("rackResults") Dataset rackResults,
      @ScriptArg("IT_data") Dataset IT_data)
      throws Exception {
    return _getMembraneReport(
        reportDate,
        production,
        rackResults,
        IT_data);
  }

  protected abstract byte[] _getMembraneReport(Date reportDate, Dataset production, Dataset rackResults, Dataset IT_data) throws Exception;
}
