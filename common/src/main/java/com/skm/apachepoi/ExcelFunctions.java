package com.skm.apachepoi;

import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

public class ExcelFunctions {


  static SimpleDateFormat displayFormat = HelperFunctions.displayFormat;
  static SimpleDateFormat excelDateFormat = HelperFunctions.excelDateFormat;
  static SimpleDateFormat operationalDateFormat = HelperFunctions.operationalDateFormat;
  static SimpleDateFormat excelShortDateFormat = HelperFunctions.excelShortDateFormat;
  static SimpleDateFormat monthFormat = HelperFunctions.monthFormat;
  static SimpleDateFormat hoursMinsSecs = HelperFunctions.hoursMinsSecs;
  // ----------------------------------------------Functions-----------------------------------------------------------

  public static byte[] _getParkMeadowsSheet(
      Date reportDate,
      ObjectDatasetWrapper turbidityData,
      ObjectDatasetWrapper Hours,
      ObjectDatasetWrapper WQData)
      throws Exception {
    Calendar cal = Calendar.getInstance();
    cal.setTime(reportDate);

    InputStream is = ExcelFunctions.class.getResourceAsStream("Park Meadows Well.xlsx");
    XSSFWorkbook wb = new XSSFWorkbook(is);
    FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

    cal.setTime(reportDate);
    Integer month = cal.get(Calendar.MONTH) + 1;
    int year = cal.get(Calendar.YEAR);
    int numDaysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

    XSSFSheet sheet = wb.getSheet("Turb Compliance");
    sheet.getRow(4).getCell(2).setCellValue(monthFormat.format(cal.getTime()));
    sheet.getRow(5).getCell(2).setCellValue(year);

    // Turbidity Worksheet
    sheet = wb.getSheet("Turb Data");
    cal.setTime(reportDate);
    sheet.getRow(2).getCell(2).setCellValue(monthFormat.format(cal.getTime()));
    sheet.getRow(3).getCell(2).setCellValue(year);

    Double blank = sheet.getRow(11).getCell(7).getNumericCellValue();

    // Clear all data first
    for (int i = 0; i < 31; i++) {
      Integer rowIdx = 11 + i;
      sheet.getRow(rowIdx).getCell(0).setCellValue("");
      sheet.getRow(rowIdx).getCell(1).setCellValue(0);
    }

    for (ObjectDatasetWrapper.Row row : turbidityData) {
      Date t_stamp = (Date) row.getKeyValue("t_stamp");
      Double fourAMTurb = (Double) row.getKeyValue("PRKM_4AM_TURB", 0.0);
      Double eightAMTurb = (Double) row.getKeyValue("PRKM_8AM_TURB", 0.0);
      Double twelvePMTurb = (Double) row.getKeyValue("PRKM_12PM_TURB", 0.0);
      Double fourPMTurb = (Double) row.getKeyValue("PRKM_4PM_TURB", 0.0);
      Double eightPMTurb = (Double) row.getKeyValue("PRKM_8PM_TURB", 0.0);
      Double twelveAMTurb = (Double) row.getKeyValue("PRKM_12AM_TURB", 0.0);

      cal.setTime(t_stamp);
      Integer day = cal.get(Calendar.DAY_OF_MONTH);
      Integer hour = cal.get(Calendar.HOUR_OF_DAY);
      Integer rowIdx = 11 + (day - 1);

      sheet.getRow(rowIdx).getCell(0).setCellValue(excelShortDateFormat.format(t_stamp));
      if (fourAMTurb == null) {
        sheet.getRow(rowIdx).getCell(2).setCellValue("PO");
      } else {
        sheet.getRow(rowIdx).getCell(2).setCellValue(fourAMTurb);
      }
      if (eightAMTurb == null) {
        sheet.getRow(rowIdx).getCell(3).setCellValue("PO");
      } else {
        sheet.getRow(rowIdx).getCell(3).setCellValue(eightAMTurb);
      }
      if (twelvePMTurb == null) {
        sheet.getRow(rowIdx).getCell(4).setCellValue("PO");
      } else {
        sheet.getRow(rowIdx).getCell(4).setCellValue(twelvePMTurb);
      }
      if (fourPMTurb == null) {
        sheet.getRow(rowIdx).getCell(5).setCellValue("PO");
      } else {
        sheet.getRow(rowIdx).getCell(5).setCellValue(fourPMTurb);
      }
      if (eightPMTurb == null) {
        sheet.getRow(rowIdx).getCell(6).setCellValue("PO");
      } else {
        sheet.getRow(rowIdx).getCell(6).setCellValue(eightPMTurb);
      }
      if (twelveAMTurb == null) {
        sheet.getRow(rowIdx).getCell(7).setCellValue("PO");
      } else {
        sheet.getRow(rowIdx).getCell(7).setCellValue(twelveAMTurb);
      }
    }

    for (ObjectDatasetWrapper.Row row : Hours) {
      Date t_stamp = (Date) row.getKeyValue("t_stamp");

      cal.setTime(t_stamp);
      Integer day = cal.get(Calendar.DAY_OF_MONTH);
      Integer rowIdx = 11 + (day - 1);

      row.setKeyValue("PRKM_ToSystem_Hours", sheet, rowIdx, 1);
    }

    // Water Quality Sheet
    sheet = wb.getSheet("WQP Report");
    cal.setTime(reportDate);
    sheet.getRow(2).getCell(2).setCellValue(monthFormat.format(cal.getTime()));
    sheet.getRow(3).getCell(2).setCellValue(year);

    Calendar cal3 = Calendar.getInstance();
    cal3.setTime(reportDate);
    cal3.set(Calendar.DAY_OF_MONTH, 1);
    int WQSidx = 12;
    for (int i = 0; i < numDaysInMonth; i++) {
      sheet.getRow(WQSidx).getCell(0).setCellValue(excelShortDateFormat.format(cal3.getTime()));
      sheet.getRow(WQSidx).getCell(4).setCellValue("PO");
      sheet.getRow(WQSidx).getCell(5).setCellValue("PO");
      sheet.getRow(WQSidx).getCell(6).setCellValue("PO");
      WQSidx++;
      cal3.add(Calendar.DAY_OF_MONTH, 1);
    }

    Integer curr_Day = -1;
    // Integer wqRowIdx = 12;
    Double Max_Turb = 0.0;
    for (ObjectDatasetWrapper.Row row : WQData) {

      Date t_stamp = (Date) row.getKeyValue("t_stamp");
      Integer ToSystem = (Integer) row.getKeyValue("Valve_Closed", 0);
      Double Turb = (Double) row.getKeyValue("PRKM_Turb", 0.0);
      cal.setTime(t_stamp);
      Integer day = cal.get(Calendar.DAY_OF_MONTH);
      int rowIdx = 12 + (day - 1);

      // If we have moved to the next day reset Max_turb and change our current day to our new day
      // to check
      if (!day.equals(curr_Day)) {
        Max_Turb = 0.0;
        curr_Day = day;
        sheet.getRow(rowIdx).getCell(0).setCellValue(excelShortDateFormat.format(t_stamp));
        //                sheet.getRow(rowIdx).getCell(4).setCellValue("PO");
        //                sheet.getRow(rowIdx).getCell(5).setCellValue("PO");
        //                sheet.getRow(rowIdx).getCell(6).setCellValue("PO");
      }
      if (ToSystem.equals(1) && Turb > Max_Turb) {
        Max_Turb = Turb;
        sheet.getRow(rowIdx).getCell(5).setCellValue(Max_Turb);
        row.setKeyValue("PRKM_pH", sheet, rowIdx, 6);
        row.setKeyValue("PRKM_Water_Temp", sheet, rowIdx, 4);
      }
    }

    // Disinfection Report
    sheet = wb.getSheet("DISINFECTION REPORT");
    cal.setTime(reportDate);
    sheet.getRow(2).getCell(2).setCellValue(monthFormat.format(cal.getTime()));
    sheet.getRow(3).getCell(2).setCellValue(year);

    Calendar cal2 = Calendar.getInstance();
    cal2.setTime(reportDate);
    cal2.set(Calendar.DAY_OF_MONTH, 1);
    int tsIDX = 13;
    for (int i = 0; i < numDaysInMonth; i++) {
      if (tsIDX < 28) {
        sheet.getRow(tsIDX).getCell(1).setCellValue(excelShortDateFormat.format(cal2.getTime()));
        sheet.getRow(tsIDX).getCell(2).setCellValue("PO");
      }
      if (tsIDX >= 28) {
        int IdxTmp = tsIDX - 15;
        sheet.getRow(IdxTmp).getCell(5).setCellValue(excelShortDateFormat.format(cal2.getTime()));
        sheet.getRow(IdxTmp).getCell(6).setCellValue("PO");
      }
      cal2.add(Calendar.DAY_OF_MONTH, 1);
      tsIDX++;
    }

    double Min_Cl2 = 10.0;
    int MinCl2Idx;
    Integer day2 = -1;
    // Iterate through all rows to determine the lowest CL2 amount while the well was running
    // Skips the first two rows after the valve goes from 0 to 1 resets the row skip every time a 0
    // is read
    for (ObjectDatasetWrapper.Row row : WQData) {

      Date t_stamp = (Date) row.getKeyValue("t_stamp");
      Double CL2 = (Double) row.getKeyValue("PRKM_CL2_Res", 0.0);
      Double PRKM_Flow = (Double) row.getKeyValue("PRKM_Flow", 0.0);
      Double DIVD_Flow = (Double) row.getKeyValue("DIVD_Flow", 0.0);
      Integer ToSystem = (Integer) row.getKeyValue("Valve_Closed", 0);

      cal.setTime(t_stamp);
      Integer day = cal.get(Calendar.DAY_OF_MONTH);
      MinCl2Idx = 13 + (day - 1);

      if (!day.equals(day2)) {
        Min_Cl2 = 10.0;
        day2 = day;
        if (MinCl2Idx < 28) {
          sheet.getRow(MinCl2Idx).getCell(1).setCellValue(excelShortDateFormat.format(t_stamp));
          //                    sheet.getRow(MinCl2Idx).getCell(2).setCellValue("PO");
        } else {
          int IdxTmp = MinCl2Idx - 15;
          sheet.getRow(IdxTmp).getCell(5).setCellValue(excelShortDateFormat.format(t_stamp));
          //                    sheet.getRow(IdxTmp).getCell(6).setCellValue("PO");
        }
      } else {
        if (ToSystem == 1 && Min_Cl2 > CL2) {
          Min_Cl2 = CL2;
          if (MinCl2Idx < 28) {
            sheet.getRow(MinCl2Idx).getCell(1).setCellValue(excelShortDateFormat.format(t_stamp));
            sheet.getRow(MinCl2Idx).getCell(2).setCellValue(Min_Cl2);
          } else {
            int IdxTmp = MinCl2Idx - 15;
            sheet.getRow(IdxTmp).getCell(5).setCellValue(excelShortDateFormat.format(t_stamp));
            sheet.getRow(IdxTmp).getCell(6).setCellValue(Min_Cl2);
          }
        }
      }
    }

    // Sequence 1 Sheet
    sheet = wb.getSheet("SEQUENCE 1");
    cal.setTime(reportDate);
    sheet.getRow(1).getCell(2).setCellValue(monthFormat.format(cal.getTime()));
    sheet.getRow(2).getCell(2).setCellValue(year);

    Calendar cal4 = Calendar.getInstance();
    cal4.setTime(reportDate);
    cal4.set(Calendar.DAY_OF_MONTH, 1);
    int DSSidx = 14;
    for (int i = 0; i < numDaysInMonth; i++) {
      sheet.getRow(DSSidx).getCell(2).setCellValue("PO");
      sheet.getRow(DSSidx).getCell(4).setCellValue("PO");
      sheet.getRow(DSSidx).getCell(5).setCellValue("PO");
      sheet.getRow(DSSidx).getCell(6).setCellValue("PO");
      DSSidx++;
      cal4.add(Calendar.DAY_OF_MONTH, 1);
    }

    // Max flow values here
    double maxFlow = 0.0;
    Integer currDay = -1;
    // Value to get to the correct start point to input data
    int offSet = 14;
    final int MAXFLOWCOL = 2, CL2COLL = 4, WATERPHCOLL = 5, WATERTEMPCOLL = 6;
    for (ObjectDatasetWrapper.Row row : WQData) {

      Date t_stamp = (Date) row.getKeyValue("t_stamp");
      Double CL2 = (Double) row.getKeyValue("PRKM_CL2_Res", 0.0);
      Double PRKM_Flow = (Double) row.getKeyValue("PRKM_Flow", 0.0);
      Double DIVD_Flow = (Double) row.getKeyValue("DIVD_Flow", 0.0);
      Double waterTemp = (Double) row.getKeyValue("PRKM_Water_Temp", 0.0);
      Double waterPh = (Double) row.getKeyValue("PRKM_pH", 0.0);
      Integer ToSystem = (Integer) row.getKeyValue("Valve_Closed", 0);

      cal.setTime(t_stamp);
      Integer day = cal.get(Calendar.DAY_OF_MONTH);
      int rowIdx = offSet + (day - 1);

      if (!day.equals(currDay)) {
        maxFlow = 0.0;
        currDay = day;
        //                sheet.getRow(rowIdx).getCell(MAXFLOWCOL).setCellValue("PO");
        //                sheet.getRow(rowIdx).getCell(CL2COLL).setCellValue("PO");
        //                sheet.getRow(rowIdx).getCell(WATERPHCOLL).setCellValue("PO");
        //                sheet.getRow(rowIdx).getCell(WATERTEMPCOLL).setCellValue("PO");
      }
      if ((PRKM_Flow + DIVD_Flow) > maxFlow && ToSystem == 1) {
        maxFlow = PRKM_Flow + DIVD_Flow;
        sheet.getRow(rowIdx).getCell(MAXFLOWCOL).setCellValue(maxFlow);
        sheet.getRow(rowIdx).getCell(CL2COLL).setCellValue(CL2);
        sheet.getRow(rowIdx).getCell(WATERPHCOLL).setCellValue(waterPh);
        sheet.getRow(rowIdx).getCell(WATERTEMPCOLL).setCellValue(waterTemp);
      }
    }
    // Run evaluator to make all in sheet formulas update with the new data
    evaluator.evaluateAll();
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    wb.write(bos);
    return bos.toByteArray();
  }

