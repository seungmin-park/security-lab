package tutorial.securitylab.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tutorial.securitylab.domain.Member;
import tutorial.securitylab.repository.MemberRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Optional<Member> findById(Long memberId) {
        return memberRepository.findById(memberId);
    }
}
