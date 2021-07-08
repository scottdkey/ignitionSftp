package com.skm.apachepoi;

import com.skm.apachepoi.ObjectDatasetWrapper;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

public class HelperFunctions {
  public static SimpleDateFormat displayFormat = new SimpleDateFormat("M/d/yyyy");
  public static SimpleDateFormat excelDateFormat = new SimpleDateFormat("M/d/yy h:mm a");
  public static SimpleDateFormat operationalDateFormat = new SimpleDateFormat("M/d/yyyy h:mm a");
  public static SimpleDateFormat excelShortDateFormat = new SimpleDateFormat("M/d");
  public static SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM");
  public static SimpleDateFormat hoursMinsSecs = new SimpleDateFormat("HH:mm:ss");
  // ----------------------------------------------Helper
  // Functions----------------------------------------------------

  // Description: Completes the DI Testing Sheets for Quinn's
  // Inputs: See parameters
  // Returns: Nothing
  static void Quinn_DI_Testing(
      ObjectDatasetWrapper FiveMinData,
      ObjectDatasetWrapper rackResults,
      XSSFWorkbook wb_in,
      String rack_number,
      int month_in,
      int year_in) {
    // Set the date at the top of the sheet
    XSSFSheet sheet = wb_in.getSheet("Unit(" + rack_number + ") DI Testing");
    sheet.getRow(4).getCell(2).setCellValue(month_in);
    sheet.getRow(5).getCell(2).setCellValue(year_in);

    final int MINFLOW = 100;
    Calendar cal = Calendar.getInstance();

    for (ObjectDatasetWrapper.Row row : rackResults) {
      Date entryTS = (Date) row.getKeyValue("t_stamp");
      Calendar entryCal = Calendar.getInstance();
      entryCal.setTime(entryTS);
      int entryDay = entryCal.get(Calendar.DAY_OF_MONTH);

      Date rackTS = (Date) row.getKeyValue("Rack" + rack_number + "TS");
      try {
        long temp_rackTS = rackTS.getTime();
      } catch (Exception e) {
        rackTS = Calendar.getInstance().getTime();
      }

      String rackResult = (String) row.getKeyValue("Rack" + rack_number + "Result", "NULL");
      cal.setTime(rackTS);
      // Get the hour and day that this check was ran
      Integer rackTSDAY = cal.get(Calendar.DAY_OF_MONTH);
      Integer rackTSMonth = cal.get(Calendar.MONTH) + 1;
      // Throw out entries that aren't from the specified month
      if (rackTSMonth.equals(month_in)) {
        boolean flowOkay = false;
        int rowIdx = 14 + rackTSDAY - 1;

        int resultCheck = entryDay - rackTSDAY;

        // Check the FiveMinData table for a timestamp that matches the DI test
        // if there is one then check that the flow is greater than 100
        for (ObjectDatasetWrapper.Row five_Row : FiveMinData) {
          // Create a calendar object for our fiveMinData table
          Date t_stamp_five_min = (Date) five_Row.getKeyValue("t_stamp");
          Calendar tempCal = Calendar.getInstance();
          tempCal.setTime(t_stamp_five_min);
          Integer fiveMinDay = tempCal.get(Calendar.DAY_OF_MONTH);
          // Check to see if there is a time stamp in FiveMin that matches our Rack table in days,
          // hours and minutes
          // Check the day to make sure the flow never went below 100
          if (fiveMinDay.equals(rackTSDAY)) {
            Double fairwayFlow = (Double) five_Row.getKeyValue("FI0510_Fairway", 0.0);
            Double boothillFlow = (Double) five_Row.getKeyValue("FI0520_BootHill", 0.0);
            // Grab the two flow values and check if they are above the minimum required to validate
            // the rackResult
            if (fairwayFlow + boothillFlow > MINFLOW) {
              flowOkay = true;
              break;
            } else {
              flowOkay = false;
            }
          }
        }
        // If the flow total found in the code above is higher than our min
        // And the date between the two timestamps in rackResults isn't more than a day different,
        // store the value;
        sheet.getRow(rowIdx).getCell(1).setCellValue(excelShortDateFormat.format(rackTS));
        if (flowOkay && resultCheck <= 1) {
          if (rackResult.equals("PASS")) {
            sheet.getRow(rowIdx).getCell(2).setCellValue("Y");
          } else if (rackResult.equals("FAIL")) {
            sheet.getRow(rowIdx).getCell(2).setCellValue("N");
          }
        }
        // If the flow total is too low, put a description in
        else if (!flowOkay) {
          sheet.getRow(rowIdx).getCell(2).setCellValue("PO");
        }
      }
    }
  }