  public static byte[] _getQuinnsSheet(
      Date reportDate,
      ObjectDatasetWrapper FiveMinData,
      ObjectDatasetWrapper rackResults,
      ObjectDatasetWrapper WQData)
      throws Exception {

    Calendar cal = Calendar.getInstance();
    cal.setTime(reportDate);

    // Get an input stream to the workbook
    InputStream is = ExcelFunctions.class.getResourceAsStream("Quinns Monthly Report.xlsx");
    XSSFWorkbook wb = new XSSFWorkbook(is);
    FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

    // Pull out the Month and Year
    cal.setTime(reportDate);
    int month = cal.get(Calendar.MONTH) + 1;
    int year = cal.get(Calendar.YEAR);

    // EPA Monthly Summary
    Map<String, String> EPA_params = new HashMap<>();
    EPA_params.put("sheet_name", "EPA Monthly Summary");
    EPA_params.put("cl2_column", "AI0533_EffluentCL2");
    EPA_params.put("date", "t_stamp");
    // EPA Monthly Summary
    HelperFunctions.EPA_Summary(FiveMinData, wb, month, year, EPA_params);

    // Turbidity Worksheet
    Map<String, String> turb_params = new HashMap<>();
    turb_params.put("sheet_name", "Turbidity Daily Data Sheet");
    turb_params.put("date", "t_stamp");
    turb_params.put("12AM_turb", "QJ_12AM_Turb");
    turb_params.put("4AM_turb", "QJ_4AM_Turb");
    turb_params.put("8AM_turb", "QJ_8AM_Turb");
    turb_params.put("12PM_turb", "QJ_12PM_Turb");
    turb_params.put("4PM_turb", "QJ_4PM_Turb");
    turb_params.put("8PM_turb", "QJ_8PM_Turb");
    HelperFunctions.Turbidity_Sheet(WQData, wb, month, year, turb_params);

    // DI
    // Testing--------------------------------------------------------------------------------------------------------------------------------------------
    // Unit(1) DI Testing
    HelperFunctions.Quinn_DI_Testing(FiveMinData, rackResults, wb, "1", month, year);
    // Unit(2) DI Testing
    HelperFunctions.Quinn_DI_Testing(FiveMinData, rackResults, wb, "2", month, year);
    // Unit(3) DI Testing
    HelperFunctions.Quinn_DI_Testing(FiveMinData, rackResults, wb, "3", month, year);
    // Unit(4) DI Testing
    HelperFunctions.Quinn_DI_Testing(FiveMinData, rackResults, wb, "4", month, year);
    // Unit(5) DI Testing
    HelperFunctions.Quinn_DI_Testing(FiveMinData, rackResults, wb, "5", month, year);

    // Operational
    // Worksheet---------------------------------------------------------------------------------------------------------------------------------
    Map<String, String> op_params = new HashMap<>();
    op_params.put("cl2_column", "AI0533_EffluentCL2");
    op_params.put("flow1", "FI0510_Fairway");
    op_params.put("flow2", "FI0520_BootHill");
    op_params.put("water_temp", "TI3008_RawWaterTemp");
    op_params.put("clearwell_level", "LI0510_ClearWellLevel");
    op_params.put("plant_running", "PlantRunning");
    op_params.put("date", "t_stamp");
    HelperFunctions.Quinn_Operational_Sheet(FiveMinData, wb, op_params);

    // Disinfection
    // Report----------------------------------------------------------------------------------------------------------------------------------
    // Quinn_Disinfection(FiveMinData, wb, month, year);

    // Sequence Dates
    // ---------------------------------------------------------------------------------------------------------------------------------------
    HelperFunctions.Quinn_Sequence(cal, year, wb);

    // Run evaluator to make all in sheet formulas update with the new data
    evaluator.evaluateAll();
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    wb.write(bos);
    return bos.toByteArray();
  }

  public static byte[] _getCreekside(
      Date reportDate,
      ObjectDatasetWrapper fiveMinData,
      ObjectDatasetWrapper turbData,
      ObjectDatasetWrapper hours)
      throws Exception {

    Calendar cal = Calendar.getInstance();
    cal.setTime(reportDate);

    // Get an input stream to the workbook
    InputStream is = ExcelFunctions.class.getResourceAsStream("CreeksideWTP.xlsx");
    XSSFWorkbook wb = new XSSFWorkbook(is);
    FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

    // Pull out the Month and Year
    cal.setTime(reportDate);
    int month = cal.get(Calendar.MONTH) + 1;
    int year = cal.get(Calendar.YEAR);

    // EPA Monthly Summary
    Map<String, String> EPA_params = new HashMap<>();
    EPA_params.put("sheet_name", "EPA Monthly Summary");
    EPA_params.put("cl2_column", "PRKM_CL2_Res");
    EPA_params.put("date", "t_stamp");
    // EPA Monthly Summary
    HelperFunctions.EPA_SummaryCreekside(fiveMinData, wb, month, year, EPA_params, reportDate);

    Map<String, String> turb_params = new HashMap<>();
    turb_params.put("sheet_name", "Turbidity Data");
    turb_params.put("date", "t_stamp");
    turb_params.put("12AM_turb", "PRKM_12AM_Turb");
    turb_params.put("4AM_turb", "PRKM_4AM_Turb");
    turb_params.put("8AM_turb", "PRKM_8AM_Turb");
    turb_params.put("12PM_turb", "PRKM_12PM_Turb");
    turb_params.put("4PM_turb", "PRKM_4PM_Turb");
    turb_params.put("8PM_turb", "PRKM_8PM_Turb");
    HelperFunctions.Turbidity_SheetCreekside(turbData, wb, month, year, turb_params, hours, reportDate);

    // Operational
    // Worksheet---------------------------------------------------------------------------------------------------------------------------------
    Map<String, String> op_params = new HashMap<>();
    op_params.put("cl2_column", "PRKM_CL2_res");
    op_params.put("flow1", "PRKM_Flow");
    op_params.put("flow2", "DIVD_Flow");
    op_params.put("water_temp", "PRKM_Water_Temp");
    op_params.put("PRKM_pH", "PRKM_pH");
    op_params.put("date", "t_stamp");
    HelperFunctions.Creekside_Operational_Sheet(fiveMinData, wb, op_params);

    // Disinfection
    // Report----------------------------------------------------------------------------------------------------------------------------------
    // Creekside_Disinfection(fiveMinData, wb);

    // Sequence Dates
    // ---------------------------------------------------------------------------------------------------------------------------------------
    // Quinn_Sequence(cal, year, wb);

    // Run evaluator to make all in sheet formulas update with the new data
    evaluator.evaluateAll();
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    wb.write(bos);
    return bos.toByteArray();
  }

