package br.com.empresa.springCrud.AppExample.dtos;

import br.com.empresa.springCrud.AppExample.domainmodel.OrderItem;
import br.com.empresa.springCrud.AppExample.domainmodel.OrderItemKey;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemDTO {

    @NotNull
    private Long orderId;

    @NotNull
    private Long itemId;

    @NotBlank
    private String itemName;

    @Min(1)
    private int quantity;

    public static OrderItemDTO fromEntity(OrderItem orderItem) {
        if (orderItem == null) return null;
        OrderItemDTO dto = new OrderItemDTO();
        dto.setOrderId(orderItem.getId().getOrderId());
        dto.setItemId(orderItem.getId().getItemId());
        dto.setItemName(orderItem.getItemName());
        dto.setQuantity(orderItem.getQuantity());
        return dto;
    }

    public static OrderItem toEntity(OrderItemDTO dto) {
        if (dto == null) return null;
        OrderItem orderItem = new OrderItem();
        orderItem.setId(new OrderItemKey(dto.getOrderId(), dto.getItemId()));
        orderItem.setItemName(dto.getItemName());
        orderItem.setQuantity(dto.getQuantity());
        return orderItem;
    }
}
