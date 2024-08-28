package org.example.component;

import org.example.dto.UserDTO;
import org.example.validate.ExcelData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Validator;

@Component
public class UserExcelData extends ExcelData<UserDTO> {
    private static final Class<UserDTO> userDTOClass = UserDTO.class;

    @Autowired
    public UserExcelData(Validator validator) {
        super(userDTOClass, validator);
    }
}