  // Description: Completes the EPA_Summary sheet for Quinn's
  // Inputs: See parameters
  // Returns: Nothing
  // Map parameters: sheet_name, cl2_column, date
  static void EPA_Summary(
      ObjectDatasetWrapper FiveMinData,
      XSSFWorkbook wb_in,
      int month_in,
      int year_in,
      Map<String, String> parameters) {
    XSSFSheet sheet = wb_in.getSheet(parameters.get("sheet_name"));
    sheet.getRow(4).getCell(5).setCellValue(month_in);
    sheet.getRow(5).getCell(5).setCellValue(year_in);
    Calendar cal = Calendar.getInstance();

    int offsetIndex = 29;
    final int COL2RESET = 10, COL3RESET = 20, COL1 = 5, COL2 = 24, COL3 = 43;
    Integer curDay = -1;
    double minRes = 10.00;

    // POE min residual disinfectant residual criteria
    for (ObjectDatasetWrapper.Row row : FiveMinData) {
      Double CL2 = (Double) row.getKeyValue(parameters.get("cl2_column"), 0.0);
      Date t_stamp = (Date) row.getKeyValue(parameters.get("date"));
      Double fairwayFlow = (Double) row.getKeyValue("FI0510_Fairway", 0.0);
      Double boothillFlow = (Double) row.getKeyValue("FI0520_Boothill", 0.0);
      double totalFlow = fairwayFlow + boothillFlow;

      cal.setTime(t_stamp);
      Integer day = cal.get(Calendar.DAY_OF_MONTH);
      int rowIdx = offsetIndex + (day - 1);

      if (!day.equals(curDay)) {
        minRes = 10.00;
        curDay = day;
      } else {
        // If there is a lower residual store it for that day
        if (CL2 != null) {
          if (minRes > CL2 && totalFlow > 20) {
            minRes = CL2;
            // check if the value goes in the center column
            if (rowIdx > 38 && rowIdx < 49) {
              rowIdx = rowIdx - COL2RESET;
              sheet.getRow(rowIdx).getCell(COL2).setCellValue(minRes);
              sheet.getRow(rowIdx).getCell(20).setCellValue(excelShortDateFormat.format(t_stamp));
            }
            // check if it goes in the third column
            else if (rowIdx > 48) {
              rowIdx = rowIdx - COL3RESET;
              sheet.getRow(rowIdx).getCell(COL3).setCellValue(minRes);
              sheet.getRow(rowIdx).getCell(39).setCellValue(excelShortDateFormat.format(t_stamp));
            }
            // it must go in the first column then
            else {
              sheet.getRow(rowIdx).getCell(COL1).setCellValue(minRes);
              sheet.getRow(rowIdx).getCell(1).setCellValue(excelShortDateFormat.format(t_stamp));
            }
          }
        }
      }
    }
  }

