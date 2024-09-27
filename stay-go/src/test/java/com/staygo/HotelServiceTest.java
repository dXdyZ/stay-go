package com.staygo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.staygo.enity.Address;
import com.staygo.enity.hotel.Hotel;
import com.staygo.enity.user.Users;
import com.staygo.repository.hotel_repo.HotelRepository;
import com.staygo.repository.user_repo.UserRepository;
import com.staygo.service.AddressService;
import com.staygo.service.hotel_ser.HotelService;
import com.staygo.service.user_ser.UserService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@code @TestInstance(TestInstance.Lifecycle.PER_CLASS)} - Эта аннотация указывает JUnit создавать один экземпляр тестового класса для всех тестовых методов.
 * Это позволяет использовать нестатические методы с аннотациями @BeforeAll и @AfterAll.
 * <p>
 * {@code @SpringBootTest} - обеспечит загрузку контекста Spring Boot для ваших тестов, что позволит вам использовать внедрение зависимостей и другие возможности Spring.
 * </p>
 */
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class HotelServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HotelService hotelService;

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private AddressService addressService;

    private ObjectMapper objectMapper = new ObjectMapper();

    private Principal principal;

    @BeforeAll
    void init() {
        principal = () -> "testUser";
    }

    @BeforeEach
    void setUp() {
        // Очищаем базы данных перед каждым тестом
        hotelRepository.deleteAll();
        addressService.deleteAll();
        userRepository.deleteAll();

        // Создаем и сохраняем пользователя
        Users user = Users.builder()
                .username("testUser")
                .email("test@example.com")
                .password("password")
                .build();
        userRepository.save(user);
    }

    @AfterAll
    void tearDown() {
        // Очищаем базы данных после всех тестов
        hotelRepository.deleteAll();
        addressService.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @Transactional
    public void testCreatedHotelSuccess() throws Exception {
        // Настройка входных данных
        Hotel hotel = Hotel.builder()
                .name("Test Hotel")
                .address(Address.builder()
                        .city("testCity")
                        .country("testCountry")
                        .zipCode("123123")
                        .street("testStreet")
                        .build())
                .grade(4)
                .build();
        String hotelJson = objectMapper.writeValueAsString(hotel);

        // Создаем MultipartFile
        MockMultipartFile hotelFile = new MockMultipartFile("file", "file1.txt",
                "text/plain", "Test data".getBytes());
        List<MultipartFile> hotelDataFiles = List.of(hotelFile);

        // Тестируемый метод
        ResponseEntity<?> response = hotelService.createdHotel(hotelJson, hotelDataFiles, principal);

        // Проверка результата
        assertEquals(200, response.getStatusCodeValue());

        // Дополнительные проверки
        Optional<Hotel> hotels = hotelRepository.findByName("Test Hotel");
        assertFalse(hotels.isEmpty());
        Hotel savedHotel = hotels.get();
        assertNotNull(savedHotel.getUsers());
        assertEquals("testUser", savedHotel.getUsers().getUsername());
    }

    @Test
    @Transactional
    public void testCreatedHotelAddressAlreadyExists() throws Exception {
        // Создаем и сохраняем адрес
        Address existingAddress = Address.builder()
                .city("testCity")
                .country("testCountry")
                .zipCode("123123")
                .street("testStreet")
                .build();
        addressService.save(existingAddress);

        // Создаем и сохраняем отель с этим адресом
        Users user = userRepository.findByUsername("testUser").get();
        Hotel existingHotel = Hotel.builder()
                .name("Existing Hotel")
                .address(existingAddress)
                .grade(5)
                .users(user)
                .build();
        hotelRepository.save(existingHotel);

        // Настройка входных данных для нового отеля с тем же адресом
        Hotel hotel = Hotel.builder()
                .name("Test Hotel")
                .address(existingAddress)
                .grade(4)
                .build();
        String hotelJson = objectMapper.writeValueAsString(hotel);

        // Создаем MultipartFile
        MockMultipartFile hotelFile = new MockMultipartFile("file", "file1.txt",
                "text/plain", "Test data".getBytes());
        List<MultipartFile> hotelDataFiles = List.of(hotelFile);

        // Тестируемый метод
        ResponseEntity<?> response = hotelService.createdHotel(hotelJson, hotelDataFiles, principal);

        // Проверка результата
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Отель по этому адресу уже существет", response.getBody());
    }

    @Test
    public void testCreatedHotelJsonProcessingException() {
        // Неправильный JSON
        String invalidHotelJson = "{invalidJson}";

        // Создаем MultipartFile
        MockMultipartFile hotelFile = new MockMultipartFile("file", "file1.txt",
                "text/plain", "Test data".getBytes());
        List<MultipartFile> hotelDataFiles = List.of(hotelFile);

        // Тестируемый метод
        ResponseEntity<?> response = hotelService.createdHotel(invalidHotelJson, hotelDataFiles, principal);

        // Проверка результата
        assertEquals(500, response.getStatusCodeValue());
        assertEquals("Неправильно введенные данные", response.getBody());
    }

    @Test
    @Transactional
    public void testCreatedHotelExceptionWhileAddingFiles() throws Exception {
        // Настройка входных данных
        Hotel hotel = Hotel.builder()
                .name("Test Hotel")
                .address(Address.builder()
                        .city("testCity")
                        .country("testCountry")
                        .zipCode("123123")
                        .street("testStreet")
                        .build())
                .grade(4)
                .build();
        String hotelJson = objectMapper.writeValueAsString(hotel);

        // Создаем MultipartFile, который выбросит исключение при вызове getBytes()
        MultipartFile hotelFile = new MultipartFile() {
            @Override
            public String getName() {
                return "file1.txt";
            }

            @Override
            public String getOriginalFilename() {
                return "file1.txt";
            }

            @Override
            public String getContentType() {
                return "text/plain";
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public long getSize() {
                return 0;
            }

            @Override
            public byte[] getBytes() throws IOException {
                throw new IOException("File processing error");
            }

            @Override
            public InputStream getInputStream() throws IOException {
                throw new IOException("File processing error");
            }

            @Override
            public void transferTo(File dest) throws IOException, IllegalStateException {
                throw new IOException("File processing error");
            }
        };
        List<MultipartFile> hotelDataFiles = List.of(hotelFile);

        // Тестируемый метод
        ResponseEntity<?> response = hotelService.createdHotel(hotelJson, hotelDataFiles, principal);

        // Проверка результата
        assertEquals(500, response.getStatusCodeValue());
        assertEquals("Ошибка при добавление фотоFile processing error", response.getBody());
    }
}

