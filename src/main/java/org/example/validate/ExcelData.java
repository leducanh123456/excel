package org.example.validate;

import org.apache.poi.ss.usermodel.Workbook;
import org.example.dto.ExcelDTO;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public abstract class ExcelData<T extends ExcelDTO> {
    protected final Class<T> tClass;

    public ExcelData(Class<T> tClass) {
        this.tClass = tClass;
        //check cấu hình có hợp lệ
    }

    public List<T> getListFromExcel(Workbook workbook) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        return null;
    }
}