  public static byte[] _getJSSD(
      Date reportDate,
      ObjectDatasetWrapper fiveMinData,
      ObjectDatasetWrapper turbData,
      ObjectDatasetWrapper hours)
      throws Exception {

    Calendar cal = Calendar.getInstance();
    cal.setTime(reportDate);

    // Get an input stream to the workbook
    InputStream is = ExcelFunctions.class.getResourceAsStream("JSSD_Monthly.xlsx");
    XSSFWorkbook wb = new XSSFWorkbook(is);
    FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

    // Pull out the Month and Year
    cal.setTime(reportDate);
    Integer month = cal.get(Calendar.MONTH) + 1;
    Integer year = cal.get(Calendar.YEAR);

    XSSFSheet sheet = wb.getSheet("Turb Data");
    //        sheet.getRow(2).getCell(2).setCellValue(reportDate);
    //        sheet.getRow(3).getCell(2).setCellValue(year_in);
    int prevDay = 0;

    for (ObjectDatasetWrapper.Row row : turbData) {
      Date t_stamp = (Date) row.getKeyValue("t_stamp");
      Double turb12AM = (Double) row.getKeyValue("jssd_12AM_Turb", 0.0);
      Double turb4AM = (Double) row.getKeyValue("jssd_4AM_turb", 0.0);
      Double turb8AM = (Double) row.getKeyValue("jssd_8AM_turb", 0.0);
      Double turb12PM = (Double) row.getKeyValue("jssd_12PM_turb", 0.0);
      Double turb4PM = (Double) row.getKeyValue("jssd_4PM_turb", 0.0);
      Double turb8PM = (Double) row.getKeyValue("jssd_8PM_turb", 0.0);
      cal.setTime(t_stamp);
      Integer day = cal.get(Calendar.DAY_OF_MONTH);
      if (day - prevDay > 1) {
        cal.add(Calendar.DAY_OF_MONTH, -1);
        Date t_stamp2 = cal.getTime();

        int rowIdx = 11 + (day - 2);
        sheet.getRow(rowIdx).getCell(0).setCellValue(excelShortDateFormat.format(t_stamp2));
        sheet.getRow(rowIdx).getCell(2).setCellValue("PO");
        sheet.getRow(rowIdx).getCell(3).setCellValue("PO");
        sheet.getRow(rowIdx).getCell(4).setCellValue("PO");
        sheet.getRow(rowIdx).getCell(5).setCellValue("PO");
        sheet.getRow(rowIdx).getCell(6).setCellValue("PO");
        sheet.getRow(rowIdx).getCell(7).setCellValue("PO");
      }

      cal.set(Calendar.DAY_OF_MONTH, day + 1);
      int rowIdx = 11 + (day - 1);

      sheet.getRow(rowIdx).getCell(0).setCellValue(excelShortDateFormat.format(t_stamp));
      if (turb4AM == null) {
        sheet.getRow(rowIdx).getCell(2).setCellValue("PO");
      } else {
        sheet.getRow(rowIdx).getCell(2).setCellValue(turb4AM);
      }
      if (turb8AM == null) {
        sheet.getRow(rowIdx).getCell(3).setCellValue("PO");
      } else {
        sheet.getRow(rowIdx).getCell(3).setCellValue(turb8AM);
      }
      if (turb12PM == null) {
        sheet.getRow(rowIdx).getCell(4).setCellValue("PO");
      } else {
        sheet.getRow(rowIdx).getCell(4).setCellValue(turb12PM);
      }
      if (turb4PM == null) {
        sheet.getRow(rowIdx).getCell(5).setCellValue("PO");
      } else {
        sheet.getRow(rowIdx).getCell(5).setCellValue(turb4PM);
      }
      if (turb8PM == null) {
        sheet.getRow(rowIdx).getCell(6).setCellValue("PO");
      } else {
        sheet.getRow(rowIdx).getCell(6).setCellValue(turb8PM);
      }
      if (turb12AM == null) {
        sheet.getRow(rowIdx).getCell(7).setCellValue("PO");
      } else {
        sheet.getRow(rowIdx).getCell(7).setCellValue(turb12AM);
      }

      prevDay = day;
    }

    sheet = wb.getSheet("Disinfection Report");

    double Min_Cl2 = 10.0;
    final int MINCL2OFFSET = 13;
    int MinCl2Idx;
    Integer day2 = -1;

    // Iterate through all rows to determine the lowest CL2 amount while the well was running
    for (ObjectDatasetWrapper.Row row : fiveMinData) {

      Date t_stamp = (Date) row.getKeyValue("t_stamp");
      Double CL2 = (Double) row.getKeyValue("jssd_eff_cl2", 0.0);
      Double flow = (Double) row.getKeyValue("jssd_eff_flow", 0.0);
      if (flow != null) {
        cal.setTime(t_stamp);
        Integer day = cal.get(Calendar.DAY_OF_MONTH);
        MinCl2Idx = MINCL2OFFSET + (day - 1);

        if (!day.equals(day2)) {
          Min_Cl2 = 10.0;
          day2 = day;
        } else {

          if (flow > 20 && Min_Cl2 > CL2) {
            Min_Cl2 = CL2;
            if (MinCl2Idx < 28) {
              sheet.getRow(MinCl2Idx).getCell(2).setCellValue(Min_Cl2);
            }
            if (MinCl2Idx >= 28) {
              int IdxTmp = MinCl2Idx - 15;
              sheet.getRow(IdxTmp).getCell(6).setCellValue(Min_Cl2);
            }
          }
          if (MinCl2Idx < 28) {
            sheet.getRow(MinCl2Idx).getCell(1).setCellValue(excelShortDateFormat.format(t_stamp));
          } else {
            int IdxTmp = MinCl2Idx - 15;
            sheet.getRow(IdxTmp).getCell(5).setCellValue(excelShortDateFormat.format(t_stamp));
          }
        }
      }
    }

    sheet = wb.getSheet("Operational Worksheet");
    int rowIdx = 8;
    int rowOffset = 0;
    Integer curHour = -1;
    double maxFlow = 0.00;

    for (ObjectDatasetWrapper.Row row : fiveMinData) {
      // Load Values from the tables
      Date t_stamp = (Date) row.getKeyValue("t_stamp");
      Double cl2Res = (Double) row.getKeyValue("jssd_eff_cl2", -1.0);
      Double flow = (Double) row.getKeyValue("jssd_eff_flow", -1.0);
      Double waterTemp = (Double) row.getKeyValue("jssd_inf_water_temp", -1.0);
      Double level = (Double) row.getKeyValue("jssd_fw_level", -1.0);
      Double waterTempF = waterTemp * 1.8 + 32;
      Double pmPH = (Double) row.getKeyValue("jssd_ph", 0.0);

      if (flow != null) {
        // Offsets for storing in the correct area
        cal.setTime(t_stamp);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int hour = cal.get(Calendar.HOUR_OF_DAY);

        // If its a new day populate each row for that day on an hourly basis
        if (!curHour.equals(hour)) {
          // reset values and drop the index to the next line
          rowOffset = rowIdx + ((day - 1) * 24) + hour;
          maxFlow = 0.00;
          curHour = hour;
          // Create a Calendar object to set the new hour
          Calendar calendar = Calendar.getInstance();
          calendar.setTime(t_stamp);
          calendar.set(Calendar.MINUTE, 0);
          calendar.set(Calendar.SECOND, 0);
          sheet
              .getRow(rowOffset)
              .getCell(1)
              .setCellValue(operationalDateFormat.format(calendar.getTime()));
        }
        // If PM Well Flow > 100 and the flow is larger than the current High Flow for the day store
        // it
        if (flow > 100 && maxFlow < flow) {
          sheet.getRow(rowOffset).getCell(3).setCellValue(flow);
          sheet.getRow(rowOffset).getCell(4).setCellValue(waterTemp);
          sheet.getRow(rowOffset).getCell(5).setCellValue(level);
          sheet.getRow(rowOffset).getCell(6).setCellValue(cl2Res);
        }
      }
    }
    //
    //        //Sequence Dates
    // ---------------------------------------------------------------------------------------------------------------------------------------
    //        Quinn_Sequence(cal, year, wb);

    // Run evaluator to make all in sheet formulas update with the new data
    evaluator.evaluateAll();
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    wb.write(bos);
    return bos.toByteArray();
  }

