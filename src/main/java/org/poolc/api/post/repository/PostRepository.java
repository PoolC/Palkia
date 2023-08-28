package org.poolc.api.post.repository;

import org.poolc.api.board.domain.Board;
import org.poolc.api.member.domain.Member;
import org.poolc.api.post.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, PagingAndSortingRepository<Post, Long>, JpaSpecificationExecutor<Post> {
    Page<Post> findByMember(Member member, Pageable pageable);
    Page<Post> findByBoard(Board board, Pageable pageable);
    Page<Post> findByTitleContainingOrBodyContaining(String title, String body, Pageable pageable);

}
