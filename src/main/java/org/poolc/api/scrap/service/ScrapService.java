package org.poolc.api.scrap.service;

import lombok.RequiredArgsConstructor;
import org.poolc.api.common.exception.ConflictException;
import org.poolc.api.member.domain.Member;
import org.poolc.api.post.domain.Post;
import org.poolc.api.post.dto.GetBoardResponse;
import org.poolc.api.post.dto.GetPostsResponse;
import org.poolc.api.post.service.PostService;
import org.poolc.api.scrap.domain.Scrap;
import org.poolc.api.scrap.repository.ScrapRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScrapService {
    private final ScrapRepository scrapRepository;
    private final PostService postService;

    private static final int size = 10;

    public void scrap(String memberId, Long postId) {
        //check scrap exist
        if (!scrapRepository.existsByMemberIdAndPostId(memberId, postId)) {
            scrapRepository.save(new Scrap(memberId, postId));
        }
        else{
            //already scrapped
            throw new ConflictException("Already Scrapped");
        }
    }

    public void removeScrap(String memberId, Long postId) {
        Scrap scrap = scrapRepository.findByMemberIdAndPostId(memberId, postId)
                .orElseThrow(() -> new NoSuchElementException("No scrap with given id."));
        scrapRepository.deleteById(scrap.getId());
    }

    public GetBoardResponse viewMyPosts(Member member, int page) {
        PageRequest pr = PageRequest.of(page, size);
        Page<Scrap> scraps = scrapRepository.findAllByMemberId(member.getLoginID(), pr);
        return new GetBoardResponse(
                scraps.getTotalPages(),
                scraps.stream()
                        .map(s -> scrapToPostResponse(member, s))
                        .collect(Collectors.toList())
        );
    }

    private GetPostsResponse scrapToPostResponse(Member member, Scrap scrap) {
        Post post = postService.findPostById(member, scrap.getPostId());
        return GetPostsResponse.of(post);
    }
}
