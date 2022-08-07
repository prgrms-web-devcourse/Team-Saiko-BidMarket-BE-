package com.saiko.bidmarket.comment.service;

import static com.saiko.bidmarket.product.Category.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.saiko.bidmarket.comment.controller.dto.CommentCreateRequest;
import com.saiko.bidmarket.comment.controller.dto.CommentCreateResponse;
import com.saiko.bidmarket.comment.entity.Comment;
import com.saiko.bidmarket.comment.repository.CommentRepository;
import com.saiko.bidmarket.common.entity.UnsignedLong;
import com.saiko.bidmarket.common.exception.NotFoundException;
import com.saiko.bidmarket.product.entity.Product;
import com.saiko.bidmarket.product.repository.ProductRepository;
import com.saiko.bidmarket.user.entity.Group;
import com.saiko.bidmarket.user.entity.User;
import com.saiko.bidmarket.user.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class DefaultCommentServiceTest {

  @Mock
  ProductRepository productRepository;

  @Mock
  UserRepository userRepository;

  @Mock
  CommentRepository commentRepository;

  @InjectMocks
  DefaultCommentService commentService;

  @Nested
  @DisplayName("create 메서드는")
  class DescribeCreate {

    @Nested
    @DisplayName("유효한 값이 전달되면")
    class ContextWithValidArg {

      @Test
      @DisplayName("댓글을 저장하고 댓글의 아이디를 반환한다")
      void ItResponseProduct() {
        //given
        long userId = 1L;
        long productId = 1L;
        CommentCreateRequest commentCreateRequest = new CommentCreateRequest(productId,
                                                                             "풍경 그림도 돼요?");
        User writer = new User("제로", "image", "google", "1234", new Group());

        Product product = Product.builder()
                                 .title("그림 그려드려요")
                                 .description("잘 그려요")
                                 .location("전주")
                                 .category(HOBBY)
                                 .minimumPrice(1000)
                                 .images(Collections.emptyList())
                                 .writer(writer)
                                 .build();
        Comment comment = Comment.builder()
                                 .writer(writer)
                                 .product(product)
                                 .content(commentCreateRequest.getContent())
                                 .build();
        ReflectionTestUtils.setField(comment, "id", 1);

        given(userRepository.findById(anyLong())).willReturn(Optional.of(writer));
        given(productRepository.findById(anyLong())).willReturn(Optional.of(product));
        given(commentRepository.save(any(Comment.class))).willReturn(comment);

        //when
        CommentCreateResponse response = commentService.create(
            UnsignedLong.valueOf(userId), commentCreateRequest);

        //then
        verify(productRepository).findById(anyLong());
        verify(userRepository).findById(anyLong());
        verify(commentRepository).save(any(Comment.class));
        assertThat(response.getId().getValue()).isEqualTo(comment.getId());
      }
    }

    @Nested
    @DisplayName("Request 값이 null이면")
    class ContextWithNullRequest {

      @Test
      @DisplayName("IllegalArgumentException 에러를 발생시킨다.")
      void ItThrowsIllegalArgumentException() {
        //when, then
        assertThatThrownBy(() -> commentService.create(UnsignedLong.valueOf(1), null))
            .isInstanceOf(IllegalArgumentException.class);
      }
    }

    @Nested
    @DisplayName("UnsignedLong 값이 null이면")
    class ContextWithNullUnsignedLong {

      @Test
      @DisplayName("IllegalArgumentException 에러를 발생시킨다.")
      void ItThrowsIllegalArgumentException() {
        //given
        CommentCreateRequest commentCreateRequest = new CommentCreateRequest(1L, "택배 선불 되나요?");

        //when, then
        assertThatThrownBy(() -> commentService.create(null, commentCreateRequest))
            .isInstanceOf(IllegalArgumentException.class);
      }
    }

    @Nested
    @DisplayName("유저를 찾을 수 없다면")
    class ContextWithNonexistentUser {

      @Test
      @DisplayName("NotFound 에러를 발생시킨다.")
      void ItThrowsNotFoundException() {
        //given
        CommentCreateRequest commentCreateRequest = new CommentCreateRequest(1L, "택배 선불 되나요?");
        given(userRepository.findById(anyLong()))
            .willThrow(NotFoundException.class);

        //when, then
        assertThatThrownBy(
            () -> commentService.create(UnsignedLong.valueOf(1), commentCreateRequest))
            .isInstanceOf(NotFoundException.class);
      }
    }

    @Nested
    @DisplayName("상품을 찾을 수 없다면")
    class ContextWithNonexistentProduct {

      @Test
      @DisplayName("NotFound 에러를 발생시킨다.")
      void ItThrowsNotFoundException() {
        //given
        User writer = new User("제로", "image", "google", "1234", new Group());
        CommentCreateRequest commentCreateRequest = new CommentCreateRequest(1L, "택배 선불 되나요?");
        given(userRepository.findById(anyLong()))
            .willReturn(Optional.of(writer));
        given(productRepository.findById(anyLong()))
            .willThrow(NotFoundException.class);

        //when, then
        assertThatThrownBy(
            () -> commentService.create(UnsignedLong.valueOf(1), commentCreateRequest))
            .isInstanceOf(NotFoundException.class);
      }
    }
  }
}