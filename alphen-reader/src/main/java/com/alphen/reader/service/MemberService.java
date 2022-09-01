package com.alphen.reader.service;

import com.alphen.reader.entity.Evaluation;
import com.alphen.reader.entity.Member;
import com.alphen.reader.entity.MemberReadState;


public interface MemberService {
    /*
    * 会员注册，创建新会员
    * @param username 用户名
    * @param password 用户密码
    * @param nickname 用户昵称
    * @return Member 会员用户对象
    * */
    public Member createMember(String username,String password,String nickname);

    /*
    * 检验登录
    * @param username 用户名
    * @param password 用户密码
    * @return Member 会员对象
    * */
    public Member checkLogin(String username,String password);

    /*
    * 获取会员用户的阅读状态
    * @param memberId 会员编号
    * @param bookId 图书编号
    * @return MemberReadState 阅读状态实体对象
    * */
    public MemberReadState selectMemberReadState(Long memberId,Long bookId);

    /*
    * 更改当前会员用户的阅读状态
    * @param memberId 会员编号
    * @param bookId 图书编号
    * @param readState 阅读状态
    * @return MemberReadState 阅读状态对象
    * */
    public MemberReadState updateMemberReadState(Long memberId,Long bookId,Integer readState);

    /*
    * 写入短评
    * @param memberId 会员编号
    * @param bookId 图书编号
    * @param content 评论内容
    * @param score 分数
    * @return Evaluation 评论对象
    * */
    public Evaluation evaluate(Long memberId,Long bookId,String content,Integer score);

    /*
    * 点赞功能
    * @param evaluationId 评论编号
    * @return Evaluation 评论对象
    * */
    public Evaluation enjoy(Long evaluationId);

    /*
    * 根据会员编号查询
    * @param memberId
    * @return member 用户对象
    * */
    public Member selectMember(Long memberId);
}