  public static byte[] _getCreeksideUVDaily(
      Date reportDate,
      ObjectDatasetWrapper runHours,
      ObjectDatasetWrapper totalProd,
      ObjectDatasetWrapper redData,
      ObjectDatasetWrapper offSpecData)
      throws Exception {

    Calendar cal = Calendar.getInstance();

    Calendar cal2 = Calendar.getInstance();
    cal2.setTime(reportDate);
    cal2.set(Calendar.DAY_OF_MONTH, cal2.getActualMaximum(Calendar.DAY_OF_MONTH));

    // Get an input stream to the workbook
    InputStream is = ExcelFunctions.class.getResourceAsStream("CKSD_DailyUV.xlsx");
    XSSFWorkbook wb = new XSSFWorkbook(is);
    FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

    // Pull out the Month and Year
    cal.setTime(reportDate);
    Integer month = cal.get(Calendar.MONTH) + 1;
    Integer year = cal.get(Calendar.YEAR);
    cal.set(Calendar.DAY_OF_MONTH, 1);

    XSSFSheet sheet = wb.getSheet("Summary");
    sheet
        .getRow(1)
        .getCell(5)
        .setCellValue(
            displayFormat.format(cal.getTime()) + " to " + displayFormat.format(cal2.getTime()));

    int rowIdx = 9;
    for (ObjectDatasetWrapper.Row row : runHours) {
      Date t_stamp = (Date) row.getKeyValue("t_stamp");
      Calendar c = Calendar.getInstance();
      c.setTime(t_stamp);
      int day = c.get(Calendar.DAY_OF_MONTH);
      Double runTime = (Double) row.getKeyValue("PRKM_ToSystem_Hours", -1.0);
      sheet.getRow(rowIdx + day).getCell(2).setCellValue(runTime);
    }
    rowIdx = 9;
    for (ObjectDatasetWrapper.Row row : totalProd) {
      Date t_stamp = (Date) row.getKeyValue("t_stamp");
      Calendar c = Calendar.getInstance();
      c.setTime(t_stamp);
      int day = c.get(Calendar.DAY_OF_MONTH);
      Double total = (Double) row.getKeyValue("WEL_PRKM_PM_Daily", 0.0);
      sheet.getRow(rowIdx + day).getCell(4).setCellValue(total / 1000);
    }
    rowIdx = 9;
    for (ObjectDatasetWrapper.Row row : redData) {
      Date t_stamp = (Date) row.getKeyValue("t_stamp");
      Calendar c = Calendar.getInstance();
      c.setTime(t_stamp);
      int day = c.get(Calendar.DAY_OF_MONTH);
      Double minRed = (Double) row.getKeyValue("minRed", 999.0);
      Date minTime = (Date) row.getKeyValue("minTime");
      Double minFlow = (Double) row.getKeyValue("minFlow", 0.0);
      Double minUVT = (Double) row.getKeyValue("minUVT", 0.0);

      if (minRed == 999.0) {
        sheet.getRow(rowIdx + day).getCell(6).setCellValue("PO");
        sheet.getRow(rowIdx + day).getCell(12).setCellValue("PO");
        sheet.getRow(rowIdx + day).getCell(15).setCellValue("PO");
        sheet.getRow(rowIdx + day).getCell(16).setCellValue("PO");
      } else {
        sheet.getRow(rowIdx + day).getCell(6).setCellValue(minTime);
        sheet.getRow(rowIdx + day).getCell(12).setCellValue(minRed);
        sheet.getRow(rowIdx + day).getCell(15).setCellValue(minFlow * .00144);
        sheet.getRow(rowIdx + day).getCell(16).setCellValue(minUVT);
      }
    }
    rowIdx = 9;
    for (ObjectDatasetWrapper.Row row : offSpecData) {
      Date t_stamp = (Date) row.getKeyValue("t_stamp");
      Calendar c = Calendar.getInstance();
      c.setTime(t_stamp);
      int day = c.get(Calendar.DAY_OF_MONTH);
      Double offSpecFlow = (Double) row.getKeyValue("OffSpecFlow", -1.0);
      sheet.getRow(rowIdx + day).getCell(19).setCellValue(offSpecFlow * 1000 / 1000000);
    }

    // Run evaluator to make all in sheet formulas update with the new data
    evaluator.evaluateAll();
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    wb.write(bos);
    return bos.toByteArray();
  }

  public static byte[] _getCreeksideUVMonthly(
      Date reportDate,
      ObjectDatasetWrapper runHours,
      ObjectDatasetWrapper totalProd,
      ObjectDatasetWrapper offSpecData)
      throws Exception {

    Calendar cal = Calendar.getInstance();

    Calendar cal2 = Calendar.getInstance();
    cal2.setTime(reportDate);
    cal2.set(Calendar.DAY_OF_MONTH, cal2.getActualMaximum(Calendar.DAY_OF_MONTH));

    // Get an input stream to the workbook
    InputStream is = ExcelFunctions.class.getResourceAsStream("CKSD_UVMonthly.xlsx");
    XSSFWorkbook wb = new XSSFWorkbook(is);
    FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

    // Pull out the Month and Year
    cal.setTime(reportDate);
    Integer month = cal.get(Calendar.MONTH) + 1;
    Integer year = cal.get(Calendar.YEAR);
    cal.set(Calendar.DAY_OF_MONTH, 1);

    XSSFSheet sheet = wb.getSheet("Monthly Summary Form");
    sheet
        .getRow(1)
        .getCell(3)
        .setCellValue(
            displayFormat.format(cal.getTime()) + " to " + displayFormat.format(cal2.getTime()));

    double totalHours = 0;
    for (ObjectDatasetWrapper.Row row : runHours) {
      Double runTime = (Double) row.getKeyValue("PRKM_ToSystem_Hours", 0.0);
      totalHours += runTime;
    }
    sheet.getRow(9).getCell(3).setCellValue(totalHours);

    double totalFlow = 0;
    for (ObjectDatasetWrapper.Row row : totalProd) {
      Double total = (Double) row.getKeyValue("WEL_PRKM_PM_Daily", 0.0);
      totalFlow += total;
    }
    sheet.getRow(9).getCell(5).setCellValue(totalFlow * 1000 / 1000000);

    int offSpecEvents = 0;
    totalFlow = 0;
    for (ObjectDatasetWrapper.Row row : offSpecData) {
      Double events = (Double) row.getKeyValue("OffSpecEvents", 0.0);
      Double flow = (Double) row.getKeyValue("OffSpecFlow", 0.0);
      offSpecEvents += events;
      totalFlow += flow;
    }
    sheet.getRow(9).getCell(8).setCellValue(offSpecEvents);
    sheet.getRow(9).getCell(11).setCellValue(totalFlow * 1000 / 1000000);
    // Run evaluator to make all in sheet formulas update with the new data
    evaluator.evaluateAll();
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    wb.write(bos);
    return bos.toByteArray();
  }

  public static byte[] _getCreeksideUVOffspec(Date reportDate, ObjectDatasetWrapper offSpecData)
      throws Exception {

    Calendar cal = Calendar.getInstance();

    Calendar cal2 = Calendar.getInstance();
    cal2.setTime(reportDate);
    cal2.set(Calendar.DAY_OF_MONTH, cal2.getActualMaximum(Calendar.DAY_OF_MONTH));

    // Get an input stream to the workbook
    InputStream is = ExcelFunctions.class.getResourceAsStream("UV Off-SpecWaterLog.xlsx");
    XSSFWorkbook wb = new XSSFWorkbook(is);
    FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

    // Pull out the Month and Year
    cal.setTime(reportDate);
    Integer month = cal.get(Calendar.MONTH) + 1;
    Integer year = cal.get(Calendar.YEAR);
    cal.set(Calendar.DAY_OF_MONTH, 1);

    XSSFSheet sheet = wb.getSheet("OffSpecLog");
    sheet.getRow(6).getCell(2).setCellValue(displayFormat.format(cal.getTime()));
    cal.set(Calendar.DAY_OF_MONTH, 1);
    sheet
        .getRow(1)
        .getCell(2)
        .setCellValue(
            displayFormat.format(cal.getTime()) + " to " + displayFormat.format(cal2.getTime()));

    int rowIdx = 10;
    for (ObjectDatasetWrapper.Row row : offSpecData) {
      Date t_stamp = (Date) row.getKeyValue("t_stamp");
      Calendar c = Calendar.getInstance();
      c.setTime(t_stamp);
      Integer timeMins = (Integer) row.getKeyValue("timeMins", -1);
      Double offSpecFlow = (Double) row.getKeyValue("volume", -1.0);
      sheet.getRow(rowIdx).getCell(1).setCellValue(excelShortDateFormat.format(t_stamp));
      sheet.getRow(rowIdx).getCell(2).setCellValue(hoursMinsSecs.format(t_stamp));
      sheet.getRow(rowIdx).getCell(9).setCellValue(timeMins);
      sheet.getRow(rowIdx).getCell(11).setCellValue(offSpecFlow);
      rowIdx++;
    }
    // Run evaluator to make all in sheet formulas update with the new data
    evaluator.evaluateAll();
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    wb.write(bos);
    return bos.toByteArray();
  }

  public static byte[] _getQuinnsSheetMnO2(
      Date reportDate,
      ObjectDatasetWrapper FiveMinData,
      ObjectDatasetWrapper rackResults,
      ObjectDatasetWrapper WQData)
      throws Exception {

    Calendar cal = Calendar.getInstance();
    cal.setTime(reportDate);

    // Get an input stream to the workbook
    InputStream is = ExcelFunctions.class.getResourceAsStream("Quinns Monthly Report MN02.xlsx");
    XSSFWorkbook wb = new XSSFWorkbook(is);
    FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

    // Pull out the Month and Year
    cal.setTime(reportDate);
    int month = cal.get(Calendar.MONTH) + 1;
    int year = cal.get(Calendar.YEAR);

    // EPA Monthly Summary
    Map<String, String> EPA_params = new HashMap<>();
    EPA_params.put("sheet_name", "EPA Monthly Summary");
    EPA_params.put("cl2_column", "AI0533_EffluentCL2");
    EPA_params.put("date", "t_stamp");
    // EPA Monthly Summary
    HelperFunctions.EPA_Summary(FiveMinData, wb, month, year, EPA_params);

    // Turbidity Worksheet
    Map<String, String> turb_params = new HashMap<>();
    turb_params.put("sheet_name", "Turbidity Daily Data Sheet");
    turb_params.put("date", "t_stamp");
    turb_params.put("12AM_turb", "QJ_12AM_Turb");
    turb_params.put("4AM_turb", "QJ_4AM_Turb");
    turb_params.put("8AM_turb", "QJ_8AM_Turb");
    turb_params.put("12PM_turb", "QJ_12PM_Turb");
    turb_params.put("4PM_turb", "QJ_4PM_Turb");
    turb_params.put("8PM_turb", "QJ_8PM_Turb");
    HelperFunctions.Turbidity_Sheet(WQData, wb, month, year, turb_params);

    // DI
    // Testing--------------------------------------------------------------------------------------------------------------------------------------------
    // Unit(1) DI Testing
    HelperFunctions.Quinn_DI_Testing(FiveMinData, rackResults, wb, "1", month, year);
    // Unit(2) DI Testing
    HelperFunctions.Quinn_DI_Testing(FiveMinData, rackResults, wb, "2", month, year);
    // Unit(3) DI Testing
    HelperFunctions.Quinn_DI_Testing(FiveMinData, rackResults, wb, "3", month, year);
    // Unit(4) DI Testing
    HelperFunctions.Quinn_DI_Testing(FiveMinData, rackResults, wb, "4", month, year);
    // Unit(5) DI Testing
    HelperFunctions.Quinn_DI_Testing(FiveMinData, rackResults, wb, "5", month, year);

    // Operational
    // Worksheet---------------------------------------------------------------------------------------------------------------------------------
    Map<String, String> op_params = new HashMap<>();
    op_params.put("cl2_column", "AI0533_EffluentCL2");
    op_params.put("flow1", "FI0510_Fairway");
    op_params.put("flow2", "FI0520_BootHill");
    op_params.put("water_temp", "TI3008_RawWaterTemp");
    op_params.put("clearwell_level", "LI0510_ClearWellLevel");
    op_params.put("plant_running", "PlantRunning");
    op_params.put("date", "t_stamp");
    op_params.put("num_trains_online", "MN02_TrainsOnline");
    HelperFunctions.Quinn_Operational_Sheet_MN02(FiveMinData, wb, op_params);

    // Disinfection
    // Report----------------------------------------------------------------------------------------------------------------------------------
    // Quinn_Disinfection(FiveMinData, wb, month, year);

    // Sequence Dates
    // ---------------------------------------------------------------------------------------------------------------------------------------
    HelperFunctions.Quinn_Sequence_MNO2(cal, year, wb);

    // Run evaluator to make all in sheet formulas update with the new data
    evaluator.evaluateAll();
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    wb.write(bos);
    return bos.toByteArray();
  }

