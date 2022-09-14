package cmc.mellyserver.auth.application;

import cmc.mellyserver.auth.client.KakaoClient;
import cmc.mellyserver.auth.presentation.dto.AuthRequest;
import cmc.mellyserver.member.domain.Member;
import cmc.mellyserver.member.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OAuthService {

    private final MemberRepository memberRepository;
    private final KakaoClient kakaoClient;
    @Transactional
    public void login(AuthRequest authRequest) {
        Member kakaoMember = kakaoClient.getUserData(authRequest.getAccessToken());
        String socialId = kakaoMember.getSocialId();
        Optional<Member> memberBySocialId = memberRepository.findMemberBySocialId(socialId);
    }
}
