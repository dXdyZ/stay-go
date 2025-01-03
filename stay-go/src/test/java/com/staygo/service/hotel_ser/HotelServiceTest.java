package com.staygo.service.hotel_ser;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.staygo.enity.address.Address;
import com.staygo.enity.hotel.Hotel;
import com.staygo.enity.hotel.Room;
import com.staygo.enity.hotel.RoomData;
import com.staygo.enity.user.Role;
import com.staygo.enity.user.Users;
import com.staygo.repository.hotel_repo.HotelRepository;
import com.staygo.repository.user_repo.UserRepository;
import com.staygo.service.AddressService;
import com.staygo.service.DateCheck;
import com.staygo.service.user_ser.UserService;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.*;

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

    public static final Logger log = LoggerFactory.getLogger(HotelServiceTest.class);
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

    @Autowired
    private PasswordEncoder passwordEncoder;

    private ObjectMapper objectMapper = new ObjectMapper();

    private Principal principal;

    @BeforeAll
    void init() {
        principal = () -> "testUser";
    }

    @BeforeEach
    void setUp() {
//        // Очищаем базы данных перед каждым тестом
//        hotelRepository.deleteAll();
//        addressService.deleteAll();
//        userRepository.deleteAll();

        // Создаем и сохраняем пользователя
//        Users user = Users.builder()
//                .username("testUser")
//                .email("test@example.com")
//                .password("password")
//                .build();
//        userRepository.save(user);
    }

//    @AfterAll
//    void tearDown() {
//        // Очищаем базы данных после всех тестов
//        hotelRepository.deleteAll();
//        addressService.deleteAll();
//        userRepository.deleteAll();
//    }

    @Autowired
    private DateCheck dateCheck;


    @Test
    @Rollback(value = false)
    void addedDataInTable() {
        Users users = Users.builder()
                .username("test")
                .createDate(new Date())
                .email("test@email.com")
                .password(passwordEncoder.encode("123123"))
                .phoneNumber("12312312312")
                .role(Role.ROLE_ADMIN)
                .build();
        userRepository.save(users);

        for (int i = 0; i < 100; i++) {
            Hotel hotel = hotel(i, users);
            hotel.setRooms(Collections.singletonList(room(hotel, i)));
            hotelRepository.save(hotel);
        }
    }

    @Test
    @Rollback(false)
    void testValidData() {
        Users users = Users.builder()
                .id(102L)
                .username("test")
                .createDate(new Date())
                .email("test@email.com")
                .password(passwordEncoder.encode("123123"))
                .phoneNumber("12312312312")
                .role(Role.ROLE_ADMIN)
                .build();
        Hotel hotel = Hotel.builder()
                .name("test valid data")
                .grade(4)
                .address(Address.builder()
                        .city("Moscow")
                        .street("test")
                        .country("Russia")
                        .zipCode("23232")
                        .numberHouse("123")
                        .build())
                .users(users)
                .build();
        hotel.setRooms(Collections.singletonList(room(hotel, 1)));
        hotelRepository.save(hotel);
    }

    Room room(Hotel hotel, int i) {
        return Room.builder()
                .hotel(hotel)
                .roomName(String.valueOf(i))
                .price(BigDecimal.valueOf(34543))
                .prestige("president")
                .roomStatus("free")
                .build();
    }

    Hotel hotel(int i, Users users) {
        return Hotel.builder()
                .name("hello" + i)
                .grade(4)
                .address(Address.builder()
                        .city("test" + i)
                        .street("test" + i)
                        .country("test" + i)
                        .zipCode("23232" + i)
                        .numberHouse("123" + i)
                        .build())
                .users(users)
                .build();
    }

    @Test
    @Transactional
    public void testFindAllHotelSuccessAllParameters() {
        log.info("Текущая дата (mapNowDateInString): {}", dateCheck.mapNowDateInString());
        log.info("Дата бронирования: 10.11.2024, Дата выезда: 24.11.2024");
    }

    @Test
    @Transactional
    @Rollback(false)
    public void testFindAllHotelSuccessNoAllParameters() throws Exception {

    }


    @Test
    @Transactional
    @Rollback(true)
    public void testCreatedHotelSuccess() throws Exception {
        // Настройка входных данных
        Hotel hotel = Hotel.builder()
                .name("Test Hotel")
                .address(Address.builder()
                        .city("testCity")
                        .country("testCountry1")
                        .zipCode("332222")
                        .street("testStreet12")
                        .numberHouse("93 q")
                        .build())
                .grade(5)
                .build();
        String hotelJson = objectMapper.writeValueAsString(hotel);

        Hotel hotel2 = Hotel.builder()
                .name("Test Hotel two test created")
                .address(Address.builder()
                        .city("testCity")
                        .country("testCountry")
                        .zipCode("332222")
                        .street("testStreet12 123 b")
                        .numberHouse("23 a")
                        .build())
                .grade(5)
                .build();
        String hotel2Json = objectMapper.writeValueAsString(hotel2);

        Hotel hotel3 = Hotel.builder()
                .name("ooooooqqweqwe")
                .address(Address.builder()
                        .city("testCity")
                        .country("testCountry")
                        .zipCode("qweqweqw123")
                        .street("qweqweqwe12312300")
                        .numberHouse("123qwe1")
                        .build())
                .grade(4)
                .build();
        String hotel3Json = objectMapper.writeValueAsString(hotel3);


        Hotel hotel4 = Hotel.builder()
                .name("yyyyyqweqwe")
                .address(Address.builder()
                        .city("testCity")
                        .country("testCountry")
                        .zipCode("123123231ff")
                        .street("123123231ffqweqweb")
                        .numberHouse("12332a")
                        .build())
                .grade(3)
                .build();
        String hotel4Json = objectMapper.writeValueAsString(hotel4);

        // Создаем MultipartFile
        MockMultipartFile hotelFile = new MockMultipartFile("file", "file1.txt",
                "text/plain", "Test data".getBytes());
        List<MultipartFile> hotelDataFiles = List.of(hotelFile);

        // Тестируемый метод
        ResponseEntity<?> response = hotelService.createdHotel(hotelJson, hotelDataFiles, principal);
        ResponseEntity<?> hotelResponse = hotelService.createdHotel(hotel2Json, hotelDataFiles, principal);
        hotelService.createdHotel(hotel3Json, hotelDataFiles, principal);
        hotelService.createdHotel(hotel4Json, hotelDataFiles, principal);

        // Проверка результата
        assertEquals(200, response.getStatusCodeValue());
        log.info("hotel two response: {}", hotelResponse.toString());
        assertEquals(HttpStatus.OK, hotelResponse.getStatusCode());

        // Дополнительные проверки
        Optional<Hotel> hotels = hotelRepository.findByName("Test Hotel");
        Optional<Hotel> hotel2Find = hotelRepository.findByName("Test Hotel two test created");
        assertFalse(hotels.isEmpty());
        assertFalse(hotel2Find.isEmpty());

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