  // Description: Completes the EPA_Summary sheet for Quinn's
  // Inputs: See parameters
  // Returns: Nothing
  // Map parameters: sheet_name, cl2_column, date
  static void EPA_SummaryCreekside(
      ObjectDatasetWrapper FiveMinData,
      XSSFWorkbook wb_in,
      int month_in,
      int year_in,
      Map<String, String> parameters,
      Date reportDate) {
    XSSFSheet sheet = wb_in.getSheet(parameters.get("sheet_name"));
    sheet.getRow(4).getCell(5).setCellValue(month_in);
    sheet.getRow(5).getCell(5).setCellValue(year_in);
    Calendar cal = Calendar.getInstance();

    int offsetIndex = 29;
    final int COL2RESET = 10, COL3RESET = 20, COL1 = 5, COL2 = 24, COL3 = 43;
    Integer curDay = -1;
    double minRes = 10.00;

    Calendar cal2 = Calendar.getInstance();
    cal2.setTime(reportDate);
    int days = cal2.get(Calendar.DAY_OF_MONTH);
    for (int i = 1; i <= days; i++) {
      cal2.set(Calendar.DAY_OF_MONTH, i);
      int offset = offsetIndex + (i - 1);
      if (offset > 38 && offset < 49) {
        offset = offset - COL2RESET;
        sheet.getRow(offset).getCell(COL2).setCellValue("PO");
        sheet.getRow(offset).getCell(20).setCellValue(excelShortDateFormat.format(cal2.getTime()));
      }
      // check if it goes in the third column
      else if (offset > 48) {
        offset = offset - COL3RESET;
        sheet.getRow(offset).getCell(COL3).setCellValue("PO");
        sheet.getRow(offset).getCell(39).setCellValue(excelShortDateFormat.format(cal2.getTime()));
      }
      // it must go in the first column then
      else {
        sheet.getRow(offset).getCell(COL1).setCellValue("PO");
        sheet.getRow(offset).getCell(1).setCellValue(excelShortDateFormat.format(cal2.getTime()));
      }
    }

    // POE min residual disinfectant residual criteria
    for (ObjectDatasetWrapper.Row row : FiveMinData) {
      Double CL2 = (Double) row.getKeyValue(parameters.get("cl2_column"), 0.0);
      Date t_stamp = (Date) row.getKeyValue(parameters.get("date"));
      Double divide = (Double) row.getKeyValue("DIVD_Flow", 0.0);
      Double parkmeadow = (Double) row.getKeyValue("PRKM_Flow", 0.0);
      int flushValveClosed = (int) row.getKeyValue("Valve_Closed", 0);

      cal.setTime(t_stamp);
      Integer day = cal.get(Calendar.DAY_OF_MONTH);
      int rowIdx = offsetIndex + (day - 1);

      if (!day.equals(curDay)) {
        minRes = 10.00;
        curDay = day;
      } else {
        // If there is a lower residual store it for that day
        if (CL2 != null) {
          if (minRes > CL2 && parkmeadow > 20) {
            minRes = CL2;
            // check if the value goes in the center column
            if (rowIdx > 38 && rowIdx < 49) {
              rowIdx = rowIdx - COL2RESET;
              sheet.getRow(rowIdx).getCell(COL2).setCellValue(minRes);
              sheet.getRow(rowIdx).getCell(20).setCellValue(excelShortDateFormat.format(t_stamp));
            }
            // check if it goes in the third column
            else if (rowIdx > 48) {
              rowIdx = rowIdx - COL3RESET;
              sheet.getRow(rowIdx).getCell(COL3).setCellValue(minRes);
              sheet.getRow(rowIdx).getCell(39).setCellValue(excelShortDateFormat.format(t_stamp));
            }
            // it must go in the first column then
            else {
              sheet.getRow(rowIdx).getCell(COL1).setCellValue(minRes);
              sheet.getRow(rowIdx).getCell(1).setCellValue(excelShortDateFormat.format(t_stamp));
            }
          }
        }
      }
    }
  }

  static void Ogden_EPA_Summary(
      ObjectDatasetWrapper operationalData, XSSFWorkbook wb_in, int month_in, int year_in) {
    XSSFSheet sheet = wb_in.getSheet("EPA Monthly Summary");
    sheet.getRow(4).getCell(5).setCellValue(month_in);
    sheet.getRow(5).getCell(5).setCellValue(year_in);
    Calendar cal = Calendar.getInstance();

    int offsetIndex = 29;
    final int COL2RESET = 10, COL3RESET = 20, COL1 = 5, COL2 = 24, COL3 = 43;
    Integer curDay = -1;

    // POE min residual disinfectant residual criteria
    for (ObjectDatasetWrapper.Row row : operationalData) {
      Date t_stamp = (Date) row.getKeyValue("t_stamp");
      cal.setTime(t_stamp);
      Integer day = cal.get(Calendar.DAY_OF_MONTH);
      int rowIdx = offsetIndex + (day - 1);

      if (!day.equals(curDay)) {
        curDay = day;
      } else {
        // If there is a lower residual store it for that day
        // check if the value goes in the center column
        if (rowIdx > 38 && rowIdx < 49) {
          rowIdx = rowIdx - COL2RESET;
          sheet.getRow(rowIdx).getCell(20).setCellValue(excelShortDateFormat.format(t_stamp));
        }
        // check if it goes in the third column
        else if (rowIdx > 48) {
          rowIdx = rowIdx - COL3RESET;
          sheet.getRow(rowIdx).getCell(39).setCellValue(excelShortDateFormat.format(t_stamp));
        }
        // it must go in the first column then
        else {
          sheet.getRow(rowIdx).getCell(1).setCellValue(excelShortDateFormat.format(t_stamp));
        }
      }
    }
  }

