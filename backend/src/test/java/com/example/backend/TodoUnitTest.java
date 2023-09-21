package com.example.backend;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class TodoUnitTest {

    TodoRepository todoRepository = mock(TodoRepository.class);
    UuidService uuidService = mock(UuidService.class);
    TodoService todoService = new TodoService(uuidService, todoRepository);

    @Test
    void getListOfTodosShouldReturnListWithTodos() {
        //GIVEN
        Todo todo1 = new Todo("12", "go running", TodoStatus.DONE);
        Todo todo2 = new Todo("123", "go to gym", TodoStatus.DONE);
        List<Todo> expected = new ArrayList<>(List.of(todo1, todo2));

        //WHEN
        when(todoRepository.findAll()).thenReturn(expected);
        List<Todo> actual = todoService.getTodos();

        //THEN
        verify(todoRepository).findAll();
        assertEquals(expected, actual);
    }

    @Test
    void whenAddTodoWithoutIdThenReturnTodoWithId() {
        //GIVEN
        TodoWithoutId todoWithoutId = new TodoWithoutId("go running", TodoStatus.DONE);
        String id = "1";
        Todo expected = new Todo("1", "go running", TodoStatus.DONE);

        //WHEN
        when(uuidService.getRandomId()).thenReturn(id);
        when(todoRepository.insert(expected)).thenReturn(expected);
        Todo actual = todoService.addTodo(todoWithoutId);

        //THEN
        verify(uuidService).getRandomId();
        verify(todoRepository).insert(expected);
        assertEquals(expected, actual);
    }

    @Test
    void getTodoByIdShouldThrowNoSuchElementException() {
        //GIVEN
        String id = "1";
        //WHEN
        when(todoRepository.findById(id)).thenReturn(Optional.empty());
        //THEN
        assertThrows(NoSuchElementException.class, () -> todoService.getTodoById(id));
    }

    @Test
    void getTodoByIdShouldReturnTodoWithMatchingId() {
        //GIVEN
        String id = "1";
        Todo expected = new Todo("1", "go running", TodoStatus.DONE);
        //WHEN
        when(todoRepository.findById(id)).thenReturn(Optional.of(expected));
        Todo actual = todoService.getTodoById(id);
        //THEN
        verify(todoRepository).findById(id);
        assertEquals(expected, actual);

    }

    @Test
    void updateTodoShouldReturnUpdatedTodo() {
        //GIVEN
        String id = "1";
        TodoWithoutId todoWithoutId = new TodoWithoutId("go running", TodoStatus.DONE);
        Todo expected = new Todo("1", "go running", TodoStatus.DONE);
        //WHEN
        when(todoRepository.save(expected)).thenReturn(expected);
        Todo actual = todoService.updateTodo(id, todoWithoutId);
        //THEN
        verify(todoRepository).save(expected);
        assertEquals(expected, actual);
    }

    @Test
    void deleteTodoKp() {
        //GIVEN
        String id = "12";
        doNothing().when(todoRepository).deleteById(id);
        //WHEN
        todoService.deleteTodo(id);
        //THEN
        verify(todoRepository).deleteById(id);
    }
}
