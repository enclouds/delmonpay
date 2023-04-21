package com.enclouds.delmonpay.user.service;

import com.enclouds.delmonpay.user.dto.UserDto;
import com.enclouds.delmonpay.user.mapper.UserMapper;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 사용자 정보 Service
 * Security에 사용 하는 UserDetailService Interface
 * @author Enclouds
 * @since 2020.12.11
 */

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 사용자 정보 조회(로그인 정보)
     *
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDto params = new UserDto();
        params.setUserId(username);
        UserDto userInfo = (UserDto) userMapper.selectUserInfo(params);

        List<GrantedAuthority> authorities = new ArrayList<>();

        if(username.equals("enclouds")){
            authorities.add(new SimpleGrantedAuthority("ROLE_SUPER_ADMIN"));
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            authorities.add(new SimpleGrantedAuthority("ROLE_AGENT"));
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        }else if(username.equals("delion")){
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            authorities.add(new SimpleGrantedAuthority("ROLE_AGENT"));
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        }else {
            if(userInfo.getUserGbn().equals("agent")){
                authorities.add(new SimpleGrantedAuthority("ROLE_AGENT"));
            }else if(userInfo.getUserGbn().equals("store")){
                authorities.add(new SimpleGrantedAuthority("ROLE_STORE"));
            }

            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        }

        return new User(userInfo.getUserId(), userInfo.getPassword(), authorities);
    }

    /**
     * 사용자 정보 조회
     *
     * @param username
     * @return
     * @throws Exception
     */
    public UserDto getUserInfo(String username) throws Exception{
        UserDto params = new UserDto();
        params.setUserId(username);
        UserDto userInfo = (UserDto) userMapper.selectUserInfo(params);

        return userInfo;
    }

    /**
     * 사용자 정보 중복체크
     *
     * @param username
     * @return
     * @throws Exception
     */
    public UserDto getUserInfoDupl(String username) throws Exception{
        UserDto params = new UserDto();
        params.setUserId(username);
        UserDto userInfo = (UserDto) userMapper.selectUserInfoDupl(params);

        return userInfo;
    }

}