  public static byte[] _getOgdensSheet(
      Date reportDate,
      ObjectDatasetWrapper operationalData,
      ObjectDatasetWrapper rackResults,
      ObjectDatasetWrapper WQData,
      ObjectDatasetWrapper turbidity,
      ObjectDatasetWrapper rack2Results,
      ObjectDatasetWrapper rack3Results,
      ObjectDatasetWrapper rack4Results,
      ObjectDatasetWrapper rack5Results,
      ObjectDatasetWrapper rack6Results,
      ObjectDatasetWrapper rack7Results,
      ObjectDatasetWrapper rack8Results,
      ObjectDatasetWrapper rack9Results)
      throws Exception {
    Calendar cal = Calendar.getInstance();
    cal.setTime(reportDate);
    // Get an input stream to the workbook
    InputStream is = ExcelFunctions.class.getResourceAsStream("OgdenReport.xlsx");
    XSSFWorkbook wb = new XSSFWorkbook(is);
    FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();
    // Pull out the Month and Year
    cal.setTime(reportDate);
    int month = cal.get(Calendar.MONTH) + 1;
    int year = cal.get(Calendar.YEAR);

    // Ogden Summary Sheet
    HelperFunctions.Ogden_EPA_Summary(operationalData, wb, month, year);

    // Ogden Operational Sheet
    HelperFunctions.Ogden_Operational_Sheet(operationalData, wb);

    // Turbidity Sheet
    Map<String, String> turb_params = new HashMap<>();
    turb_params.put("sheet_name", "Turbidity Daily Data Sheet");
    turb_params.put("date", "t_stamp");
    turb_params.put("12AM_turb", "CFE_12AM");
    turb_params.put("4AM_turb", "CFE_4AM");
    turb_params.put("8AM_turb", "CFE_8AM");
    turb_params.put("12PM_turb", "CFE_12PM");
    turb_params.put("4PM_turb", "CFE_4PM");
    turb_params.put("8PM_turb", "CFE_8PM");
    HelperFunctions.Turbidity_Sheet(turbidity, wb, month, year, turb_params);

    HelperFunctions.Ogden_DI(rackResults, month, year, wb, "1");
    HelperFunctions.Ogden_DI(rack2Results, month, year, wb, "2");
    HelperFunctions.Ogden_DI(rack3Results, month, year, wb, "3");
    HelperFunctions.Ogden_DI(rack4Results, month, year, wb, "4");
    HelperFunctions.Ogden_DI(rack5Results, month, year, wb, "5");
    HelperFunctions.Ogden_DI(rack6Results, month, year, wb, "6");
    HelperFunctions.Ogden_DI(rack7Results, month, year, wb, "7");
    HelperFunctions.Ogden_DI(rack8Results, month, year, wb, "8");
    HelperFunctions.Ogden_DI(rack9Results, month, year, wb, "9");

    HelperFunctions.Ogden_Disinfection(cal, operationalData, wb, month, year);

    HelperFunctions.Ogden_Sequence_1(cal, WQData, wb, year);

    // Run evaluator to make all in sheet formulas update with the new data
    evaluator.evaluateAll();
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    wb.write(bos);
    return bos.toByteArray();
  }

  public static byte[] _getGroundWaterDisinfection(
      Date reportDate, ObjectDatasetWrapper groundWaterData, ObjectDatasetWrapper hypoSpeed)
      throws Exception {
    Calendar cal = Calendar.getInstance();
    cal.setTime(reportDate);
    int year = cal.get(Calendar.YEAR);
    int month = cal.get(Calendar.MONTH) + 1;
    InputStream is = ExcelFunctions.class.getResourceAsStream("GW DBPR.xlsx");
    XSSFWorkbook wb = new XSSFWorkbook(is);
    FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();
    XSSFSheet sheet = wb.getSheet("Groundwater Disinfection");
    boolean firstLoop = true;
    boolean secondLoop = true;
    double MDSCTot = 0;
    double PRKMTot = 0;
    double DIVTot = 0;
    double SpiroTot1 = 0;
    double SpiroTot2 = 0;
    double SpiroTot3 = 0;
    int test = 13;
    for (ObjectDatasetWrapper.Row row : groundWaterData) {
      double MDSCTotalFlow = (double) row.getKeyValue("MDSCTotalFlow", 0.0);
      double MDSCTankLevel = (double) row.getKeyValue("MDSCTankLevel", 0.0);
      double PRKMTotalFlow = (double) row.getKeyValue("PRKMTotalFlow", 0.0);
      double PRKMTankLevel = (double) row.getKeyValue("PRKMTankLevel", 0.0);
      double DIVTotalFlow = (double) row.getKeyValue("DIVTotalFlow", 0.0);
      double DIVTankLevel = (double) row.getKeyValue("DIVTankLevel", 0.0);
      double SpiroFilter1TotalFlow = (double) row.getKeyValue("SpiroFilter1TotalFlow", 0.0);
      double SpiroFilter2TotalFlow = (double) row.getKeyValue("SpiroFilter2TotalFlow", 0.0);
      double SpiroFilter3TotalFlow = (double) row.getKeyValue("SpiroFilter3TotalFlow", 0.0);
      double SpiroTankLevel = (double) row.getKeyValue("SpiroTankLevel", 0.0);

      if (firstLoop) {
        firstLoop = false;
        MDSCTot = MDSCTotalFlow;
        PRKMTot = PRKMTotalFlow;
        DIVTot = DIVTotalFlow;
        SpiroTot1 = SpiroFilter1TotalFlow;
        SpiroTot2 = SpiroFilter2TotalFlow;
        SpiroTot3 = SpiroFilter3TotalFlow;
      } else if (secondLoop) {
        secondLoop = false;

        sheet.getRow(test).getCell(1).setCellValue(MDSCTotalFlow);
        sheet.getRow(test).getCell(2).setCellValue(MDSCTotalFlow - MDSCTot);
        sheet.getRow(test).getCell(4).setCellValue(MDSCTankLevel);

        sheet.getRow(test).getCell(9).setCellValue(DIVTotalFlow);
        sheet.getRow(test).getCell(10).setCellValue(DIVTotalFlow - DIVTot);
        sheet.getRow(test).getCell(12).setCellValue(DIVTankLevel);

        sheet.getRow(test).getCell(25).setCellValue(PRKMTotalFlow);
        sheet.getRow(test).getCell(26).setCellValue(PRKMTotalFlow - PRKMTot);
        sheet.getRow(test).getCell(28).setCellValue(PRKMTankLevel);

        sheet.getRow(test).getCell(20).setCellValue(SpiroTankLevel);
        sheet.getRow(test).getCell(33).setCellValue(SpiroFilter1TotalFlow);
        sheet.getRow(test).getCell(34).setCellValue(SpiroFilter1TotalFlow - SpiroTot1);
        sheet.getRow(test).getCell(36).setCellValue(SpiroTankLevel);
        sheet.getRow(test).getCell(41).setCellValue(SpiroFilter2TotalFlow);
        sheet.getRow(test).getCell(42).setCellValue(SpiroFilter2TotalFlow - SpiroTot2);
        sheet.getRow(test).getCell(44).setCellValue(SpiroTankLevel);
        sheet.getRow(test).getCell(49).setCellValue(SpiroFilter3TotalFlow);
        sheet.getRow(test).getCell(50).setCellValue(SpiroFilter3TotalFlow - SpiroTot3);
        sheet.getRow(test).getCell(52).setCellValue(SpiroTankLevel);
        test++;

      } else {
        sheet.getRow(test).getCell(1).setCellValue(MDSCTotalFlow);
        sheet.getRow(test).getCell(4).setCellValue(MDSCTankLevel);

        sheet.getRow(test).getCell(9).setCellValue(DIVTotalFlow);
        sheet.getRow(test).getCell(12).setCellValue(DIVTankLevel);

        sheet.getRow(test).getCell(25).setCellValue(PRKMTotalFlow);
        sheet.getRow(test).getCell(28).setCellValue(PRKMTankLevel);

        sheet.getRow(test).getCell(20).setCellValue(SpiroTankLevel);
        sheet.getRow(test).getCell(33).setCellValue(SpiroFilter1TotalFlow);
        sheet.getRow(test).getCell(36).setCellValue(SpiroTankLevel);
        sheet.getRow(test).getCell(41).setCellValue(SpiroFilter2TotalFlow);
        sheet.getRow(test).getCell(44).setCellValue(SpiroTankLevel);
        sheet.getRow(test).getCell(49).setCellValue(SpiroFilter3TotalFlow);
        sheet.getRow(test).getCell(52).setCellValue(SpiroTankLevel);
        test++;
      }
    }
    int day, prevDay = 0, rowIdx, count = 1;
    double MDSCSpeed = 0, PRKMSpeed = 0, DIVSpeed = 0, SpiroSpeed = 0;
    double MDSCLog = 0, PRKMLog = 0, DIVLog = 0, SpiroLog = 0;
    boolean fLoop = true;
    for (ObjectDatasetWrapper.Row row : hypoSpeed) {
      double MDSCHypoSpeed = (double) row.getKeyValue("MDSCHypoSpeed", 0.0);
      double PRKMHypoSpeed = (double) row.getKeyValue("PRKMHypoSpeed", 0.0);
      double DIVHypoSpeed = (double) row.getKeyValue("DIVHypoSpeed", 0.0);
      double SpiroHypoSpeed = (double) row.getKeyValue("SpiroHypoSpeed", 0.0);
      Date t_stamp = (Date) row.getKeyValue("t_stamp");
      cal.setTime(t_stamp);
      day = cal.get(Calendar.DAY_OF_MONTH);
      if (fLoop) {
        prevDay = day;
        fLoop = false;
      }

      if (day != prevDay) {
        rowIdx = 13 + prevDay - 1;
        if (MDSCSpeed == 0) {
          sheet.getRow(rowIdx).getCell(3).setCellValue(0);
        } else {
          MDSCSpeed = (MDSCSpeed / MDSCLog) * .60;
          sheet.getRow(rowIdx).getCell(3).setCellValue((int) MDSCSpeed + " Hz");
        }
        if (PRKMSpeed == 0) {
          sheet.getRow(rowIdx).getCell(27).setCellValue(0);
        } else {
          PRKMSpeed = (PRKMSpeed / PRKMLog) * .60;
          sheet.getRow(rowIdx).getCell(27).setCellValue((int) PRKMSpeed + " Hz");
        }
        if (DIVSpeed == 0) {
          sheet.getRow(rowIdx).getCell(11).setCellValue(0);
        } else {
          DIVSpeed = (DIVSpeed / DIVLog) * .60;
          sheet.getRow(rowIdx).getCell(11).setCellValue((int) DIVSpeed + " Hz");
        }
        if (SpiroSpeed == 0) {
          sheet.getRow(rowIdx).getCell(19).setCellValue(0);
          sheet.getRow(rowIdx).getCell(35).setCellValue(0);
          sheet.getRow(rowIdx).getCell(43).setCellValue(0);
          sheet.getRow(rowIdx).getCell(51).setCellValue(0);
        } else {
          SpiroSpeed = (SpiroSpeed / SpiroLog) * .60;
          sheet.getRow(rowIdx).getCell(19).setCellValue((int) SpiroSpeed + " Hz");
          sheet.getRow(rowIdx).getCell(35).setCellValue((int) SpiroSpeed + " Hz");
          sheet.getRow(rowIdx).getCell(43).setCellValue((int) SpiroSpeed + " Hz");
          sheet.getRow(rowIdx).getCell(51).setCellValue((int) SpiroSpeed + " Hz");
        }
        prevDay = day;
        MDSCSpeed = 0;
        PRKMSpeed = 0;
        DIVSpeed = 0;
        SpiroSpeed = 0;
        MDSCLog = 0;
        PRKMLog = 0;
        DIVLog = 0;
        SpiroLog = 0;
      }
      if (MDSCHypoSpeed > 1) {
        MDSCSpeed = MDSCSpeed + MDSCHypoSpeed;
        MDSCLog++;
      }
      if (PRKMHypoSpeed > 1) {
        PRKMSpeed = PRKMSpeed + PRKMHypoSpeed;
        PRKMLog++;
      }
      if (DIVHypoSpeed > 1) {
        DIVSpeed = DIVSpeed + DIVHypoSpeed;
        DIVLog++;
      }
      if (SpiroHypoSpeed > 1) {
        SpiroSpeed = SpiroSpeed + SpiroHypoSpeed;
        SpiroLog++;
      }
      if (count == hypoSpeed.getSize()) {
        rowIdx = 13 + day - 1;
        if (MDSCSpeed == 0) {
          sheet.getRow(rowIdx).getCell(3).setCellValue(0);
        } else {
          MDSCSpeed = (MDSCSpeed / MDSCLog) * .60;
          sheet.getRow(rowIdx).getCell(3).setCellValue((int) MDSCSpeed + " Hz");
        }
        if (PRKMSpeed == 0) {
          sheet.getRow(rowIdx).getCell(27).setCellValue(0);
        } else {
          PRKMSpeed = (PRKMSpeed / PRKMLog) * .60;
          sheet.getRow(rowIdx).getCell(27).setCellValue((int) PRKMSpeed + " Hz");
        }
        if (DIVSpeed == 0) {
          sheet.getRow(rowIdx).getCell(11).setCellValue(0);
        } else {
          DIVSpeed = (DIVSpeed / DIVLog) * .60;
          sheet.getRow(rowIdx).getCell(11).setCellValue((int) DIVSpeed + " Hz");
        }
        if (SpiroSpeed == 0) {
          sheet.getRow(rowIdx).getCell(19).setCellValue(0);
          sheet.getRow(rowIdx).getCell(35).setCellValue(0);
          sheet.getRow(rowIdx).getCell(43).setCellValue(0);
          sheet.getRow(rowIdx).getCell(51).setCellValue(0);
        } else {
          SpiroSpeed = (SpiroSpeed / SpiroLog) * .60;
          sheet.getRow(rowIdx).getCell(19).setCellValue((int) SpiroSpeed + " Hz");
          sheet.getRow(rowIdx).getCell(35).setCellValue((int) SpiroSpeed + " Hz");
          sheet.getRow(rowIdx).getCell(43).setCellValue((int) SpiroSpeed + " Hz");
          sheet.getRow(rowIdx).getCell(51).setCellValue((int) SpiroSpeed + " Hz");
        }
      } else {
        count++;
      }
    }
    // Run evaluator to make all in sheet formulas update with the new data
    evaluator.evaluateAll();
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    wb.write(bos);
    return bos.toByteArray();
  }

