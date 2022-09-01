package com.alphen.reader.service;

import com.alphen.reader.entity.Book;
import com.alphen.reader.entity.Evaluation;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

public interface EvaluationService {
    /*
    * 按图书编号查询有效短评
    * @param bookId 图书编号
    * @return 短评对象集合
    * */
    public List<Evaluation> selectByBookID(Long bookId);

    /*
    * 后台管理-展示所有短评
    * */
    public List<Evaluation> showAllEva();

    /*
    * 后台管理-修改书评
    * @param evaluation 短评对象
    * @param Evaluation 短评对象
    * */
    public Evaluation update(Evaluation evaluation);

    /*
     * 分页查询评论
     * @param page 页号
     * @param rows 每页记录数
     * @return 分页对象
     * */
    public IPage<Evaluation> paging(Integer page, Integer rows);

    /*
    * 更改当前评论的状态
    * @param evaluationId 评论编号
    * @param reason 禁用该评论的理由
    * */
    public void changeState(Long evaluationId,String reason);
}
