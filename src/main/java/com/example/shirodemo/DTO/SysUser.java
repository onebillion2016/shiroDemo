package com.example.shirodemo.DTO;


import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class SysUser {

    @ExcelProperty(value = "order", index = 0)
    private String order;


    @ExcelProperty(value = "url", index = 1)
    private String url;

    @ExcelProperty(value = "num", index = 2)
    private String num;
}
