package com.enclouds.delmonpay.user.mapper;

import com.enclouds.delmonpay.user.dto.UserDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * 사용자 Mapper
 * @author Enclouds
 * @since 2022.09.11
 */

@Mapper
public interface UserMapper {

    /**
     * 사용자 정보 조회
     *
     * @param params
     * @return
     * @throws UsernameNotFoundException
     */
    UserDto selectUserInfo(UserDto params) throws UsernameNotFoundException;

    /**
     * 사용자 정보 중복체크
     *
     * @param params
     * @return
     * @throws UsernameNotFoundException
     */
    UserDto selectUserInfoDupl(UserDto params) throws UsernameNotFoundException;

    /**
     * 사용자 정보 조회
     * agentCode로 검색
     *
     * @param params
     * @return
     * @throws UsernameNotFoundException
     */
    UserDto selectUserInfoAsAgentCode(UserDto params) throws UsernameNotFoundException;

}
