package org.example;

import org.dhatim.fastexcel.Workbook;
import org.dhatim.fastexcel.Worksheet;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;

@SpringBootApplication
public class Main {
    //    public static void main(String[] args) {
//        SpringApplication.run(Main.class, args);
//    }
    public static void main(String[] args) throws IOException {
        // Đường dẫn thư mục và tên file
//    String folderPath = "F:\\test"; // Thay đổi theo đường dẫn của bạn
//    String filePath = Paths.get(folderPath, "sxssf_example.xlsx").toString();
//
//    // Sử dụng SXSSFWorkbook với kích thước bộ nhớ đệm (rows to keep in memory)
//    SXSSFWorkbook workbook = new SXSSFWorkbook(100); // Giữ 100 hàng trong bộ nhớ
//    long start = System.currentTimeMillis();
//    Sheet sheet = workbook.createSheet("Sheet1");
//
//    for (int row = 0; row < 50000; row++) { // Ghi 50,000 hàng
//        Row excelRow = sheet.createRow(row);
//        for (int col = 0; col < 30; col++) { // Ghi 30 cột
//            Cell cell = excelRow.createCell(col);
//            cell.setCellValue("Data " + row + "," + col);
//        }
//    }
//    System.out.println(System.currentTimeMillis()- start);
//    try (FileOutputStream fos = new FileOutputStream(filePath)) {
//        workbook.write(fos);
//    }
//    workbook.dispose(); // Giải phóng tài nguyên bộ nhớ


        //
//    String folderPath = "F:\\test"; // Thay đổi theo đường dẫn của bạn
//    String filePath = Paths.get(folderPath, "xssf_example.xlsx").toString();
//
//    // Tạo XSSFWorkbook
//    Workbook workbook = new XSSFWorkbook();
//    long start = System.currentTimeMillis();
//    Sheet sheet = workbook.createSheet("Sheet1");
//
//    for (int row = 0; row < 50000; row++) { // Ghi 50,000 hàng
//        Row excelRow = sheet.createRow(row);
//        for (int col = 0; col < 30; col++) { // Ghi 30 cột
//            Cell cell = excelRow.createCell(col);
//            cell.setCellValue("Data " + row + "," + col);
//        }
//    }
//    System.out.println(System.currentTimeMillis()- start);
//    try (FileOutputStream fos = new FileOutputStream(filePath)) {
//        workbook.write(fos);
//    }
//    workbook.close(); // Đóng workbook để giải phóng tài nguyên

        // Đường dẫn thư mục và tên file
        String folderPath = "F:\\test"; // Thay đổi theo đường dẫn của bạn
        String filePath = Paths.get(folderPath, "fast_excel.xlsx").toString();

        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            // Khởi tạo Workbook với các tham số đúng
            long start = System.currentTimeMillis();
            Workbook workbook = new Workbook(fos, "My Application", "1.0");
            Worksheet sheet = workbook.newWorksheet("Sheet1");
            // Ghi dữ liệu vào file Excel
            for (int row = 0; row < 1000000; row++) { // Ghi 50,000 hàng
                for (int col = 0; col < 30; col++) { // Ghi 30 cột
                    sheet.value(row, col, "Data " + row + "," + col);
                }
            }
            System.out.println(System.currentTimeMillis()- start);
            // Hoàn tất ghi dữ liệu và đóng workbook
            workbook.finish();
        }
    }
}