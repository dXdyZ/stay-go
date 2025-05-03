INSERT INTO addresses (id, country, city, street, house_number, postal_code)
VALUES (1, 'Russia', 'Moscow', 'Lenina', '10A', 123456);

INSERT INTO hotels (id, name, stars, grade, description, address_id)
VALUES (1, 'Test Hotel', 4, 8.75, 'Good hotel near the city center', 1);

INSERT INTO rooms (id, number, capacity, price_per_day, description, is_active, bed_type, room_type, hotel_id, auto_approve)
VALUES
(1, 101, 2, 3500.00, 'Standard room with queen bed', true, 'QUEEN_SIZE', 'STANDARD', 1, true),
(2, 102, 4, 5500.00, 'Deluxe room with two double beds', true, 'DOUBLE', 'LUX', 1, true);

INSERT INTO bookings (id, hotel_id, room_id, booking_status, start_date, end_date, total_price, username)
VALUES
(1, 1, 1, 'CONFIRMED', '2025-06-01', '2025-06-05', 14000.00, 'test_user'),
(2, 1, 2, 'PENDING', '2025-06-10', '2025-06-15', 27500.00, 'another_user');

SELECT setval('addresses_id_seq', (SELECT MAX(id) FROM addresses));
SELECT setval('hotels_id_seq', (SELECT MAX(id) FROM hotels));
SELECT setval('rooms_id_seq', (SELECT MAX(id) FROM rooms));
SELECT setval('bookings_id_seq', (SELECT MAX(id) FROM bookings));
