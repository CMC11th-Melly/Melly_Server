package cmc.mellyserver.auth.application;

import cmc.mellyserver.common.exception.MemberNotFoundException;
import cmc.mellyserver.member.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

//
//@Service
//@RequiredArgsConstructor
//public class CustomUserDetailsService implements UserDetailsService {
//
//    private final MemberRepository memberRepository;
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//
//
//
////        if (user == null) {
//  //          throw new UsernameNotFoundException("Can not find username.");
//    //    }
//
//      //  return UserPrincipal.create(user);
//    }
//}