  public static byte[] _getGroundWaterDisinfectionNoPM(
      Date reportDate, ObjectDatasetWrapper groundWaterData, ObjectDatasetWrapper hypoSpeed)
      throws Exception {
    Calendar cal = Calendar.getInstance();
    cal.setTime(reportDate);
    InputStream is = ExcelFunctions.class.getResourceAsStream("GW DBPR No PM.xlsx");
    XSSFWorkbook wb = new XSSFWorkbook(is);
    FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();
    XSSFSheet sheet = wb.getSheet("Groundwater Disinfection");

    String strMonth = new SimpleDateFormat("MMMM").format(reportDate);
    String strYear = new SimpleDateFormat("yyyy").format(reportDate);
    sheet.getRow(4).getCell(2).setCellValue(strMonth);
    sheet.getRow(5).getCell(2).setCellValue(strYear);

    boolean firstLoop = true;
    boolean secondLoop = true;
    double MDSCTot = 0;
    double DIVTot = 0;
    double THIRTot = 0;
    int test = 13;
    for (ObjectDatasetWrapper.Row row : groundWaterData) {
      double MDSCTotalFlow = (double) row.getKeyValue("MDSCTotalFlow", 0.0);
      double MDSCTankLevel = (double) row.getKeyValue("MDSCTankLevel", 0.0);
      double DIVTotalFlow = (double) row.getKeyValue("DIVTotalFlow", 0.0);
      double DIVTankLevel = (double) row.getKeyValue("DIVTankLevel", 0.0);
      double THIRTotalFlow = (double) row.getKeyValue("THIRTotalFlow", 0.0);
      double THIRTankTotal = (double) row.getKeyValue("THIRTankTotal", 0.0);

      if (firstLoop) {
        firstLoop = false;
        MDSCTot = MDSCTotalFlow;
        DIVTot = DIVTotalFlow;
        THIRTot = THIRTotalFlow;
      } else if (secondLoop) {
        secondLoop = false;

        sheet.getRow(test).getCell(1).setCellValue(MDSCTotalFlow);
        sheet.getRow(test).getCell(2).setCellValue(MDSCTotalFlow - MDSCTot);
        sheet.getRow(test).getCell(4).setCellValue(MDSCTankLevel);

        sheet.getRow(test).getCell(9).setCellValue(DIVTotalFlow);
        sheet.getRow(test).getCell(10).setCellValue(DIVTotalFlow - DIVTot);
        sheet.getRow(test).getCell(12).setCellValue(DIVTankLevel);

        sheet.getRow(test).getCell(17).setCellValue(THIRTotalFlow);
        sheet.getRow(test).getCell(18).setCellValue(THIRTotalFlow - THIRTot);
        sheet.getRow(test).getCell(20).setCellValue(THIRTankTotal);

        test++;
      } else {
        sheet.getRow(test).getCell(1).setCellValue(MDSCTotalFlow);
        sheet.getRow(test).getCell(4).setCellValue(MDSCTankLevel);

        sheet.getRow(test).getCell(9).setCellValue(DIVTotalFlow);
        sheet.getRow(test).getCell(12).setCellValue(DIVTankLevel);

        sheet.getRow(test).getCell(17).setCellValue(THIRTotalFlow);
        sheet.getRow(test).getCell(20).setCellValue(THIRTankTotal);

        test++;
      }
    }
    int day, prevDay = 0, rowIdx, count = 1;
    double MDSCSpeed = 0, DIVSpeed = 0, THIRSpeed = 0;
    double MDSCLog = 0, DIVLog = 0, THIRLog = 0;
    boolean fLoop = true;
    for (ObjectDatasetWrapper.Row row : hypoSpeed) {
      double MDSCHypoSpeed = (double) row.getKeyValue("MDSCHypoSpeed", 0.0);
      double DIVHypoSpeed = (double) row.getKeyValue("DIVHypoSpeed", 0.0);
      double THIRCL2Speed = (double) row.getKeyValue("ThiriotCL2Speed", 0.0);
      Date t_stamp = (Date) row.getKeyValue("t_stamp");
      cal.setTime(t_stamp);
      day = cal.get(Calendar.DAY_OF_MONTH);
      if (fLoop) {
        prevDay = day;
        fLoop = false;
      }

      if (day != prevDay) {
        rowIdx = 13 + prevDay - 1;
        if (MDSCSpeed == 0) {
          sheet.getRow(rowIdx).getCell(3).setCellValue(0);
        } else {
          MDSCSpeed = (MDSCSpeed / MDSCLog) * .60;
          sheet.getRow(rowIdx).getCell(3).setCellValue((int) MDSCSpeed + " Hz");
        }

        if (DIVSpeed == 0) {
          sheet.getRow(rowIdx).getCell(11).setCellValue(0);
        } else {
          DIVSpeed = (DIVSpeed / DIVLog) * .60;
          sheet.getRow(rowIdx).getCell(11).setCellValue((int) DIVSpeed + " Hz");
        }

        if (THIRSpeed == 0) {
          sheet.getRow(rowIdx).getCell(19).setCellValue(0);
        } else {
          THIRSpeed = (THIRSpeed / THIRLog);
          sheet.getRow(rowIdx).getCell(19).setCellValue((int) THIRSpeed + " PPD");
        }
        prevDay = day;
        MDSCSpeed = 0;
        DIVSpeed = 0;
        THIRSpeed = 0;
        MDSCLog = 0;
        DIVLog = 0;
        THIRLog = 0;
      }
      if (MDSCHypoSpeed > 1) {
        MDSCSpeed = MDSCSpeed + MDSCHypoSpeed;
        MDSCLog++;
      }

      if (DIVHypoSpeed > 1) {
        DIVSpeed = DIVSpeed + DIVHypoSpeed;
        DIVLog++;
      }

      if (THIRCL2Speed > 1) {
        THIRSpeed = THIRSpeed + THIRCL2Speed;
        THIRLog++;
      }
      if (count == hypoSpeed.getSize()) {
        rowIdx = 13 + day - 1;
        if (MDSCSpeed == 0) {
          sheet.getRow(rowIdx).getCell(3).setCellValue(0);
        } else {
          MDSCSpeed = (MDSCSpeed / MDSCLog) * .60;
          sheet.getRow(rowIdx).getCell(3).setCellValue((int) MDSCSpeed + " Hz");
        }

        if (DIVSpeed == 0) {
          sheet.getRow(rowIdx).getCell(11).setCellValue(0);
        } else {
          DIVSpeed = (DIVSpeed / DIVLog) * .60;
          sheet.getRow(rowIdx).getCell(11).setCellValue((int) DIVSpeed + " Hz");
        }

        if (THIRSpeed == 0) {
          sheet.getRow(rowIdx).getCell(19).setCellValue(0);
        } else {
          THIRSpeed = (THIRSpeed / THIRLog);
          sheet.getRow(rowIdx).getCell(19).setCellValue((int) THIRSpeed + " PPD");
        }
      } else {
        count++;
      }
    }
    // Run evaluator to make all in sheet formulas update with the new data
    evaluator.evaluateAll();
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    wb.write(bos);
    return bos.toByteArray();
  }

