package com.alphen.reader.service.impl;

import com.alphen.reader.entity.Book;
import com.alphen.reader.entity.Evaluation;
import com.alphen.reader.entity.Member;
import com.alphen.reader.exception.BussinessException;
import com.alphen.reader.mapper.BookMapper;
import com.alphen.reader.mapper.EvaluationMapper;
import com.alphen.reader.mapper.MemberMapper;
import com.alphen.reader.service.EvaluationService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class EvaluationServiceImpl implements EvaluationService {
    @Resource
    private EvaluationMapper evaluationMapper;
    @Resource
    private MemberMapper memberMapper;
    @Resource
    private BookMapper bookMapper;
    /*
     * 按图书编号查询有效短评
     * @param bookId 图书编号
     * @return 短评对象集合
     * */
    @Override
    public List<Evaluation> selectByBookID(Long bookId) {
        QueryWrapper<Evaluation> queryWrapper = new QueryWrapper<Evaluation>();
        queryWrapper.eq("book_id",bookId);
        queryWrapper.eq("state","enable");
        queryWrapper.orderByDesc("create_time");
        List<Evaluation> evaluationList = evaluationMapper.selectList(queryWrapper);
        for(Evaluation evaluation:evaluationList){
            Member member = memberMapper.selectById(evaluation.getMemberId());
            evaluation.setMember(member);
            Book book = bookMapper.selectById(evaluation.getBookId());
            evaluation.setBook(book);
        }
        return evaluationList;
    }

    /*
     * 后台管理-展示所有短评
     * */
    @Override
    public List<Evaluation> showAllEva() {
        QueryWrapper<Evaluation> queryWrapper = new QueryWrapper<Evaluation>();
        List<Evaluation> evaluationList = evaluationMapper.selectList(queryWrapper);
        return evaluationList;
    }

    /*
     * 后台管理-修改书评
     * @param evaluation 短评对象
     * @param Evaluation 短评对象
     * */
    @Override
    @Transactional
    public Evaluation update(Evaluation evaluation) {

        return null;
    }

    /*
     * 分页查询评论
     * @param page 页号
     * @param rows 每页记录数
     * @return 分页对象
     * */
    @Override
    public IPage<Evaluation> paging(Integer page, Integer rows) {
        QueryWrapper<Evaluation> queryWrapper = new QueryWrapper<Evaluation>();
        Page<Evaluation> evaluationPage = new Page<Evaluation>(page,rows);
        IPage<Evaluation> pageObject = evaluationMapper.selectPage(evaluationPage,queryWrapper);
        List<Evaluation> evaluationList = pageObject.getRecords();
        for(int i = 0;i<evaluationList.size();i++){
            Evaluation evaluation = evaluationList.get(i);
            //获取Book对象
            Book book = bookMapper.selectById(evaluation.getBookId());
            //获取Member对象
            Member member = memberMapper.selectById((evaluation.getMemberId()));
            //赋值
            evaluation.setBook(book);
            evaluation.setMember(member);
        }
        return pageObject;
    }

    /*
     * 更改当前评论的状态
     * @param evaluationId 评论编号
     * @param state 评论状态
     * */
    @Override
    public void changeState(Long evaluationId,String reason) {
        Evaluation evaluation = evaluationMapper.selectById(evaluationId);
        evaluation.setDisableReason(reason);
        evaluation.setDisableTime(new Date());
        evaluation.setState("disable");
        evaluationMapper.updateById(evaluation);
    }
}
