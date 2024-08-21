package com.website.repository.purchases.model;

import com.website.repository.model.item.Item;
import com.website.repository.model.user.User;
import com.website.repository.purchases.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "purchases")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(value = AuditingEntityListener.class)
public class Purchases {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "purchases_id", nullable = false)
    private Long id;

    @Column
    private String orderNumber;

    @Column(name = "order_date")
    @CreatedDate
    private LocalDateTime orderDate;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @JoinColumn(name = "item_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Item item;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private OrderStatus status;

    @Column(name = "total_amount")
    private Integer totalAmount;

    @Column(name = "address")
    private String address;

    @Column(name = "discount")
    private Integer discount;

    @Column(name = "notes", length = 1000)
    private String notes;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

}