  // Description: completes the SewerFlows spreadsheet by populating the cells with data passed in
  // via SewerFlows
  // Inputs: the date the report is made, A sewerflows dataset
  // Returns: a byte array that can be converted to a XLS file
  public static byte[] _getQuinnsFlows(Date reportDate, ObjectDatasetWrapper SewerFlows)
      throws Exception {
    // Reference the Quinns report
    InputStream is = ExcelFunctions.class.getResourceAsStream("Quinns Sewer Flows.xlsx");
    XSSFWorkbook wb = new XSSFWorkbook(is);
    FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();
    XSSFSheet sheet = wb.getSheet("Daily Sewer Flows");

    // Get the date from the reportDate
    Calendar cal = Calendar.getInstance();
    cal.setTime(reportDate);
    int year = cal.get(Calendar.YEAR);
    int month = cal.get(Calendar.MONTH) + 1;
    // Initial Starting point to store data
    final int TIMECOL = 0, PLANTFLOWCOL = 1, PLATESETTLERCOL = 2, DRAINFLOWCOL = 3;
    // set the date
    sheet.getRow(0).getCell(2).setCellValue(month);
    sheet.getRow(1).getCell(2).setCellValue(year);

    for (ObjectDatasetWrapper.Row row : SewerFlows) {
      double bootHill = (double) row.getKeyValue("BootHill", 0.0);
      double fairwayHills = (double) row.getKeyValue("FairwayHills", 0.0);
      double plateSettler = ((double) row.getKeyValue("Solids", 0.0) / 1000);
      double drainFlow = ((double) row.getKeyValue("Waste", 0.0) / 1000);

      Date t_stamp = (Date) row.getKeyValue("t_stamp");
      cal.setTime(t_stamp);
      // cal.add(Calendar.DAY_OF_MONTH, -1);
      int day = cal.get(Calendar.DAY_OF_MONTH);
      int rowIdxOffset = 6 + day - 1;
      double plantFlow = (bootHill + fairwayHills) / 1000;

      // Store the Values to their respective rows and columns
      sheet.getRow(rowIdxOffset).getCell(TIMECOL).setCellValue(displayFormat.format(cal.getTime()));
      sheet.getRow(rowIdxOffset).getCell(PLANTFLOWCOL).setCellValue(plantFlow);
      sheet.getRow(rowIdxOffset).getCell(PLATESETTLERCOL).setCellValue(plateSettler);
      sheet.getRow(rowIdxOffset).getCell(DRAINFLOWCOL).setCellValue(drainFlow);
    }
    // Run evaluator to make all in sheet formulas update with the new data
    evaluator.evaluateAll();
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    wb.write(bos);
    return bos.toByteArray();
  }

  public static byte[] _getQuinnsMonitoringData(
      Date reportDate,
      ObjectDatasetWrapper production,
      ObjectDatasetWrapper rackResults,
      ObjectDatasetWrapper chemicals)
      throws Exception {
    // Reference the Quinns report
    InputStream is = ExcelFunctions.class.getResourceAsStream("QJ Monitoring.xlsx");
    XSSFWorkbook wb = new XSSFWorkbook(is);
    FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();
    XSSFSheet sheet = wb.getSheet("Production");

    int idx = 9;
    int dayIdx = 1;
    int prevDay = 0;
    for (ObjectDatasetWrapper.Row row : production) {
      Date t_stamp = (Date) row.getKeyValue("t_stamp");
      Double rawTurb = (Double) row.getKeyValue("rawTurb", 0.0);
      Double decantTurb = (Double) row.getKeyValue("decantTurb", 0.0);
      Double decantFlow = (Double) row.getKeyValue("decantFlow", 0.0);
      Double mfFeedTurb = (Double) row.getKeyValue("mfFeedTurb", 0.0);
      Double feedTemp = (Double) row.getKeyValue("feedTemp", 0.0);
      Double strainInPress = (Double) row.getKeyValue("strainInPress", 0.0);
      Double strainOutPress = (Double) row.getKeyValue("strainOutPress", 0.0);
      Double strainerDP = (Double) row.getKeyValue("strainerDp", 0.0);
      Double membraneFeedPress = (Double) row.getKeyValue("memFeedPress", 0.0);
      Double filtratePress = (Double) row.getKeyValue("filtratePress", 0.0);

      Double feedFlow = (Double) row.getKeyValue("feedFlow", 0.0);
      Double filtrateFlow = (Double) row.getKeyValue("filtrateFlow", 0.0);
      Double XRFlow = (Double) row.getKeyValue("XRFlow", 0.0);
      Double plantRecoveryToday = (Double) row.getKeyValue("plantRecoveryToday", 0.0);
      Double combinedFiltrateTurb = (Double) row.getKeyValue("combinedFiltrateTurb", 0.0);
      Double clearWellLevel = (Double) row.getKeyValue("clearWellLevel", 0.0);
      Double feedVolume = (Double) row.getKeyValue("feedVolume", 0.0);
      Double strainerBackwashVolume = (Double) row.getKeyValue("strainerBackwashVolume", 0.0);
      Double netFiltrateVolume = (Double) row.getKeyValue("netFiltrateVolume", 0.0);

      Calendar cal = Calendar.getInstance();
      cal.setTime(t_stamp);
      int curDay = cal.get(Calendar.DAY_OF_MONTH);
      if (curDay != prevDay) {
        dayIdx = 1;
      }
      prevDay = curDay;

      sheet.getRow(idx).getCell(0).setCellValue(dayIdx);
      sheet.getRow(idx).getCell(1).setCellValue(excelShortDateFormat.format(t_stamp));
      sheet.getRow(idx).getCell(2).setCellValue(hoursMinsSecs.format(t_stamp));
      sheet.getRow(idx).getCell(3).setCellValue(rawTurb);
      sheet.getRow(idx).getCell(4).setCellValue(decantTurb);
      sheet.getRow(idx).getCell(5).setCellValue(decantFlow);
      sheet.getRow(idx).getCell(6).setCellValue(mfFeedTurb);
      sheet.getRow(idx).getCell(7).setCellValue(feedTemp);
      sheet.getRow(idx).getCell(8).setCellValue(strainInPress);
      sheet.getRow(idx).getCell(9).setCellValue(strainOutPress);
      sheet.getRow(idx).getCell(10).setCellValue(strainerDP);
      sheet.getRow(idx).getCell(11).setCellValue(membraneFeedPress);
      sheet.getRow(idx).getCell(12).setCellValue(filtratePress);
      sheet.getRow(idx).getCell(13).setCellValue(feedFlow);
      sheet.getRow(idx).getCell(14).setCellValue(filtrateFlow);
      sheet.getRow(idx).getCell(15).setCellValue(XRFlow);
      sheet.getRow(idx).getCell(16).setCellValue(plantRecoveryToday);
      sheet.getRow(idx).getCell(17).setCellValue(combinedFiltrateTurb);
      sheet.getRow(idx).getCell(18).setCellValue(clearWellLevel);
      sheet.getRow(idx).getCell(19).setCellValue(feedVolume);
      sheet.getRow(idx).getCell(20).setCellValue(strainerBackwashVolume);
      sheet.getRow(idx).getCell(21).setCellValue(netFiltrateVolume);
      idx++;
      dayIdx++;
    }

    // Get Rack1's sheet reset everything
    sheet = wb.getSheet("Rack 1");
    idx = 9;
    dayIdx = 1;
    prevDay = 0;

    // for(ObjectDatasetWrapper.Row row : rackResults) {
    // }
    // Run evaluator to make all in sheet formulas update with the new data
    evaluator.evaluateAll();
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    wb.write(bos);
    return bos.toByteArray();
  }