  // Description: Completes the turbidity sheet for Quinn's
  // Inputs: See parameters
  // Returns: Nothing
  // Map parameters: sheet_name, date, t_stamp, 12AM_turb, 4AM_turb, 8AM_turb, 12PM_turb, 4PM_turb,
  // 8PM_turb);
  static void Turbidity_Sheet(
      ObjectDatasetWrapper WQData,
      XSSFWorkbook wb_in,
      int month_in,
      int year_in,
      Map<String, String> parameters) {

    XSSFSheet sheet = wb_in.getSheet(parameters.get("sheet_name"));
    sheet.getRow(3).getCell(2).setCellValue(month_in);
    sheet.getRow(4).getCell(2).setCellValue(year_in);
    Double blank = sheet.getRow(12).getCell(2).getNumericCellValue();
    Calendar cal = Calendar.getInstance();
    int prevDay = 0;

    for (ObjectDatasetWrapper.Row row : WQData) {
      Date t_stamp = (Date) row.getKeyValue(parameters.get("date"));
      Double turb12AM = (Double) row.getKeyValue(parameters.get("12AM_turb"));
      Double turb4AM = (Double) row.getKeyValue(parameters.get("4AM_turb"));
      Double turb8AM = (Double) row.getKeyValue(parameters.get("8AM_turb"));
      Double turb12PM = (Double) row.getKeyValue(parameters.get("12PM_turb"));
      Double turb4PM = (Double) row.getKeyValue(parameters.get("4PM_turb"));
      Double turb8PM = (Double) row.getKeyValue(parameters.get("8PM_turb"));
      cal.setTime(t_stamp);
      int day = cal.get(Calendar.DAY_OF_MONTH);
      if (day - prevDay > 1) {
        cal.add(Calendar.DAY_OF_MONTH, -1);
        Date t_stamp2 = cal.getTime();

        int rowIdx = 15 + (day - 2);
        sheet.getRow(rowIdx).getCell(1).setCellValue(excelShortDateFormat.format(t_stamp2));
        sheet.getRow(rowIdx).getCell(2).setCellValue("PO");
        sheet.getRow(rowIdx).getCell(3).setCellValue("PO");
        sheet.getRow(rowIdx).getCell(4).setCellValue("PO");
        sheet.getRow(rowIdx).getCell(5).setCellValue("PO");
        sheet.getRow(rowIdx).getCell(6).setCellValue("PO");
        sheet.getRow(rowIdx).getCell(7).setCellValue("PO");
      }

      cal.set(Calendar.DAY_OF_MONTH, day + 1);
      int rowIdx = 15 + (day - 1);

      sheet.getRow(rowIdx).getCell(1).setCellValue(excelShortDateFormat.format(t_stamp));
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
  }

  // Description: Completes the turbidity sheet for Creekside
  // Inputs: See parameters
  // Returns: Nothing
  // Map parameters: sheet_name, date, t_stamp, 12AM_turb, 4AM_turb, 8AM_turb, 12PM_turb, 4PM_turb,
  // 8PM_turb);
  static void Turbidity_SheetCreekside(
      ObjectDatasetWrapper WQData,
      XSSFWorkbook wb_in,
      int month_in,
      int year_in,
      Map<String, String> parameters,
      ObjectDatasetWrapper Hours,
      Date reportDate) {

    XSSFSheet sheet = wb_in.getSheet(parameters.get("sheet_name"));
    sheet.getRow(2).getCell(2).setCellValue(reportDate);
    sheet.getRow(3).getCell(2).setCellValue(year_in);
    Calendar cal = Calendar.getInstance();
    int prevDay = 0;

    for (ObjectDatasetWrapper.Row row : WQData) {
      Date t_stamp = (Date) row.getKeyValue(parameters.get("date"));
      Double turb12AM = (Double) row.getKeyValue(parameters.get("12AM_turb"));
      Double turb4AM = (Double) row.getKeyValue(parameters.get("4AM_turb"));
      Double turb8AM = (Double) row.getKeyValue(parameters.get("8AM_turb"));
      Double turb12PM = (Double) row.getKeyValue(parameters.get("12PM_turb"));
      Double turb4PM = (Double) row.getKeyValue(parameters.get("4PM_turb"));
      Double turb8PM = (Double) row.getKeyValue(parameters.get("8PM_turb"));
      cal.setTime(t_stamp);
      int day = cal.get(Calendar.DAY_OF_MONTH);
      if (day - prevDay > 1) {
        cal.add(Calendar.DAY_OF_MONTH, -1);
        Date t_stamp2 = cal.getTime();

        int rowIdx = 14 + (day - 2);
        sheet.getRow(rowIdx).getCell(0).setCellValue(excelShortDateFormat.format(t_stamp2));
        sheet.getRow(rowIdx).getCell(2).setCellValue("PO");
        sheet.getRow(rowIdx).getCell(3).setCellValue("PO");
        sheet.getRow(rowIdx).getCell(4).setCellValue("PO");
        sheet.getRow(rowIdx).getCell(5).setCellValue("PO");
        sheet.getRow(rowIdx).getCell(6).setCellValue("PO");
        sheet.getRow(rowIdx).getCell(7).setCellValue("PO");
      }

      cal.set(Calendar.DAY_OF_MONTH, day + 1);
      int rowIdx = 14 + (day - 1);

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

    for (ObjectDatasetWrapper.Row row : Hours) {
      Date t_stamp = (Date) row.getKeyValue("t_stamp");
      Double hour = (Double) row.getKeyValue("PRKM_ToSystem_Hours", 0.0);
      cal.setTime(t_stamp);
      int day = cal.get(Calendar.DAY_OF_MONTH);
      int rowIdx = 14 + (day - 1);
      sheet.getRow(rowIdx).getCell(1).setCellValue(hour);
    }
  }

  // Description: Completes the Operational sheet for Creekside
  // Inputs: See parameters
  // Returns: Nothing
  // Map parameters: cl2_column, flow1, flow2, water_temp, clearwell_level, plant_running, date
  static void Creekside_Operational_Sheet(
      ObjectDatasetWrapper FiveMinData, XSSFWorkbook wb_in, Map<String, String> op_params) {

    XSSFSheet sheet = wb_in.getSheet("Operational Worksheet");
    int rowIdx = 7;
    int rowOffset = 0;
    Integer curHour = -1;
    Integer curDay = -1;
    double maxFlow = 0.00;
    Calendar cal = Calendar.getInstance();

    for (ObjectDatasetWrapper.Row row : FiveMinData) {
      // Load Values from the tables
      Date t_stamp = (Date) row.getKeyValue(op_params.get("date"));
      Double cl2Res = (Double) row.getKeyValue(op_params.get("cl2_column"), -1.0);
      Double pmFlow = (Double) row.getKeyValue(op_params.get("flow1"));
      Double dvFlow = (Double) row.getKeyValue(op_params.get("flow2"));
      Double waterTemp = (Double) row.getKeyValue(op_params.get("water_temp"), -1.0);
      double waterTempF = waterTemp * 1.8 + 32;
      Double pmPH = (Double) row.getKeyValue(op_params.get("PRKM_pH"), 0.0);

      if (pmFlow != null && dvFlow != null) {
        // Offsets for storing in the correct area
        cal.setTime(t_stamp);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int hour = cal.get(Calendar.HOUR_OF_DAY);

        // If its a new day populate each row for that day on an hourly basis
        if (!curHour.equals(hour) || !curDay.equals(day)) {
          // reset values and drop the index to the next line
          rowOffset = rowIdx + ((day - 1) * 24) + hour;
          maxFlow = 0.00;
          curHour = hour;
          curDay = day;
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
        if (pmFlow > 100 && maxFlow < pmFlow) {
          sheet.getRow(rowOffset).getCell(3).setCellValue(pmFlow);
          sheet.getRow(rowOffset).getCell(4).setCellValue(dvFlow);
          sheet.getRow(rowOffset).getCell(6).setCellValue(pmPH);
          sheet.getRow(rowOffset).getCell(7).setCellValue(waterTempF);
          sheet.getRow(rowOffset).getCell(8).setCellValue(cl2Res);
          maxFlow = pmFlow;
        }
      }
    }
  }

  // Description: Completes the Operational sheet for Quinn's
  // Inputs: See parameters
  // Returns: Nothing
  // Map parameters: cl2_column, flow1, flow2, water_temp, clearwell_level, plant_running, date
  static void Quinn_Operational_Sheet_MN02(
      ObjectDatasetWrapper FiveMinData, XSSFWorkbook wb_in, Map<String, String> op_params) {

    XSSFSheet sheet = wb_in.getSheet("Operational Worksheet");
    int rowIdx = 7;
    Integer curHour = -1;
    double maxFlow = 0.00;
    Calendar cal = Calendar.getInstance();

    for (ObjectDatasetWrapper.Row row : FiveMinData) {
      // Load Values from the tables
      Date t_stamp = (Date) row.getKeyValue(op_params.get("date"));
      Double cl2Res = (Double) row.getKeyValue(op_params.get("cl2_column"), -1.0);
      Double fairwayFlow = (Double) row.getKeyValue(op_params.get("flow1"));
      Double boothillFlow = (Double) row.getKeyValue(op_params.get("flow2"));
      Double waterTemp = (Double) row.getKeyValue(op_params.get("water_temp"), -1.0);
      Double clearwellLevel = (Double) row.getKeyValue(op_params.get("clearwell_level"), -1.0);
      Integer plantRunning = (Integer) row.getKeyValue(op_params.get("plant_running"), -1);
      Integer numTrainsOnline = (Integer) row.getKeyValue(op_params.get("num_trains_online"));

      if (fairwayFlow != null && boothillFlow != null) {
        // Offsets for storing in the correct area
        cal.setTime(t_stamp);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        Integer hour = cal.get(Calendar.HOUR_OF_DAY);

        // If it a new day populate each row for that day on an hourly basis
        if (!curHour.equals(hour)) {
          // reset values and drop the index to the next line
          rowIdx++;
          maxFlow = 0.00;
          curHour = hour;
          // Create a Calendar object to set the new hour
          Calendar calendar = Calendar.getInstance();
          calendar.setTime(t_stamp);
          calendar.set(Calendar.MINUTE, 0);
          calendar.set(Calendar.SECOND, 0);
          sheet
              .getRow(rowIdx)
              .getCell(1)
              .setCellValue(operationalDateFormat.format(calendar.getTime()));
        }
        // If Fairway and Boothills total flow is greater than 100 and the flow value is higher than
        // our current for the day, store it.
        if (((fairwayFlow + boothillFlow) > 100 && maxFlow < (fairwayFlow + boothillFlow))) {
          maxFlow = fairwayFlow + boothillFlow;
          sheet.getRow(rowIdx).getCell(3).setCellValue(maxFlow);
          sheet.getRow(rowIdx).getCell(4).setCellValue(waterTemp);
          sheet.getRow(rowIdx).getCell(5).setCellValue(clearwellLevel);
          sheet.getRow(rowIdx).getCell(6).setCellValue(cl2Res);
          sheet
              .getRow(rowIdx)
              .getCell(7)
              .setCellValue(Objects.requireNonNullElse(numTrainsOnline, 0));
          sheet.getRow(rowIdx).getCell(2).setCellValue(day);
        }
      }
    }
  }

  // Description: Completes the Operational sheet for Quinn's
  // Inputs: See parameters
  // Returns: Nothing
  // Map parameters: cl2_column, flow1, flow2, water_temp, clearwell_level, plant_running, date
  static void Quinn_Operational_Sheet(
      ObjectDatasetWrapper FiveMinData, XSSFWorkbook wb_in, Map<String, String> op_params) {

    XSSFSheet sheet = wb_in.getSheet("Operational Worksheet");
    int rowIdx = 7;
    Integer curHour = -1;
    double maxFlow = 0.00;
    Calendar cal = Calendar.getInstance();

    for (ObjectDatasetWrapper.Row row : FiveMinData) {
      // Load Values from the tables
      Date t_stamp = (Date) row.getKeyValue(op_params.get("date"));
      Double cl2Res = (Double) row.getKeyValue(op_params.get("cl2_column"), -1.0);
      Double fairwayFlow = (Double) row.getKeyValue(op_params.get("flow1"));
      Double boothillFlow = (Double) row.getKeyValue(op_params.get("flow2"));
      Double waterTemp = (Double) row.getKeyValue(op_params.get("water_temp"), 0.0);
      Double clearwellLevel = (Double) row.getKeyValue(op_params.get("clearwell_level"), 0.0);
      Integer plantRunning = (Integer) row.getKeyValue(op_params.get("plant_running"), 0);

      if (fairwayFlow != null && boothillFlow != null) {
        // Offsets for storing in the correct area
        cal.setTime(t_stamp);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        Integer hour = cal.get(Calendar.HOUR_OF_DAY);

        // If its a new day populate each row for that day on an hourly basis
        if (!curHour.equals(hour)) {
          // reset values and drop the index to the next line
          rowIdx++;
          maxFlow = 0.00;
          curHour = hour;
          // Create a Calendar object to set the new hour
          Calendar calendar = Calendar.getInstance();
          calendar.setTime(t_stamp);
          calendar.set(Calendar.MINUTE, 0);
          calendar.set(Calendar.SECOND, 0);
          sheet
              .getRow(rowIdx)
              .getCell(1)
              .setCellValue(operationalDateFormat.format(calendar.getTime()));
        }
        // If Fairway and Boothills total flow is greater than 100 and the flow value is higher than
        // our current for the day, store it.
        if (((fairwayFlow + boothillFlow) > 100 && maxFlow < (fairwayFlow + boothillFlow))) {
          maxFlow = fairwayFlow + boothillFlow;
          sheet.getRow(rowIdx).getCell(3).setCellValue(maxFlow);
          sheet.getRow(rowIdx).getCell(4).setCellValue(waterTemp);
          sheet.getRow(rowIdx).getCell(5).setCellValue(clearwellLevel);
          sheet.getRow(rowIdx).getCell(6).setCellValue(cl2Res);
          sheet.getRow(rowIdx).getCell(2).setCellValue(day);
        }
      }
    }
  }

  // Description: Completes the Disinfection sheet for Quinn's
  // Inputs: See parameters
  // Returns: Nothing
  private static void Quinn_Disinfection(
      ObjectDatasetWrapper FiveMinData, XSSFWorkbook wb, int month, int year) {
    XSSFSheet sheet = wb.getSheet("Disinfection Report");
    sheet.getRow(2).getCell(2).setCellValue(month);
    sheet.getRow(3).getCell(2).setCellValue(year);
    Calendar cal = Calendar.getInstance();

    double Min_Cl2 = 10.0;
    final int MINCL2OFFSET = 13;
    int MinCl2Idx;
    Integer day2 = -1;

    // Iterate through all rows to determine the lowest CL2 amount while the well was running
    for (ObjectDatasetWrapper.Row row : FiveMinData) {

      Date t_stamp = (Date) row.getKeyValue("t_stamp");
      Double CL2 = (Double) row.getKeyValue("AI0533_EffluentCL2", 0.0);
      Double fairwayFlow = (Double) row.getKeyValue("FI0510_Fairway", 0.0);
      Double boothillFlow = (Double) row.getKeyValue("FI0520_Boothill", 0.0);
      if (fairwayFlow != null && boothillFlow != null) {
        cal.setTime(t_stamp);
        Integer day = cal.get(Calendar.DAY_OF_MONTH);
        MinCl2Idx = MINCL2OFFSET + (day - 1);

        if (!day.equals(day2)) {
          Min_Cl2 = 10.0;
          day2 = day;
        } else {

          if ((fairwayFlow + boothillFlow) > 20 && Min_Cl2 > CL2) {
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
  }

  // Description: Completes the Disinfection sheet for Quinn's
  // Inputs: See parameters
  // Returns: Nothing
  private static void Creekside_Disinfection(ObjectDatasetWrapper FiveMinData, XSSFWorkbook wb) {
    XSSFSheet sheet = wb.getSheet("Disinfection Report");
    Calendar cal = Calendar.getInstance();

    double Min_Cl2 = 10.0;
    final int MINCL2OFFSET = 13;
    int MinCl2Idx;
    Integer day2 = -1;

    // Iterate through all rows to determine the lowest CL2 amount while the well was running
    for (ObjectDatasetWrapper.Row row : FiveMinData) {

      Date t_stamp = (Date) row.getKeyValue("t_stamp");
      Double CL2 = (Double) row.getKeyValue("PRKM_CL2_Res", 0.0);
      Double pmFlow = (Double) row.getKeyValue("PRKM_Flow", 0.0);
      Double dvFlow = (Double) row.getKeyValue("DIVD_Flow", 0.0);
      if (pmFlow != null && dvFlow != null) {
        cal.setTime(t_stamp);
        Integer day = cal.get(Calendar.DAY_OF_MONTH);
        MinCl2Idx = MINCL2OFFSET + (day - 1);

        if (!day.equals(day2)) {
          Min_Cl2 = 10.0;
          day2 = day;
        } else {

          if ((pmFlow + dvFlow) > 20 && Min_Cl2 > CL2) {
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
  }

  // Description: Sets the date on the Sequence sheets for Quinn's
  // Inputs: See parameters
  // Returns: Nothing
  static void Quinn_Sequence(Calendar cal, int year, XSSFWorkbook wb) {
    XSSFSheet sheet = wb.getSheet("Sequence 1");
    sheet.getRow(1).getCell(2).setCellValue(monthFormat.format(cal.getTime()));
    sheet.getRow(2).getCell(2).setCellValue(year);

    sheet = wb.getSheet("Sequence 2");
    sheet.getRow(1).getCell(2).setCellValue(monthFormat.format(cal.getTime()));
    sheet.getRow(2).getCell(2).setCellValue(year);
  }

  static void Quinn_Sequence_MNO2(Calendar cal, int year, XSSFWorkbook wb) {
    XSSFSheet sheet = wb.getSheet("Sequence 1");
    sheet.getRow(1).getCell(2).setCellValue(monthFormat.format(cal.getTime()));
    sheet.getRow(2).getCell(2).setCellValue(year);

    sheet = wb.getSheet("Sequence 2");
    sheet.getRow(1).getCell(2).setCellValue(monthFormat.format(cal.getTime()));
    sheet.getRow(2).getCell(2).setCellValue(year);

    sheet = wb.getSheet("Sequence 3");
    sheet.getRow(1).getCell(2).setCellValue(monthFormat.format(cal.getTime()));
    sheet.getRow(2).getCell(2).setCellValue(year);

    sheet = wb.getSheet("Sequence 4");
    sheet.getRow(1).getCell(2).setCellValue(monthFormat.format(cal.getTime()));
    sheet.getRow(2).getCell(2).setCellValue(year);
  }

  static void Ogden_Operational_Sheet(
      ObjectDatasetWrapper operationalData, XSSFWorkbook wb) {
    XSSFSheet sheet = wb.getSheet("Operational Worksheet");
    int rowIdx = 8;
    int prevHour = -1;
    Calendar cal = Calendar.getInstance();

    for (ObjectDatasetWrapper.Row row : operationalData) {
      // Load Values from the tables
      Date t_stamp = (Date) row.getKeyValue("t_stamp");
      Double cl2Res = (Double) row.getKeyValue("Chlorine_Res", -1.0);
      Double op_flow = (Double) row.getKeyValue("Op_Flow", -1.0);
      Double waterTemp = (Double) row.getKeyValue("Op_Temp", -1.0);

      // Offsets for storing in the correct area
      cal.setTime(t_stamp);
      int day = cal.get(Calendar.DAY_OF_MONTH);
      int hour = cal.get(Calendar.HOUR_OF_DAY);

      // Create a Calendar object to set the new hour
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(t_stamp);
      // If it logs earlier than on the hour add 1 to correct it
      if (prevHour == hour) {
        calendar.add(Calendar.HOUR, 1);
      } else {
        prevHour = hour;

        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        sheet
            .getRow(rowIdx)
            .getCell(1)
            .setCellValue(operationalDateFormat.format(calendar.getTime()));
        sheet.getRow(rowIdx).getCell(3).setCellValue(op_flow);
        sheet.getRow(rowIdx).getCell(4).setCellValue(waterTemp);
        sheet.getRow(rowIdx).getCell(5).setCellValue(0.0);
        sheet.getRow(rowIdx).getCell(6).setCellValue(cl2Res);
        sheet.getRow(rowIdx).getCell(2).setCellValue(day);

        rowIdx++;
      }
    }
  }

  static void Ogden_Sequence_1(
      Calendar cal, ObjectDatasetWrapper WQData, XSSFWorkbook wb, int year) {
    XSSFSheet sheet = wb.getSheet("Sequence 1");
    sheet.getRow(1).getCell(2).setCellValue(monthFormat.format(cal.getTime()));
    sheet.getRow(2).getCell(2).setCellValue(year);
    final int PH_COLUMN = 4;
    int rowIdx = 16;
    for (ObjectDatasetWrapper.Row row : WQData) {
      sheet
          .getRow(rowIdx)
          .getCell(PH_COLUMN)
          .setCellValue((Double) row.getKeyValue("Finished_Water_pH_Ave", -1.0));
      rowIdx++;
    }
  }

  static void Ogden_DI(
      ObjectDatasetWrapper rackResults,
      int month,
      int year,
      XSSFWorkbook wb_in,
      String rack_number_in) {
    XSSFSheet sheet = wb_in.getSheet("Unit(" + rack_number_in + ") DI Testing");
    sheet.getRow(4).getCell(2).setCellValue(month);
    sheet.getRow(5).getCell(2).setCellValue(year);
    Calendar cal = Calendar.getInstance();
    int rack_number = Integer.parseInt(rack_number_in);

    for (ObjectDatasetWrapper.Row row : rackResults) {
      // Grab values needed and get the index
      Date time = (Date) row.getKeyValue("t_stamp");
      String pass_fail = (String) row.getKeyValue("Pass_Fail");
      cal.setTime(time);
      int day = cal.get(Calendar.DAY_OF_MONTH);
      int rowIdx = 14 + day - 1;
      // Set the date column
      sheet.getRow(rowIdx).getCell(1).setCellValue(excelShortDateFormat.format(time));
      if (pass_fail != null && pass_fail.equals("PASS")) {
        sheet.getRow(rowIdx).getCell(2).setCellValue("Y");
      } else {
        sheet.getRow(rowIdx).getCell(2).setCellValue("N");
      }
    }
  }

  static void Ogden_Disinfection(
      Calendar cal_in, ObjectDatasetWrapper operationalData, XSSFWorkbook wb, int month, int year) {
    XSSFSheet sheet = wb.getSheet("Disinfection Report");
    int day = 0, rowIdx;
    sheet.getRow(2).getCell(2).setCellValue(monthFormat.format(cal_in.getTime()));
    sheet.getRow(3).getCell(2).setCellValue(year);
    Calendar cal = Calendar.getInstance();
    for (ObjectDatasetWrapper.Row row : operationalData) {
      Date time = (Date) row.getKeyValue("t_stamp");
      cal.setTime(time);
      int cur_day = cal.get(Calendar.DAY_OF_MONTH);
      if (cur_day != day) {
        rowIdx = 14 + day - 1;
        if (rowIdx < 28) {
          sheet.getRow(rowIdx).getCell(1).setCellValue(excelShortDateFormat.format(time));
        } else {
          rowIdx = rowIdx - 15;
          sheet.getRow(rowIdx).getCell(5).setCellValue(excelShortDateFormat.format(time));
        }
        day = cur_day;
      }
    }
  }

}
