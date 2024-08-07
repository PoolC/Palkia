package org.poolc.api.comment.service;

import org.poolc.api.comment.dto.CommentCreateRequest;
import org.poolc.api.comment.dto.CommentResponse;
import org.poolc.api.comment.dto.CommentUpdateRequest;
import org.poolc.api.post.service.PostService;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.poolc.api.auth.exception.UnauthorizedException;
import org.poolc.api.badge.service.BadgeConditionService;
import org.poolc.api.comment.domain.Comment;
import org.poolc.api.comment.repository.CommentRepository;
import org.poolc.api.comment.vo.CommentCreateValues;
import org.poolc.api.comment.vo.CommentUpdateValues;
import org.poolc.api.member.domain.Member;
import org.poolc.api.notification.service.NotificationService;
import org.poolc.api.post.domain.Post;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    //좋아요 수에 따라 뱃지 자동지급을 위함
    private final BadgeConditionService badgeConditionService;
    private final NotificationService notificationService;
    private final PostService postService;

    @Transactional
    public void createComment(Member member, CommentCreateRequest request) {
        Post post = postService.findById(member, request.getPostId());
        CommentCreateValues values = new CommentCreateValues(post, member, request);

        Comment parent = findParentComment(values.getParentId());
        validateParentComment(parent);

        Comment comment = saveComment(values, parent);
        sendNotifications(values, parent);
    }

    @Transactional(readOnly = true)
    public List<Comment> findCommentsByPost(Post post) {
        return commentRepository.findAllByPost(post).stream()
                .sorted(Comparator.comparing(Comment::getCreatedAt))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Comment> findCommentsByParent(Long parentId) {
        Comment parent = commentRepository.findById(parentId)
                .orElseThrow(() -> new NoSuchElementException("No comment found with given comment id."));
        return commentRepository.findAllByParent(parent);
    }

    @Transactional(readOnly = true)
    public Comment findById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new NoSuchElementException("No comment found with given comment id."));
    }

    @Transactional
    public void deleteComment(Member member, Long commentId) {
        Comment comment = findById(commentId);
        checkWriterOrAdmin(member, comment);
        comment.setIsDeleted();
        comment.getPost().deductCommentCount();
    }

    @Transactional
    public void updateComment(Member member, Long commentId, CommentUpdateRequest request) {
        Comment comment = findById(commentId);
        checkWriter(member, comment);
        comment.updateComment(new CommentUpdateValues(request));
    }

    @Transactional
    public void likeComment(Member member, Long commentId) {
        Comment comment = findById(commentId);
        checkNotWriter(member, comment);
        if (comment.getPost().getIsQuestion()){
            comment.addLikeCount();
            badgeConditionService.like(comment.getMember());
        }
    }

    @Transactional
    public void dislikeComment(Member member, Long commentId) {
        Comment comment = findById(commentId);
        checkNotWriter(member, comment);
        if (comment.getPost().getIsQuestion()){
            comment.deductLikeCount();
            badgeConditionService.dislike(comment.getMember());
        }
    }

    // TODO
    private Comment findParentComment(Long parentId) {
        if (parentId != null && parentId != 0) {
            return findById(parentId);
        }
        return null;
    }

    private void validateParentComment(Comment parent) {
        if (parent != null && parent.getIsChild()) {
            throw new IllegalArgumentException("대댓글에 대댓글을 달 수 없습니다.");
        }
    }

    private Comment saveComment(CommentCreateValues values, Comment parent) {
        Comment comment = new Comment(parent, values);
        commentRepository.save(comment);
        comment.getPost().addCommentCount();
        return comment;
    }

    private void sendNotifications(CommentCreateValues values, Comment parent) {
        String commenterID = values.getMember().getLoginID();
        String postWriterID = values.getPost().getMember().getLoginID();

        if (parent == null && !commenterID.equals(postWriterID)) {
            notificationService.createCommentNotification(commenterID, postWriterID, values.getPost().getId());
        } else if (parent != null && !parent.getMember().equals(values.getMember())) {
            String parentCommenterID = parent.getMember().getLoginID();
            notificationService.createRecommentNotification(commenterID, parentCommenterID, values.getPost().getId(), parent.getId());
        }
    }

    private void checkWriterOrAdmin(Member member, Comment comment) {
        if (!comment.getMember().equals(member) && !member.isAdmin()) throw new UnauthorizedException("접근할 수 없습니다.");
    }

    private void checkWriter(Member member, Comment comment) {
        if (!comment.getMember().equals(member)) throw new UnauthorizedException("접근할 수 없습니다.");
    }

    private void checkNotWriter(Member member, Comment comment) {
        if (comment.getMember().equals(member)) throw new UnauthorizedException("자신의 댓글을 좋아할 수 없습니다.");
    }
}
