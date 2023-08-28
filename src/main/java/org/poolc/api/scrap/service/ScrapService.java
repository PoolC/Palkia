package org.poolc.api.scrap.service;

import lombok.RequiredArgsConstructor;
import org.poolc.api.member.domain.Member;
import org.poolc.api.post.domain.Post;
import org.poolc.api.post.dto.PostResponse;
import org.poolc.api.post.service.PostService;
import org.poolc.api.scrap.domain.Scrap;
import org.poolc.api.scrap.repository.ScrapRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScrapService {
    private final ScrapRepository scrapRepository;
    private final PostService postService;

    private static final int size = 15;

    public void scrap(String memberId, Long postId) {
        if (!scrapRepository.existsByMemberIdAndPostId(memberId, postId)) {
            scrapRepository.save(new Scrap(memberId, postId));
        }
    }

    public void removeScrap(String memberId, Long postId) {
        Scrap scrap = scrapRepository.findByMemberIdAndPostId(memberId, postId)
                .orElseThrow(() -> new NoSuchElementException("No scrap with given id."));
        scrapRepository.deleteById(scrap.getId());
    }

    public List<PostResponse> viewMyPosts(Member member, int page) {
        PageRequest pr = PageRequest.of(page, size);
        Page<Scrap> scraps = scrapRepository.findAllByMemberId(member.getLoginID(), pr);
        return scraps.stream()
                .map(s -> scrapToPostResponse(member, s))
                .collect(Collectors.toList());
    }

    private PostResponse scrapToPostResponse(Member member, Scrap scrap) {
        Post post = postService.findPostById(member, scrap.getPostId());
        return PostResponse.of(post);
    }
}
