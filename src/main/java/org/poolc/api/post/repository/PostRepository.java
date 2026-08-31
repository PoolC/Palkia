package org.poolc.api.post.repository;

import org.poolc.api.member.domain.Member;
import org.poolc.api.post.domain.BoardType;
import org.poolc.api.post.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, PagingAndSortingRepository<Post, Long>, JpaSpecificationExecutor<Post> {
    @Query("select p from Post p where p.member=:member and p.isDeleted=false")
    Page<Post> findByMember(Member member, Pageable pageable);

    @Query("select p from Post p where p.boardType in :boardTypes and p.isDeleted=false")
    Page<Post> findByBoardTypeIn(@Param("boardTypes") Collection<BoardType> boardTypes, Pageable pageable);

    @Query("select p from Post p where (p.title like %:title% or p.body like %:body%) and p.isDeleted = false")
    Page<Post> findByTitleContainingOrBodyContaining(String title, String body, Pageable pageable);

    @Query("select p from Post p where p.boardType in :boardTypes and p.title like %:keyword% and p.isDeleted = false")
    Page<Post> findByBoardTypeInAndTitleContaining(@Param("boardTypes") Collection<BoardType> boardTypes, @Param("keyword") String keyword, Pageable pageable);

}
