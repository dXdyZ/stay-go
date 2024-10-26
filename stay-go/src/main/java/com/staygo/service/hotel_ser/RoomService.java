package com.staygo.service.hotel_ser;

import com.staygo.enity.hotel.Hotel;
import com.staygo.enity.hotel.Room;
import com.staygo.enity.hotel.RoomData;
import com.staygo.repository.hotel_repo.RoomRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Service
public class RoomService {
    private final RoomRepository roomRepository;
    private final HotelService hotelService;

    @Autowired
    public RoomService(RoomRepository roomRepository,
                       HotelService hotelService) {
        this.roomRepository = roomRepository;
        this.hotelService = hotelService;
    }

    /**
     * <p>
     * Принимает {@code Principal} для поиска отеля по имени пользователя и добавления данных о комнатах.
     * Выполняется проверка на совпадение имени файла с именем комнаты. Если совпадение найдено,
     * данные комнаты добавляются к объекту {@code Room}.
     * </p>
     *<p>
     * <h3>В случае проблемы заменить метод, {@code addedPhoto}, на кусок кода ниже</h3>
     * <pre>{@code
     * for (MultipartFile file : roomFile) {
     *     String fileName = file.getOriginalFilename();
     *     String newFileName = Objects.requireNonNull(fileName).substring(0, fileName.lastIndexOf('.'));
     *
     *     for (Room roomAddedData : room) {
     *         if (newFileName.equals(roomAddedData.getRoomName())) {
     *             roomAddedData.addRoomData(RoomData.builder()
     *                                       .name(newFileName)
     *                                       .createDate(new Date())
     *                                       .room(roomAddedData)
     *                                       .data(file.getBytes())
     *                                       .build());
     *         }
     *     }
     * }
     * }</pre>
     * </p>
     * @param principal объект {@link Principal}, представляющий текущего пользователя
     * @param street адрес отеля в виде строки
     * @param room список объектов {@link Room}, которые будут обновлены
     * @param roomFile список файлов {@link MultipartFile}, содержащих фото для комнат
     * @return объект {@link ResponseEntity}, указывающий на результат выполнения операции
     */
    @Transactional
    public ResponseEntity<?> addedARoomToTheHotel(Principal principal,
                                                  String street, List<Room> room,
                                                  List<MultipartFile> roomFile) {
        Optional<Hotel> hotel = hotelService.findByUserAndStreet(street, principal.getName());
        Iterable<Room> rooms = roomRepository.findAll();
        Set<Room> roomSet = new HashSet<>();
        AtomicBoolean result = new AtomicBoolean(true);
        room.forEach(n -> {
            if (!roomSet.add(n)) result.set(false);
        });
        roomSet.clear();
        rooms.forEach(n -> {
            if (!roomSet.add(n)) result.set(false);
        });
        if (result.get()) {
            if (hotel.isPresent()) {
                if (roomFile != null && !roomFile.isEmpty()) {
                    try {
                        room.stream().forEach(n -> {
                            System.out.println("room: " + n.toString() + "----" + n.getRoomData());
                        });
                        room = addedPhoto(roomFile, room);
                        room.stream().forEach(n -> {
                            System.out.println("room: " + n.toString() + "----" + n.getRoomData() + "---" + n.getRoomData().get(0).getRoom());
                        });
                    } catch (IOException e) {
                        log.error("failed added photo to rooms: {}", e.getMessage());
                        return new ResponseEntity<>("failed added photo to rooms", HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                }
                for (Room roomAddedData : room) {
                    roomAddedData.setHotel(hotel.get());
                    roomAddedData.setRoomStatus("free");
                    roomRepository.save(roomAddedData);
                }
                return ResponseEntity.ok(room);
            } else {
                return new ResponseEntity<>("Такого отеля нет", HttpStatus.BAD_REQUEST);
            }
        } return new ResponseEntity<>("Иммена комнат не должны повторятся", HttpStatus.BAD_REQUEST);
    }


    public ResponseEntity<?> addedPhotosToRooms(List<MultipartFile> files) {
        Iterable<Room> rooms = roomRepository.findAll();
        List<Room> roomArrayList = new ArrayList<>();
        rooms.forEach(roomArrayList::add);
        List<Room> roomUpdate = null;
        try {
            roomUpdate = addedPhoto(files, roomArrayList);
        } catch (IOException e) {
            return new ResponseEntity<>("failed added photo to rooms", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        roomRepository.saveAll(roomUpdate);
        return ResponseEntity.ok().build();
    }

    private List<Room> addedPhoto(List<MultipartFile> files, List<Room> rooms) throws IOException {
        if (!files.isEmpty()) {
            for (MultipartFile fileNameForIterator : files) {
                for (Room room : rooms) {
                    String oldFileName = fileNameForIterator.getOriginalFilename();
                    String newFileName = Objects.requireNonNull(oldFileName).substring(0, oldFileName.lastIndexOf('.'));
                    if (room.getRoomName().equals(newFileName)) {
                        room.addRoomData(RoomData.builder()
                                .name(newFileName)
                                .createDate(new Date())
                                .data(fileNameForIterator.getBytes())
                                .room(room)
                                .build());
                    }
                }
            }
            rooms.stream().forEach(n -> {
                System.out.println(n.getId() + " : " + n.getRoomData() + " : " + n.getRoomName());
            });
            return rooms;
        } else return rooms;
    }

    @Transactional
    public void forTestDeleteAll() {
        roomRepository.deleteAll();
    }


    @Transactional
    public ResponseEntity<?> modifyRoomName(String oldName, String newName) {
        Optional<Room> room = roomRepository.findByRoomName(oldName);
        if (room.isPresent()) {
            if (newName != null) {
                room.get().setRoomName(newName);
            }
            return ResponseEntity.ok(room);
        } else return new ResponseEntity<>("Комнаты под таким именем нет", HttpStatus.BAD_REQUEST);
    }

    @Transactional
    public List<Long> findNotArmoredRoom(String armoredDate, String departureDate, String city, String hotelName, String prestige) {
        Hotel hotel = hotelService.findByCityAndName(city, hotelName);
        List<Long> sortedByPrestige = new ArrayList<>();
        for (Room room : hotel.getRooms()) {
            if (room.getPrestige().equals(prestige)) {
                sortedByPrestige.add(room.getId());
            }
        }
        return sortedByPrestige;
    }
}
