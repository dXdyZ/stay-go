package com.lfey.statygo.service;

import com.lfey.statygo.dto.CreateRoom;
import com.lfey.statygo.entity.Room;
import com.lfey.statygo.entity.RoomType;
import com.lfey.statygo.exception.DuplicateRoomException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Profile("test")
class RoomServiceTest {

    @Autowired
    private RoomService roomService;

    // Вспомогательные методы для создания объектов
    private CreateRoom createRoom(Integer number) {
        return CreateRoom.builder()
                .number(number)
                .capacity(2)
                .pricePerDay(true)
                .description("Test Room")
                .roomType(RoomType.STANDARD)
                .build();
    }

    private Room room(Integer number) {
        return Room.builder()
                .number(number)
                .capacity(2)
                .pricePerDay(true)
                .description("Test Room")
                .isActive(true)
                .roomType(RoomType.STANDARD)
                .build();
    }

    // Тест 1: Есть дубликаты в roomDto, rooms пуст
    @Test
    void existsRoomByHotel_DuplicateInDto_ThrowsDuplicateException() {
        List<Room> rooms = List.of();
        List<CreateRoom> roomDto = List.of(createRoom(101), createRoom(101));

        DuplicateRoomException exception = assertThrows(
                DuplicateRoomException.class,
                () -> roomService.existsRoomByHotel(rooms, roomDto)
        );
        assertEquals("Duplicates number in added rooms", exception.getMessage());
    }

    // Тест 2: Нет дубликатов, но номер существует в rooms
    @Test
    void existsRoomByHotel_ExistingRoomNumber_ThrowsExistingException() {
        List<Room> rooms = List.of(room(101));
        List<CreateRoom> roomDto = List.of(createRoom(101));

        DuplicateRoomException exception = assertThrows(
                DuplicateRoomException.class,
                () -> roomService.existsRoomByHotel(rooms, roomDto)
        );
        assertEquals("The rooms to be added already exist", exception.getMessage());
    }

    // Тест 3: Есть и дубликаты, и существующие номера (должна превалировать проверка на существующие номера)
    @Test
    void existsRoomByHotel_BothExistingAndDuplicate_ThrowsExistingException() {
        List<Room> rooms = List.of(room(101));
        List<CreateRoom> roomDto = List.of(createRoom(101), createRoom(101));

        DuplicateRoomException exception = assertThrows(
                DuplicateRoomException.class,
                () -> roomService.existsRoomByHotel(rooms, roomDto)
        );
        assertEquals("The rooms to be added already exist", exception.getMessage());
    }

    // Тест 4: Нет дубликатов и существующих номеров
    @Test
    void existsRoomByHotel_NoIssues_NoException() {
        List<Room> rooms = List.of(room(102));
        List<CreateRoom> roomDto = List.of(createRoom(101));

        assertDoesNotThrow(() -> roomService.existsRoomByHotel(rooms, roomDto));
    }

    // Тест 5: Пустой roomDto
    @Test
    void existsRoomByHotel_EmptyDto_NoException() {
        List<Room> rooms = List.of(room(101));
        List<CreateRoom> roomDto = List.of();

        assertDoesNotThrow(() -> roomService.existsRoomByHotel(rooms, roomDto));
    }

    // Тест 6: Пустой rooms, есть дубликаты в roomDto
    @Test
    void existsRoomByHotel_EmptyRoomsWithDuplicate_ThrowsDuplicateException() {
        List<Room> rooms = List.of();
        List<CreateRoom> roomDto = List.of(createRoom(101), createRoom(101));

        DuplicateRoomException exception = assertThrows(
                DuplicateRoomException.class,
                () -> roomService.existsRoomByHotel(rooms, roomDto)
        );
        assertEquals("Duplicates number in added rooms", exception.getMessage());
    }

    // Тест 7: Пустой rooms, нет дубликатов
    @Test
    void existsRoomByHotel_EmptyRoomsNoDuplicate_NoException() {
        List<Room> rooms = List.of();
        List<CreateRoom> roomDto = List.of(createRoom(101));

        assertDoesNotThrow(() -> roomService.existsRoomByHotel(rooms, roomDto));
    }

    // Тест 8: Нет существующих номеров, но есть дубликаты в roomDto
    @Test
    void existsRoomByHotel_NonExistingWithDuplicate_ThrowsDuplicateException() {
        List<Room> rooms = List.of(room(102));
        List<CreateRoom> roomDto = List.of(createRoom(101), createRoom(101));

        DuplicateRoomException exception = assertThrows(
                DuplicateRoomException.class,
                () -> roomService.existsRoomByHotel(rooms, roomDto)
        );
        assertEquals("Duplicates number in added rooms", exception.getMessage());
    }
}