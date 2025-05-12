package br.com.empresa.springCrud.AppExample.infrastructure.runners;

import br.com.empresa.springCrud.AppExample.domainmodel.*;
import br.com.empresa.springCrud.AppExample.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Configuration
public class DataLoader {

    List<String> sampleNames = Arrays.asList(
            "Alice Smith", "Bob Johnson", "Charlie Brown", "Diana Miller", "Edward Davis",
            "Fiona Wilson", "George Taylor", "Hannah Moore", "Ian Anderson", "Julia Thomas",
            "Kevin Jackson", "Laura White", "Michael Harris", "Nina Martin", "Oscar Thompson",
            "Paula Garcia", "Quentin Martinez", "Rachel Robinson", "Samuel Clark", "Tina Rodriguez",
            "Alice Evans", "Bob Walker", "Charlie Hall", "Diana Allen", "Edward Young",
            "Fiona Hernandez", "George King", "Hannah Wright", "Ian Lopez", "Julia Hill",
            "Kevin Scott", "Laura Green", "Michael Adams", "Nina Baker", "Oscar Nelson",
            "Paula Carter", "Quentin Mitchell", "Rachel Perez", "Samuel Roberts", "Tina Turner",
            "Alice Cooper", "Bob Morgan", "Charlie Reed", "Diana Cook", "Edward Bell",
            "Fiona Murphy", "George Bailey", "Hannah Rivera", "Ian Cooper", "Julia Richardson",
            "Kevin Cox", "Laura Howard", "Michael Ward", "Nina Torres", "Oscar Peterson",
            "Paula Gray", "Quentin Ramirez", "Rachel James", "Samuel Watson", "Tina Brooks",
            "Alice Sanders", "Bob Price", "Charlie Bennett", "Diana Wood", "Edward Barnes",
            "Fiona Ross", "George Henderson", "Hannah Coleman", "Ian Jenkins", "Julia Perry",
            "Kevin Powell", "Laura Long", "Michael Patterson", "Nina Hughes", "Oscar Flores",
            "Paula Washington", "Quentin Butler", "Rachel Simmons", "Samuel Foster", "Tina Gonzales",
            "Alice Bryant", "Bob Alexander", "Charlie Russell", "Diana Griffin", "Edward Diaz",
            "Fiona Hayes", "George Myers", "Hannah Ford", "Ian Hamilton", "Julia Graham",
            "Kevin Sullivan", "Laura Wallace", "Michael West", "Nina Cole", "Oscar Jordan",
            "Paula Reynolds", "Quentin Fisher", "Rachel Ellis", "Samuel Harrison", "Tina Gibson"
    );


    private static final List<String> sampleWords = Arrays.asList(
            "tech", "finance", "education", "music", "gaming", "travel",
            "health", "sports", "food", "science", "nature", "history", "fashion"
    );

    private final Random random = new Random();

    @Bean
    CommandLineRunner initData(
            UserRepository userRepository,
            ProfileRepository profileRepository,
            RoleRepository roleRepository,
            PostRepository postRepository,
            TagRepository tagRepository,
            OrderRepository orderRepository,
            OrderItemRepository orderItemRepository
    ) {
        return args -> {
            // Create Roles
            List<Role> roles = IntStream.rangeClosed(1, 50)
                    .mapToObj(i -> new Role(null, "ROLE_" + sampleWords.get(random.nextInt(sampleWords.size())).toUpperCase(), null))
                    .collect(Collectors.toList());
            roles = roleRepository.saveAll(roles);

            // Create Tags
            List<Tag> tags = IntStream.rangeClosed(1, 50)
                    .mapToObj(i -> new Tag(null, sampleWords.get(random.nextInt(sampleWords.size())), null))
                    .collect(Collectors.toList());
            tags = tagRepository.saveAll(tags);

            // Create Users and Profiles
            List<User> users = new ArrayList<>();
            List<Role> finalRoles = roles;
            IntStream.rangeClosed(1, 1000).forEach(i -> {
                String name = sampleNames.get(random.nextInt(sampleNames.size())) + " " + (char) ('A' + random.nextInt(26)) + ".";
                String email = name.replaceAll("\\s+", "").toLowerCase() + "@example.com";

                User user = new User();
                user.setName(name);
                user.setEmail("orlando@gmail.com");
                user.setPassword("P@ssword" + i);
                user.setRoles(Set.of(finalRoles.get(random.nextInt(finalRoles.size()))));

                User savedUser = userRepository.save(user);

                Profile profile = new Profile();
                profile.setBio("Lover of " + sampleWords.get(random.nextInt(sampleWords.size())) + " and " + sampleWords.get(random.nextInt(sampleWords.size())));
                profile.setProfilePictureUrl("https://picsum.photos/id/" + (random.nextInt(100) + 1) + "/200/300");
                profile.setUser(savedUser);
                profileRepository.save(profile);

                users.add(savedUser);
            });

            // Create Posts
            List<Tag> finalTags = tags;
            IntStream.rangeClosed(1, 1000).forEach(i -> {
                Post post = new Post();
                post.setTitle("How to master " + sampleWords.get(random.nextInt(sampleWords.size())));
                post.setContent("In this article, we will explore " + sampleWords.get(random.nextInt(sampleWords.size())) + " deeply.");
                post.setUser(users.get(random.nextInt(users.size())));
                post.setTags(Set.of(finalTags.get(random.nextInt(finalTags.size()))));
                postRepository.save(post);
            });

            // Create Orders
            List<Order> orders = new ArrayList<>();
            IntStream.rangeClosed(1, 1000).forEach(i -> {
                User user = users.get(random.nextInt(users.size()));
                OrderKey orderKey = new OrderKey(user.getId(), (long) (i));
                Order order = new Order();
                order.setId(orderKey);
                order.setProductName("Product " + sampleWords.get(random.nextInt(sampleWords.size())));
                order.setQuantity(random.nextInt(10) + 1);
                order.setPrice(1000 + random.nextDouble() * 950); // entre 50 e 1000
                order.setUser(user);
                orders.add(orderRepository.save(order));
            });

            // Create OrderItems
            IntStream.rangeClosed(1, 1000).forEach(i -> {
                Order order = orders.get(random.nextInt(orders.size()));
                OrderItem item = new OrderItem();
                OrderItemKey itemKey = new OrderItemKey(order.getId().getOrderId(), (long) (i));
                item.setId(itemKey);
                item.setItemName("Accessory " + sampleWords.get(random.nextInt(sampleWords.size())));
                item.setQuantity(random.nextInt(5) + 1);
                item.setOrder(order);
                orderItemRepository.save(item);
            });
        };
    }
}
