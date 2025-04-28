package br.com.empresa.springCrud.AppExample.runners;

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

    private static final List<String> sampleNames = Arrays.asList(
            "Alice", "Bob", "Charlie", "Diana", "Edward", "Fiona", "George",
            "Hannah", "Ian", "Julia", "Kevin", "Laura", "Michael", "Nina",
            "Oscar", "Paula", "Quentin", "Rachel", "Samuel", "Tina"
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
            IntStream.rangeClosed(1, 50).forEach(i -> {
                String name = sampleNames.get(random.nextInt(sampleNames.size())) + " " + (char) ('A' + random.nextInt(26)) + ".";
                String email = name.replaceAll("\\s+", "").toLowerCase() + "@example.com";

                User user = new User();
                user.setName(name);
                user.setEmail(email);
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
            IntStream.rangeClosed(1, 50).forEach(i -> {
                Post post = new Post();
                post.setTitle("How to master " + sampleWords.get(random.nextInt(sampleWords.size())));
                post.setContent("In this article, we will explore " + sampleWords.get(random.nextInt(sampleWords.size())) + " deeply.");
                post.setUser(users.get(random.nextInt(users.size())));
                post.setTags(Set.of(finalTags.get(random.nextInt(finalTags.size()))));
                postRepository.save(post);
            });

            // Create Orders
            List<Order> orders = new ArrayList<>();
            IntStream.rangeClosed(1, 50).forEach(i -> {
                User user = users.get(random.nextInt(users.size()));
                OrderKey orderKey = new OrderKey(user.getId(), (long) (i));
                Order order = new Order();
                order.setId(orderKey);
                order.setProductName("Product " + sampleWords.get(random.nextInt(sampleWords.size())));
                order.setQuantity(random.nextInt(10) + 1);
                order.setPrice(50 + random.nextDouble() * 950); // entre 50 e 1000
                order.setUser(user);
                orders.add(orderRepository.save(order));
            });

            // Create OrderItems
            IntStream.rangeClosed(1, 50).forEach(i -> {
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
