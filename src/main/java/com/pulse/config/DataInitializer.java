package com.pulse.config;

import com.pulse.model.*;
import com.pulse.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CirculationRepository circulationRepository;

    @Autowired
    private FineRepository fineRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Override
    public void run(String... args) {
        // Add sample rooms first since events will reference them
        Room room1 = new Room();
        room1.setName("Main Reading Hall");
        room1.setDescription("A spacious hall for quiet reading and book-related events");
        room1.setCapacity(30);
        room1.setLocation("Ground Floor");
        room1.setFacilities("Tables, Chairs, Projector, Sound System");
        room1.setStatus(RoomStatus.AVAILABLE);
        roomRepository.save(room1);

        Room room2 = new Room();
        room2.setName("Computer Lab");
        room2.setDescription("Equipped computer laboratory for digital learning and workshops");
        room2.setCapacity(20);
        room2.setLocation("First Floor");
        room2.setFacilities("Computers, Internet, Projector, Whiteboard");
        room2.setStatus(RoomStatus.AVAILABLE);
        roomRepository.save(room2);

        Room room3 = new Room();
        room3.setName("Children's Reading Corner");
        room3.setDescription("Cozy corner designed for children's reading activities");
        room3.setCapacity(15);
        room3.setLocation("Ground Floor");
        room3.setFacilities("Child-sized Furniture, Story Time Area, Educational Toys");
        room3.setStatus(RoomStatus.AVAILABLE);
        roomRepository.save(room3);

        // Add sample books
        Book book1 = new Book();
        book1.setIsbn("978-602-8519-93-9");
        book1.setTitle("Laskar Pelangi");
        book1.setAuthor("Andrea Hirata");
        book1.setPublisher("Bentang Pustaka");
        book1.setCategory(BookCategory.FICTION);
        book1.setPublicationYear(2005);
        book1.setCopies(3);
        book1.setAvailableCopies(3);
        book1.setLocation(BookLocation.SHELF_A); // Fiction shelf
        book1.setStatus("AVAILABLE");
        bookRepository.save(book1);

        Book book2 = new Book();
        book2.setIsbn("978-983-46-0836-7");
        book2.setTitle("Mereka Yang Terpinggir");
        book2.setAuthor("Osman Ayob");
        book2.setPublisher("Dewan Bahasa dan Pustaka");
        book2.setCategory(BookCategory.NON_FICTION);
        book2.setPublicationYear(2018);
        book2.setCopies(2);
        book2.setAvailableCopies(2);
        book2.setLocation(BookLocation.SHELF_B); // Non-Fiction shelf
        book2.setStatus("AVAILABLE");
        bookRepository.save(book2);

        Book book3 = new Book();
        book3.setIsbn("978-967-411-456-8");
        book3.setTitle("Sejarah Melayu");
        book3.setAuthor("Tun Sri Lanang");
        book3.setPublisher("PTS Publishing");
        book3.setCategory(BookCategory.HISTORY);
        book3.setPublicationYear(2015);
        book3.setCopies(1);
        book3.setAvailableCopies(1);
        book3.setLocation(BookLocation.SHELF_C); // Reference shelf
        book3.setStatus("AVAILABLE");
        bookRepository.save(book3);

        // Add sample members
        Member member1 = new Member();
        member1.setMemberId("M001");
        member1.setName("Ahmad bin Ismail");
        member1.setEmail("ahmad.ismail@gmail.com");
        member1.setPhoneNumber("011-1234567");
        member1.setAddress("123, Jalan Ampang, 50450 Kuala Lumpur");
        member1.setMembershipType("STANDARD");
        member1.setStatus("ACTIVE");
        member1.setJoinDate(LocalDateTime.now());
        member1.setExpiryDate(LocalDateTime.now().plusYears(1));
        memberRepository.save(member1);

        Member member2 = new Member();
        member2.setMemberId("M002");
        member2.setName("Siti Nurhaliza binti Hassan");
        member2.setEmail("siti.nh@gmail.com");
        member2.setPhoneNumber("012-9876543");
        member2.setAddress("456, Jalan Bukit Bintang, 55100 Kuala Lumpur");
        member2.setMembershipType("PREMIUM");
        member2.setStatus("ACTIVE");
        member2.setJoinDate(LocalDateTime.now());
        member2.setExpiryDate(LocalDateTime.now().plusYears(1));
        memberRepository.save(member2);

        Member member3 = new Member();
        member3.setMemberId("M003");
        member3.setName("Muhammad Hafiz bin Abdullah");
        member3.setEmail("hafiz.abdullah@gmail.com");
        member3.setPhoneNumber("013-5557777");
        member3.setAddress("789, Jalan Putra, 50350 Kuala Lumpur");
        member3.setMembershipType("STANDARD");
        member3.setStatus("ACTIVE");
        member3.setJoinDate(LocalDateTime.now());
        member3.setExpiryDate(LocalDateTime.now().plusYears(1));
        memberRepository.save(member3);

        // Add sample circulation records
        
        // 1. Currently borrowed book (not overdue)
        Circulation circulation1 = new Circulation();
        circulation1.setBook(book1);
        circulation1.setMember(member1);
        circulation1.setBorrowDate(LocalDateTime.now().minusDays(1));
        circulation1.setDueDate(LocalDateTime.now().plusDays(13)); // Due in 13 days
        circulation1.setStatus(CirculationStatus.BORROWED);
        circulationRepository.save(circulation1);
        
        // Update book status
        book1.setAvailableCopies(book1.getAvailableCopies() - 1);
        bookRepository.save(book1);

        // 2. Overdue book
        Circulation circulation2 = new Circulation();
        circulation2.setBook(book2);
        circulation2.setMember(member2);
        circulation2.setBorrowDate(LocalDateTime.now().minusDays(16));
        circulation2.setDueDate(LocalDateTime.now().minusDays(2)); // 2 days overdue
        circulation2.setStatus(CirculationStatus.OVERDUE);
        circulationRepository.save(circulation2);

        // Create fine for overdue book
        Fine fine1 = new Fine();
        fine1.setCirculation(circulation2);
        fine1.setAmount(new BigDecimal("2.00")); // rm1 per day * 2 days
        fine1.setStatus(FineStatus.PENDING);
        fine1.setIssuedDate(LocalDateTime.now());
        fineRepository.save(fine1);
        circulation2.setFine(fine1);
        circulationRepository.save(circulation2);
        
        // Update book status
        book2.setAvailableCopies(book2.getAvailableCopies() - 1);
        bookRepository.save(book2);

        // 3. Returned book with paid fine
        Circulation circulation3 = new Circulation();
        circulation3.setBook(book3);
        circulation3.setMember(member3);
        circulation3.setBorrowDate(LocalDateTime.now().minusDays(20));
        circulation3.setDueDate(LocalDateTime.now().minusDays(6));
        circulation3.setReturnDate(LocalDateTime.now().minusDays(4));
        circulation3.setStatus(CirculationStatus.RETURNED);
        circulationRepository.save(circulation3);

        Fine fine2 = new Fine();
        fine2.setCirculation(circulation3);
        fine2.setAmount(new BigDecimal("2.00")); // rm1 per day * 2 days
        fine2.setStatus(FineStatus.PAID);
        fine2.setIssuedDate(LocalDateTime.now().minusDays(4));
        fine2.setPaidDate(LocalDateTime.now().minusDays(3));
        fineRepository.save(fine2);
        circulation3.setFine(fine2);
        circulationRepository.save(circulation3);

        // Add sample events
        
        // 1. Upcoming Book Reading Session
        Event event1 = new Event();
        event1.setName("Book Reading: Laskar Pelangi");
        event1.setDescription("Join us for an engaging book reading session of the beloved novel 'Laskar Pelangi' by Andrea Hirata.");
        event1.setLocation("Main Reading Hall");
        event1.setStartDateTime(LocalDateTime.now().plusDays(7).withHour(14).withMinute(0)); // Next week, 2 PM
        event1.setEndDateTime(LocalDateTime.now().plusDays(7).withHour(16).withMinute(0)); // 2 hours duration
        event1.setCapacity(30);
        event1.setOrganizer("Library Book Club");
        event1.setContactInfo("bookclub@library.com");
        event1.setStatus("UPCOMING");
        event1.setParticipants(new HashSet<>());
        eventRepository.save(event1);

        // 2. Ongoing Workshop
        Event event2 = new Event();
        event2.setName("Digital Library Resources Workshop");
        event2.setDescription("Learn how to effectively use our digital library resources and online databases.");
        event2.setLocation("Computer Lab");
        event2.setStartDateTime(LocalDateTime.now().withHour(10).withMinute(0));
        event2.setEndDateTime(LocalDateTime.now().withHour(16).withMinute(0));
        event2.setCapacity(20);
        event2.setOrganizer("Library IT Team");
        event2.setContactInfo("it.support@library.com");
        event2.setStatus("ONGOING");
        event2.setParticipants(new HashSet<>());
        // Add some participants
        event2.getParticipants().add(member1);
        event2.getParticipants().add(member2);
        eventRepository.save(event2);

        // 3. Completed Event
        Event event3 = new Event();
        event3.setName("Children's Story Time");
        event3.setDescription("Interactive storytelling session for children aged 5-10 years.");
        event3.setLocation("Children's Reading Corner");
        event3.setStartDateTime(LocalDateTime.now().minusDays(3).withHour(10).withMinute(0));
        event3.setEndDateTime(LocalDateTime.now().minusDays(3).withHour(11).withMinute(30));
        event3.setCapacity(15);
        event3.setOrganizer("Children's Library Section");
        event3.setContactInfo("children@library.com");
        event3.setStatus("COMPLETED");
        event3.setParticipants(new HashSet<>());
        // Add participant
        event3.getParticipants().add(member3);
        eventRepository.save(event3);
    }
} 