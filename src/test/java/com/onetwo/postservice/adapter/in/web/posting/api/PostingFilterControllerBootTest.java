package com.onetwo.postservice.adapter.in.web.posting.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onetwo.postservice.adapter.in.web.config.TestHeader;
import com.onetwo.postservice.application.port.in.command.PostPostingCommand;
import com.onetwo.postservice.application.port.in.usecase.PostPostingUseCase;
import com.onetwo.postservice.common.GlobalStatus;
import com.onetwo.postservice.common.GlobalUrl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import(TestHeader.class)
class PostingFilterControllerBootTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PostPostingUseCase postPostingUseCase;

    @Autowired
    private TestHeader testHeader;

    private final String userId = "testUserId";
    private final String content = "content";
    private final PageRequest pageRequest = PageRequest.of(0, 20);
    private final String pageNumber = "pageNumber";
    private final String pageSize = "pageSize";

    @Test
    @Transactional
    @DisplayName("[통합][Web Adapter] Posting Filter by user 조회 성공 - 성공 테스트")
    void getFilteredPostingByUserSuccessTest() throws Exception {
        //given
        for (int i = 1; i <= pageRequest.getPageSize(); i++) {
            PostPostingCommand postPostingCommand = new PostPostingCommand(userId, content + i);
            postPostingUseCase.postPosting(postPostingCommand);
        }

        String queryString = UriComponentsBuilder.newInstance()
                .queryParam(pageNumber, pageRequest.getPageNumber())
                .queryParam(pageSize, pageRequest.getPageSize())
                .build()
                .encode()
                .toUriString();
        //when
        ResultActions resultActions = mockMvc.perform(
                get(GlobalUrl.POSTING_FILTER + GlobalUrl.PATH_VARIABLE_USER_ID_WITH_BRACE + queryString, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(testHeader.getRequestHeader())
                        .accept(MediaType.APPLICATION_JSON));
        //then
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("filtering-posting-by-user-id",
                                requestHeaders(
                                        headerWithName(GlobalStatus.ACCESS_ID).description("서버 Access id"),
                                        headerWithName(GlobalStatus.ACCESS_KEY).description("서버 Access key")
                                ),
                                pathParameters(
                                        parameterWithName(GlobalUrl.PATH_VARIABLE_USER_ID).description("조회할 posting의 user id")
                                ),
                                queryParameters(
                                        parameterWithName(pageNumber).description("조회할 posting slice 페이지 번호"),
                                        parameterWithName(pageSize).description("조회할 posting slice size")
                                ),
                                responseFields(
                                        fieldWithPath("content[]").type(JsonFieldType.ARRAY).description("Posting List"),
                                        fieldWithPath("content[].postingId").type(JsonFieldType.NUMBER).description("Posting id"),
                                        fieldWithPath("content[].userId").type(JsonFieldType.STRING).description("Posting 작성자 user id"),
                                        fieldWithPath("content[].postedDate").type(JsonFieldType.STRING).description("Posting 작성 일자"),
                                        fieldWithPath("pageable").type(JsonFieldType.OBJECT).description("pageable object"),
                                        fieldWithPath("pageable.pageNumber").type(JsonFieldType.NUMBER).description("조회 페이지 번호"),
                                        fieldWithPath("pageable.pageSize").type(JsonFieldType.NUMBER).description("조회 한 size"),
                                        fieldWithPath("pageable.sort").type(JsonFieldType.OBJECT).description("sort object"),
                                        fieldWithPath("pageable.sort.empty").type(JsonFieldType.BOOLEAN).description("sort 요청 여부"),
                                        fieldWithPath("pageable.sort.sorted").type(JsonFieldType.BOOLEAN).description("sort 여부"),
                                        fieldWithPath("pageable.sort.unsorted").type(JsonFieldType.BOOLEAN).description("unsort 여부"),
                                        fieldWithPath("pageable.offset").type(JsonFieldType.NUMBER).description("대상 시작 번호"),
                                        fieldWithPath("pageable.unpaged").type(JsonFieldType.BOOLEAN).description("unpaged"),
                                        fieldWithPath("pageable.paged").type(JsonFieldType.BOOLEAN).description("paged"),
                                        fieldWithPath("size").type(JsonFieldType.NUMBER).description("List 크기"),
                                        fieldWithPath("number").type(JsonFieldType.NUMBER).description("조회 페이지 번호"),
                                        fieldWithPath("sort").type(JsonFieldType.OBJECT).description("sort object"),
                                        fieldWithPath("sort.empty").type(JsonFieldType.BOOLEAN).description("sort 요청 여부"),
                                        fieldWithPath("sort.sorted").type(JsonFieldType.BOOLEAN).description("sort 여부"),
                                        fieldWithPath("sort.unsorted").type(JsonFieldType.BOOLEAN).description("unsort 여부"),
                                        fieldWithPath("numberOfElements").type(JsonFieldType.NUMBER).description("numberOfElements"),
                                        fieldWithPath("first").type(JsonFieldType.BOOLEAN).description("처음인지 여부"),
                                        fieldWithPath("last").type(JsonFieldType.BOOLEAN).description("마지막인지 여부"),
                                        fieldWithPath("empty").type(JsonFieldType.BOOLEAN).description("비어있는지 여부")
                                )
                        )
                );
    }

    @Test
    @Transactional
    @DisplayName("[통합][Web Adapter] Posting Filter by user page number validation - 성공 테스트")
    void getFilteredPostingByUserWithOutPageNumberSuccessTest() throws Exception {
        //given
        for (int i = 1; i <= pageRequest.getPageSize(); i++) {
            PostPostingCommand postPostingCommand = new PostPostingCommand(userId, content + i);
            postPostingUseCase.postPosting(postPostingCommand);
        }

        String queryString = UriComponentsBuilder.newInstance()
                .queryParam(pageSize, pageRequest.getPageSize())
                .build()
                .encode()
                .toUriString();
        //when
        ResultActions resultActions = mockMvc.perform(
                get(GlobalUrl.POSTING_FILTER + GlobalUrl.PATH_VARIABLE_USER_ID_WITH_BRACE + queryString, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(testHeader.getRequestHeader())
                        .accept(MediaType.APPLICATION_JSON));
        //then
        resultActions.andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @Transactional
    @DisplayName("[통합][Web Adapter] Posting Filter by user page size validation - 성공 테스트")
    void getFilteredPostingByUserWithOutPageSizeSuccessTest() throws Exception {
        //given
        for (int i = 1; i <= pageRequest.getPageSize(); i++) {
            PostPostingCommand postPostingCommand = new PostPostingCommand(userId, content + i);
            postPostingUseCase.postPosting(postPostingCommand);
        }

        String queryString = UriComponentsBuilder.newInstance()
                .queryParam(pageNumber, pageRequest.getPageNumber())
                .build()
                .encode()
                .toUriString();
        //when
        ResultActions resultActions = mockMvc.perform(
                get(GlobalUrl.POSTING_FILTER + GlobalUrl.PATH_VARIABLE_USER_ID_WITH_BRACE + queryString, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(testHeader.getRequestHeader())
                        .accept(MediaType.APPLICATION_JSON));
        //then
        resultActions.andExpect(status().isOk())
                .andDo(print());
    }
}