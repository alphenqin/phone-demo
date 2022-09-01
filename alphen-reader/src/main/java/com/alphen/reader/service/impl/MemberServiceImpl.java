package com.alphen.reader.service.impl;

import com.alphen.reader.entity.Evaluation;
import com.alphen.reader.entity.Member;
import com.alphen.reader.entity.MemberReadState;
import com.alphen.reader.exception.BussinessException;
import com.alphen.reader.mapper.EvaluationMapper;
import com.alphen.reader.mapper.MemberMapper;
import com.alphen.reader.mapper.MemberReadStateMapper;
import com.alphen.reader.service.MemberService;
import com.alphen.reader.utils.MD5Utils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static com.alphen.reader.utils.MD5Utils.md5Digest;

@Service("memberService")
@Transactional
public class MemberServiceImpl implements MemberService {
    @Resource
    private MemberMapper memberMapper;
    @Resource
    private MemberReadStateMapper memberReadStateMapper;
    @Resource
    private EvaluationMapper evaluationMapper;
    /*
     * 会员注册，创建新会员
     * @param username 用户名
     * @param password 用户密码
     * @param nickname 用户昵称
     * @return Member 会员用户对象
     * */
    @Override
    public Member createMember(String username, String password, String nickname) {
        QueryWrapper<Member> queryWrapper = new QueryWrapper<Member>();
        queryWrapper.eq("username",username);
        List<Member> memberList = memberMapper.selectList(queryWrapper);
//        判断用户是否已经存在
        if(memberList.size()>0){
            throw new BussinessException("M01","用户名已存在");
        }
        Member member = new Member();
        int salt = new Random().nextInt(1000)+1000;
        String md5 = MD5Utils.md5Digest(password,salt);
        member.setUsername(username);
        member.setPassword(md5);
        member.setSalt(salt);
        member.setCreateTime(new Date());
        member.setNickname(nickname);
        memberMapper.insert(member);
        return member;
    }

    /*
     * 检验登录
     * @param username 用户名
     * @param password 用户密码
     * @return Member 会员对象
     * */
    @Override
    public Member checkLogin(String username, String password) {
        QueryWrapper<Member> queryWrapper = new QueryWrapper<Member>();
        queryWrapper.eq("username",username);
        Member member = memberMapper.selectOne(queryWrapper);
        if(member == null){
            throw new BussinessException("M02","用户不存在");
        }
        String md5 = MD5Utils.md5Digest(password,member.getSalt());
        if(!md5.equals(member.getPassword())){
            throw new BussinessException("M03","密码错误");
        }
        return member;
    }

    /*
     * 获取会员用户的阅读状态
     * @param memberId 会员编号
     * @param bookId 图书编号
     * @return MemberReadState 阅读状态实体对象
     * */
    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED,readOnly = true)
    public MemberReadState selectMemberReadState(Long memberId, Long bookId) {
        QueryWrapper<MemberReadState> queryWrapper = new QueryWrapper<MemberReadState>();
        queryWrapper.eq("member_id",memberId);
        queryWrapper.eq("book_id",bookId);
        MemberReadState memberReadState = memberReadStateMapper.selectOne(queryWrapper);
        return memberReadState;
    }

    /*
     * 更改当前会员用户的阅读状态
     * @param memberId 会员编号
     * @param bookId 图书编号
     * @param readState 阅读状态
     * @return MemberReadState 阅读状态对象
     * */
    @Override
    public MemberReadState updateMemberReadState(Long memberId, Long bookId, Integer readState) {
        QueryWrapper<MemberReadState> queryWrapper = new QueryWrapper<MemberReadState>();
        queryWrapper.eq("member_id",memberId);
        queryWrapper.eq("book_id",bookId);
        MemberReadState memberReadState = memberReadStateMapper.selectOne(queryWrapper);
//        有则改之，无则加勉
        if (memberReadState == null){
            memberReadState = new MemberReadState();
            memberReadState.setMemberId(memberId);
            memberReadState.setBookId(bookId);
            memberReadState.setReadState(readState);
            memberReadState.setCreateTime(new Date());
            memberReadStateMapper.insert(memberReadState);
        }else {
            memberReadState.setReadState(readState);
            memberReadStateMapper.updateById(memberReadState);
        }
        return memberReadState;
    }

    /*
     * 写入短评
     * @param memberId 会员编号
     * @param bookId 图书编号
     * @param content 评论内容
     * @param score 分数
     * @return Evaluation 评论对象
     * */
    @Override
    public Evaluation evaluate(Long memberId, Long bookId, String content, Integer score) {
        Evaluation evaluation = new Evaluation();
        evaluation.setMemberId(memberId);
        evaluation.setBookId(bookId);
        evaluation.setContent(content);
        evaluation.setScore(score);
        evaluation.setCreateTime(new Date());
        evaluation.setEnjoy(0);
        evaluation.setState("enable");
        evaluationMapper.insert(evaluation);
        return evaluation;
    }

    /*
     * 点赞功能
     * @param evaluationId 评论编号
     * @return Evaluation 评论对象
     * */
    @Override
    public Evaluation enjoy(Long evaluationId) {
        Evaluation evaluation = evaluationMapper.selectById(evaluationId);
        evaluation.setEnjoy(evaluation.getEnjoy()+1);
        evaluationMapper.updateById(evaluation);
        return evaluation;
    }

    @Override
    public Member selectMember(Long memberId) {
        Member member = memberMapper.selectById(memberId);
        return member;
    }
}
