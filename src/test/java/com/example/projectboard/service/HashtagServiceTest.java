package com.example.projectboard.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.example.projectboard.domain.Hashtag;
import com.example.projectboard.repository.HashtagRepository;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.assertj.core.api.Assertions.*;

@DisplayName("비즈니스 로직 - 해시태그")
@ExtendWith(MockitoExtension.class)
class HashtagServiceTest {

    @InjectMocks
    private HashtagService sut;

    @Mock
    private HashtagRepository hashtagRepository;

    @DisplayName("본문을 파싱하여 해시태그 이름들을 중복 없이 반환")
    @ParameterizedTest(name = "[{index}] \"{0}\" => \"{1}\"")
    @MethodSource
    void given_whenParsing_thenReturnUniqueHashtagNames(String input, Set<String> expected){
        //Given


        //When
        Set<String> actual = sut.parseHashtagNames(input);

        //Then
        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
        then(hashtagRepository).shouldHaveNoInteractions();
    }

    static Stream<Arguments> given_whenParsing_thenReturnUniqueHashtagNames(){
        return Stream.of(
            arguments("#java", Set.of("java")),
            arguments("#java_spring", Set.of("java_spring")),
            arguments("#java #spring", Set.of("java", "spring")),
            arguments("#java#spring", Set.of("java", "spring")),
            arguments("#", Set.of()),
            arguments("#       ", Set.of()),
            arguments("       #", Set.of()),
            arguments(null, Set.of()),
            arguments("", Set.of()),
            arguments(" ", Set.of())
        );
    }

    @DisplayName("해시태그 이름들을 입력하면, 저장된 해시태그 중 이름에 매칭하는 것들을 중복 없이 반환한다.")
    @Test
    void givenHashtagNames_whenFindingHashtags_thenReturnsHashtagSet(){
        //Given
        Set<String> hashtagNames = Set.of("java","spring","boots");
        given(hashtagRepository.findByHashtagNameIn(hashtagNames)).willReturn(List.of(
            Hashtag.of("java"),
            Hashtag.of("spring")
        ));

        //When
        Set<Hashtag> hashtags = sut.findHashtagsByNames(hashtagNames);

        //Then
        assertThat(hashtags).hasSize(2);
        then(hashtagRepository).should().findByHashtagNameIn(hashtagNames);

    }
}