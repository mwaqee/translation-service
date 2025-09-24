package org.mwtech.translation.api;

import org.mwtech.translation.service.ExportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class ExportController {
  private final ExportService exportService;
  public ExportController(ExportService exportService){ this.exportService = exportService; }

  @GetMapping("/export")
  public ResponseEntity<Map<String,String>> export(
      @RequestParam String locale,
      @RequestParam String platform,
      @RequestParam(required=false) String namespace,
      @RequestHeader(value="If-None-Match", required=false) String inm
  ){
    return exportService.export(locale, platform, namespace, inm, null);
  }
}
