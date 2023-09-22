package com.example.backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TodoIntegrationTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    TodoRepository todoRepository;

    @Test
    @DirtiesContext
    void getAllProductsAndExpectAnEmptyList() throws Exception {
        //GIVEN
        todoRepository.save(new Todo("1", "tidy up", TodoStatus.OPEN));
        //WHEN
        mockMvc.perform(MockMvcRequestBuilders.get("/api/todo"))

                //THEN
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        [
                        {
                        "id": "1",
                        "description": "tidy up",
                        "status": "OPEN"
                        }
                        ]
                        """));
    }

    @Test
    @DirtiesContext
    void addTodoAndExpectStatusIsOk() throws Exception {
        //GIVEN

        //WHEN
        mockMvc.perform(MockMvcRequestBuilders.post("/api/todo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "description":"tidy up",
                                "status": "DONE"
                                }
                                """)
                )

                //THEN
                .andExpect(status().isCreated())
                .andExpect(content().json("""
                        {
                        "description": "tidy up",
                        "status": "DONE"
                        }
                        """))
                .andExpect(jsonPath("$.id").isNotEmpty());
    }

    @Test
    @DirtiesContext
    void findTodoById() throws Exception {
        //GIVEN
        String body = mockMvc.perform(MockMvcRequestBuilders.post("/api/todo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "description": "say hello",
                                "status": "OPEN"
                                }
                                """)
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Todo response = objectMapper.readValue(body, Todo.class);

        //WHEN
        mockMvc.perform(MockMvcRequestBuilders.get("/api/todo/" + response.id()))

                //THEN
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                         "description": "say hello",
                         "status": "OPEN"
                        }
                        """))
                .andExpect(jsonPath("$.id").isNotEmpty());
    }

    @Test
    @DirtiesContext
    void updateTodoById() throws Exception {
        //GIVEN
        todoRepository.save(new Todo("1", "tidy up", TodoStatus.OPEN));

        String body = mockMvc.perform(MockMvcRequestBuilders.put("/api/todo/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "description": "say hello",
                                "status": "OPEN"
                                }
                                """)
                )
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Todo response = objectMapper.readValue(body, Todo.class);

        //WHEN
        mockMvc.perform(MockMvcRequestBuilders.get("/api/todo/" + response.id()))

                //THEN
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                         "description": "say hello",
                         "status": "OPEN"
                        }
                        """))
                .andExpect(jsonPath("$.id").isNotEmpty());
    }

    @Test
    @DirtiesContext
    void deleteTodoById() throws Exception {
        //GIVEN
        todoRepository.save(new Todo("1", "tidy up", TodoStatus.OPEN));
        //WHEN
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/todo/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "description": "say hello",
                                "status": "OPEN"
                                }
                                """)
                )
                //THEN
                .andExpect(status().isNoContent());

    }

    @Test
    @DirtiesContext
    void findTodoByIdAndExpectTodoNotFound() throws Exception {
        //GIVEN
        String id = "123";

        //WHEN
        mockMvc.perform(MockMvcRequestBuilders.get("/api/todo/" + id))

                //THEN
                .andExpect(status().isNotFound())
                .andExpect(content().json("""
                        {
                         "message": "Todo with id: 123 not found"
                        }
                        """));

    }
}
