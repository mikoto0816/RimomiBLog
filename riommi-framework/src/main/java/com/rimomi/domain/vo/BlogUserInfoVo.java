package com.rimomi.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlogUserInfoVo {

    private String token;
    private UserInfoVo userInfo;

}
