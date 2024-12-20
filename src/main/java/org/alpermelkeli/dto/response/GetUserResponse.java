package org.alpermelkeli.dto.response;

import lombok.Data;
import org.alpermelkeli.model.RoleEntity;

import java.util.List;

@Data
public class GetUserResponse {
    private String id;
    private String email;
    private Double balance;
    private List<RoleEntity> role;

    public GetUserResponse(String id, String email, Double balance, List<RoleEntity> role) {
        this.id = id;
        this.email = email;
        this.balance = balance;
        this.role = role;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public List<RoleEntity> getRole() {
        return role;
    }

    public void setRole(List<RoleEntity> role) {
        this.role = role;
    }
}
