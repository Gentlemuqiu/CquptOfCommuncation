package com.example.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;

import java.util.Date;

@Data
@TableName("movie")
public class Movie extends Model<Movie> {
    /**
      * 主键
      */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
      * 名称
      */
    private String name;

    /**
      * 导演
      */
    private String director;


    /**
      * 上映日期
      */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date date;

    /**
      * 图片
      */
    private String img;
    private String description;
    private String url;
    private String area;
    private Integer zan;
    private Integer cai;

}
