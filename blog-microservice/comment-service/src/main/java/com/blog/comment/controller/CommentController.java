package com.blog.comment.controller;

import com.blog.comment.dto.CommentEvent;
import com.blog.comment.entity.Comment;
import com.blog.comment.repository.CommentRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    @PostMapping
    public String createComment(@RequestBody Comment comment){
        // 1. Lưu vào Database MySQL
        commentRepository.save(comment);

        // 2. Tạo thông điệp (Event)
        CommentEvent event = new CommentEvent();
        event.setMessage("Có bình luận mới!");
        event.setPostId(comment.getPostId());
        event.setCommentContent(comment.getContent());

        // 3. Gửi thông điệp sang RabbitMQ (Bất đồng bộ)
        rabbitTemplate.convertAndSend(exchange, routingKey, event);

        return "Đã lưu bình luận và gửi thông báo!";
    }
}