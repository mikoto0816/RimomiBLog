package com.rimomi.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleListVo {
    //id
    private Long id;
    //标题
    private String title;
    //详情
    private String summary;
//    表名
    private String categoryName;
    //略缩图
    private String thumbnail;
    //查看量
    private Long viewCount;
    //创建时间
    private Date createTime;
}