  public static byte[] _getMembraneReport(
      Date reportDate,
      ObjectDatasetWrapper production,
      ObjectDatasetWrapper rackResults,
      ObjectDatasetWrapper IT_data)
      throws Exception {

    InputStream is = ExcelFunctions.class.getResourceAsStream("Single Rack.xlsx");
    XSSFWorkbook wb = new XSSFWorkbook(is);
    FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();
    XSSFSheet sheet = wb.getSheet("Production");

    int idx = 9;
    int dayIdx = 1;
    int prevDay = 0;
    for (ObjectDatasetWrapper.Row row : production) {
      Date t_stamp = (Date) row.getKeyValue("t_stamp");
      Double rawTurb = (Double) row.getKeyValue("rawWaterTurb", 0.0);
      Double decantTurb = (Double) row.getKeyValue("decantTurb", 0.0);
      Double decantFlow = (Double) row.getKeyValue("decantFlow", 0.0);
      Double mfFeedTurb = (Double) row.getKeyValue("MFTurb", 0.0);
      Double feedTemp = (Double) row.getKeyValue("feedTemp", 0.0);
      Double strainInPress = (Double) row.getKeyValue("strainerInletPress", 0.0);
      Double strainOutPress = (Double) row.getKeyValue("strainerOutletPress", 0.0);
      Double strainerDP = (Double) row.getKeyValue("strainerDP", 0.0);
      Double membraneFeedPress = (Double) row.getKeyValue("membraneFeedPress", 0.0);
      Double filtratePress = (Double) row.getKeyValue("filtratePress", 0.0);
      Double feedFlow = (Double) row.getKeyValue("feedFlow", 0.0);
      Double filtrateFlow = (Double) row.getKeyValue("filtFlow", 0.0);
      Double XRFlow = (Double) row.getKeyValue("XRFlow", 0.0);
      Double plantRecoveryToday = (Double) row.getKeyValue("plantRecovery", 0.0);
      Double combinedFiltrateTurb = (Double) row.getKeyValue("combinedFiltTurb", 0.0);
      Double clearWellLevel = (Double) row.getKeyValue("clearWellLevel", 0.0);
      Double feedVolume = (Double) row.getKeyValue("feedVolumeGal", 0.0);
      Double strainerBackwashVolume = (Double) row.getKeyValue("strainerBackwashVol", 0.0);
      Double netFiltrateVolume = (Double) row.getKeyValue("netFiltrateVol", 0.0);

      Calendar cal = Calendar.getInstance();
      cal.setTime(t_stamp);
      int curDay = cal.get(Calendar.DAY_OF_MONTH);
      if (curDay != prevDay) {
        dayIdx = 1;
      }
      prevDay = curDay;
      //
      sheet.getRow(idx).getCell(0).setCellValue(dayIdx);
      //            sheet.getRow(idx).getCell(1).setCellValue(excelShortDateFormat.format(t_stamp));
      sheet.getRow(idx).getCell(1).setCellValue(t_stamp);
      //            sheet.getRow(idx).getCell(2).setCellValue(hoursMinsSecs.format(t_stamp));
      sheet.getRow(idx).getCell(2).setCellValue(t_stamp);
      sheet.getRow(idx).getCell(3).setCellValue(rawTurb);
      sheet.getRow(idx).getCell(4).setCellValue(decantTurb);
      sheet.getRow(idx).getCell(5).setCellValue(decantFlow);
      sheet.getRow(idx).getCell(6).setCellValue(mfFeedTurb);
      sheet.getRow(idx).getCell(7).setCellValue(feedTemp);
      sheet.getRow(idx).getCell(8).setCellValue(strainInPress);
      sheet.getRow(idx).getCell(9).setCellValue(strainOutPress);
      sheet.getRow(idx).getCell(10).setCellValue(strainerDP);
      sheet.getRow(idx).getCell(11).setCellValue(membraneFeedPress);
      sheet.getRow(idx).getCell(12).setCellValue(filtratePress);
      sheet.getRow(idx).getCell(13).setCellValue(feedFlow);
      sheet.getRow(idx).getCell(14).setCellValue(filtrateFlow);
      sheet.getRow(idx).getCell(15).setCellValue(XRFlow);
      sheet.getRow(idx).getCell(16).setCellValue(plantRecoveryToday);
      sheet.getRow(idx).getCell(17).setCellValue(combinedFiltrateTurb);
      sheet.getRow(idx).getCell(18).setCellValue(clearWellLevel);
      sheet.getRow(idx).getCell(19).setCellValue(feedVolume);
      sheet.getRow(idx).getCell(20).setCellValue(strainerBackwashVolume);
      sheet.getRow(idx).getCell(21).setCellValue(netFiltrateVolume);
      idx++;
      dayIdx++;
    }

    // Get Rack1's sheet reset everything
    sheet = wb.getSheet("Rack");
    idx = 9;
    dayIdx = 1;
    prevDay = 0;

    for (ObjectDatasetWrapper.Row row : rackResults) {
      Date t_stamp = (Date) row.getKeyValue("t_stamp");
      //            Double rawTurb = (Double) row.getKeyValue("rawWaterTurb");
      Integer rackProcess = (Integer) row.getKeyValue("rackProc", 0);
      Integer rackSequence = (Integer) row.getKeyValue("rackStep", 0);
      Double feedPressure = (Double) row.getKeyValue("feedPress", 0.0);
      Double feedTemp = (Double) row.getKeyValue("feedTemp", 0.0);
      Double filtTurb = (Double) row.getKeyValue("filtTurbidity", 0.0);
      Double tmp = (Double) row.getKeyValue("TMP", 0.0);
      Double filtPress = (Double) row.getKeyValue("filtPress", 0.0);
      Double feedFlow = (Double) row.getKeyValue("feedFlow", 0.0);
      Double xrFlow = (Double) row.getKeyValue("XRFlow", 0.0);
      Double filtFlow = (Double) row.getKeyValue("filtFlow", 0.0);
      Double rackVolToday = (Double) row.getKeyValue("rackVolToday", 0.0);
      Double rackWasteToday = (Double) row.getKeyValue("rackWasteToday", 0.0);
      Double rackRecToday = (Double) row.getKeyValue("rackRecToday", 0.0);
      Double rackFlux = (Double) row.getKeyValue("rackFlux", 0.0);
      Double rackSpecFlux = (Double) row.getKeyValue("rackSpecFlux", 0.0);
      Double rackLRV = (Double) row.getKeyValue("rackLRV", 0.0);
      Double rackFiltTime = (Double) row.getKeyValue("rackFiltTime", 0.0);

      Calendar cal = Calendar.getInstance();
      cal.setTime(t_stamp);
      int curDay = cal.get(Calendar.DAY_OF_MONTH);
      if (curDay != prevDay) {
        dayIdx = 1;
      }
      prevDay = curDay;

      sheet.getRow(idx).getCell(0).setCellValue(dayIdx);
      sheet.getRow(idx).getCell(1).setCellValue(excelShortDateFormat.format(t_stamp));
      sheet.getRow(idx).getCell(1).setCellValue(t_stamp);
      sheet.getRow(idx).getCell(2).setCellValue(hoursMinsSecs.format(t_stamp));
      sheet.getRow(idx).getCell(2).setCellValue(t_stamp);
      sheet.getRow(idx).getCell(3).setCellValue(rackProcess);
      sheet.getRow(idx).getCell(4).setCellValue(rackSequence);
      sheet.getRow(idx).getCell(5).setCellValue(feedPressure);
      sheet.getRow(idx).getCell(6).setCellValue(feedTemp);
      sheet.getRow(idx).getCell(7).setCellValue(filtTurb);
      sheet.getRow(idx).getCell(8).setCellValue(tmp);
      sheet.getRow(idx).getCell(9).setCellValue(filtPress);
      sheet.getRow(idx).getCell(10).setCellValue(feedFlow);
      sheet.getRow(idx).getCell(11).setCellValue(xrFlow);
      sheet.getRow(idx).getCell(12).setCellValue(filtFlow);
      sheet.getRow(idx).getCell(13).setCellValue(rackVolToday);
      sheet.getRow(idx).getCell(14).setCellValue(rackWasteToday);
      sheet.getRow(idx).getCell(15).setCellValue(rackRecToday);
      sheet.getRow(idx).getCell(16).setCellValue(rackFlux);
      sheet.getRow(idx).getCell(17).setCellValue(rackSpecFlux);
      sheet.getRow(idx).getCell(18).setCellValue(rackLRV);
      sheet.getRow(idx).getCell(19).setCellValue(rackFiltTime);

      idx++;
      dayIdx++;
    }
    sheet = wb.getSheet("Rack IT Data");
    idx = 9;
    prevDay = 0;

    for (ObjectDatasetWrapper.Row row : IT_data) {
      String date = (String) row.getKeyValue("DATE", "NULL");
      String time = (String) row.getKeyValue("TIME", "NULL");
      Date timestamp = (Date) row.getKeyValue("TIMESTAMP");
      Double testTime = (Double) row.getKeyValue("TESTTIME", 0.0);
      Double startPSI = (Double) row.getKeyValue("PRESSATSTART", 0.0);
      Double endPSI = (Double) row.getKeyValue("PRESSATEND", 0.0);
      Double decayPSI = (Double) row.getKeyValue("PRESSCHANGE", 0.0);
      String passFail = (String) row.getKeyValue("PASSFAIL", "NULL");

      Calendar cal = Calendar.getInstance();
      cal.setTime(timestamp);
      int curDay = cal.get(Calendar.DAY_OF_MONTH);
      if (curDay != prevDay) {
        dayIdx = 1;
      } else {
        prevDay = curDay;
      }

      //            sheet.getRow(idx).getCell(0).setCellValue(dayIdx);
      sheet.getRow(idx).getCell(1).setCellValue(date);
      sheet.getRow(idx).getCell(2).setCellValue(time);
      sheet.getRow(idx).getCell(3).setCellValue(testTime);
      sheet.getRow(idx).getCell(4).setCellValue(testTime);
      sheet.getRow(idx).getCell(5).setCellValue(startPSI);
      sheet.getRow(idx).getCell(6).setCellValue(endPSI);

      sheet.getRow(idx).getCell(8).setCellValue(decayPSI);
      sheet.getRow(idx).getCell(9).setCellValue(passFail);
    }

    // Run evaluator to make all in sheet formulas update with the new data
    //       evaluator.evaluateAll();
    wb.setForceFormulaRecalculation(true);
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    wb.write(bos);
    return bos.toByteArray();
  }
  }
