package com.alphen.reader.mapper;

import com.alphen.reader.entity.Book;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

public interface BookMapper extends BaseMapper<Book> {
    /*
    * 更新图书评分/数量
    * */
    public void updateEvaluation();
}
