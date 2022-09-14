package cmc.mellyserver.member.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Long> {

   Optional<Member> findMemberBySocialId(String socialId);

   Optional<Member> findMemberByEmail(String email);
}
