package com.rimomi.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleDetailVo {
    //id
    private Long id;
    //标题
    private String title;
    //详情
    private String summary;
    //所属分类id
    private Long categoryId;
    //分类名称
    private String categoryName;
    //略缩图
    private String thumbnail;
    //查看量
    private Long viewCount;
    //创建时间
    private Date createTime;
    //文章内容
    private String content;
}
