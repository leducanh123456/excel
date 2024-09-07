package org.example.collection;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.dto.UserExcelDTO;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserExcelCollection extends ExcelCollection<UserExcelDTO> {
}
