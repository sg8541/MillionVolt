//package kr.co.milionvolt.ifive.service;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
//@Component
//public class CsvRunner implements CommandLineRunner {
//
//    private final CsvImportService csvImportService;
//
//    public CsvRunner(CsvImportService csvImportService) {
//        this.csvImportService = csvImportService;
//    }
//
//    @Override
//    public void run(String... args) throws Exception {
//        String resourcePath = "data/charging_stations.csv"; // CSV 파일 경로
//        csvImportService.importCsvToDb(resourcePath);
//    }
//}
