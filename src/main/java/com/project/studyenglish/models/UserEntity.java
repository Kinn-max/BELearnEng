package com.project.studyenglish.models;


import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "user")
public class UserEntity  extends BaseEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fullname", length = 100)
    private String fullName;

    @Column(name = "email")
    private String email;

    @Column(name = "password", length = 200, nullable = false)
    private String password;

    @Column(name = "phone_number", length = 10)
    private String phoneNumber;

    @Column(name = "address")
    private String address;

    @Column(name = "is_active")
    private boolean active;

    @Column(name = "facebook_account_id")
    private int facebookAccountId;

    @Column(name = "activation_code")
    private int activationCode;

    @Column(name = "image",columnDefinition = "LONGTEXT")
    @Lob
    private String image;

    @Column(name = "google_account_id")
    private int googleAccountId;

    @OneToMany(mappedBy = "userEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<OrderEntity> orderEntityList = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "role_id")
    private RoleEntity roleEntity;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();
        authorityList.add(new SimpleGrantedAuthority("ROLE_"+getRoleEntity().getName().toUpperCase()));
        return authorityList;
    }
    @ManyToMany
    @JoinTable(
            name = "user_product_rating",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private List<ProductEntity> ratedProduct = new ArrayList<>();

    @Override
    public String getPassword() {
        return password;
    }
    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
