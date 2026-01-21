package org.expensetracker.auth.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UserInfo {

    @Id
    @Column(name = "user_id", nullable = false, updatable = false)
    @EqualsAndHashCode.Include
    private String userId;

    @PrePersist
    private void generateUserId() {
        if (this.userId == null) {
            this.userId = UUID.randomUUID().toString();
        }
    }

    @Column(nullable = false, unique = true, length = 100)
    private String username;

    @Column(nullable = false, length = 100)
    @ToString.Exclude
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @Builder.Default
    private Set<UserRole> roles = new HashSet<>();
}
