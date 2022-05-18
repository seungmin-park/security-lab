package tutorial.securitylab.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tutorial.securitylab.domain.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
