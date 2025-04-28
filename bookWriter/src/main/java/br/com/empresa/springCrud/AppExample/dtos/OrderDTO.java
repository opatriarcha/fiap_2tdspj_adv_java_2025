package br.com.empresa.springCrud.AppExample.dtos;

import br.com.empresa.springCrud.AppExample.domainmodel.Order;
import br.com.empresa.springCrud.AppExample.domainmodel.OrderKey;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class OrderDTO {

    @NotNull(message = "O ID do usuário é obrigatório")
    private UUID userId;

    @NotNull(message = "O ID do pedido é obrigatório")
    private Long orderId;

    @NotBlank(message = "O nome do produto é obrigatório")
    @Size(max = 255, message = "O nome do produto deve ter no máximo 255 caracteres")
    private String productName;

    @Min(value = 1, message = "A quantidade deve ser pelo menos 1")
    private int quantity;

    @Min(value = 0, message = "O preço deve ser positivo")
    private double price;

    // Métodos de conversão

    public static OrderDTO fromEntity(Order order) {
        if (order == null) {
            return null;
        }
        OrderDTO dto = new OrderDTO();
        dto.setUserId(order.getId().getUserId());
        dto.setOrderId(order.getId().getOrderId());
        dto.setProductName(order.getProductName());
        dto.setQuantity(order.getQuantity());
        dto.setPrice(order.getPrice());
        return dto;
    }

    public static Order toEntity(OrderDTO dto) {
        if (dto == null) {
            return null;
        }
        Order order = new Order();
        order.setId(new OrderKey(dto.getUserId(), dto.getOrderId()));
        order.setProductName(dto.getProductName());
        order.setQuantity(dto.getQuantity());
        order.setPrice(dto.getPrice());
        return order;
    }
}
