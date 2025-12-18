package com.tecnocampus.LS2.protube_back.domain.service;

import com.tecnocampus.LS2.protube_back.adapter.in.web.dto.CommentDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TemporaryCommentServiceTest {

    private TemporaryCommentService commentService;

    @BeforeEach
    void setUp() {
        commentService = new TemporaryCommentService();
    }

    @Test
    void addComment_ShouldCreateAndStoreComment() {
        String videoId = "video1";
        String username = "user1";
        String text = "Nice video!";

        CommentDTO comment = commentService.addComment(videoId, username, text);

        assertNotNull(comment);
        assertEquals(username, comment.getAuthor());
        assertEquals(text, comment.getText());
        assertEquals(0L, comment.getLikeCount());
        
        List<CommentDTO> comments = commentService.getComments(videoId);
        assertEquals(1, comments.size());
        assertEquals(comment, comments.get(0));
    }

    @Test
    void getComments_WhenNoComments_ShouldReturnEmptyList() {
        List<CommentDTO> comments = commentService.getComments("video_empty");
        assertNotNull(comments);
        assertTrue(comments.isEmpty());
    }

    @Test
    void addComment_MultipleCommentsSameVideo() {
        String videoId = "video2";
        commentService.addComment(videoId, "u1", "c1");
        commentService.addComment(videoId, "u2", "c2");

        List<CommentDTO> comments = commentService.getComments(videoId);
        assertEquals(2, comments.size());
        assertEquals("c1", comments.get(0).getText());
        assertEquals("c2", comments.get(1).getText());
    }
}
