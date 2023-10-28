package com.sn.shehan_n.cafe_management_system.commons.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmailDto {
    private String subject;
    private String receiver;
    private String body;
}
