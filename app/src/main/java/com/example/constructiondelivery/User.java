package com.example.constructiondelivery;

import com.google.firebase.firestore.IgnoreExtraProperties;
import com.google.firebase.firestore.ServerTimestamp;
import java.util.Date;

@IgnoreExtraProperties
public class User {

    private String fullName;
    private String email;
    private String phone;
    private String gender;
    private String pincode;
    private String address;
    private String occupation;
    private String role;
    private Boolean isBlocked;

    @ServerTimestamp private Date createdAt;
    @ServerTimestamp private Date updatedAt;
    @ServerTimestamp private Date lastLoginAt;

    public User() {
        // Required empty constructor for Firestore
    }

    // Getters
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getGender() { return gender; }
    public String getPincode() { return pincode; }
    public String getAddress() { return address; }
    public String getOccupation() { return occupation; }
    public String getRole() { return role; }
    public Boolean getIsBlocked() { return isBlocked; }
    public Date getCreatedAt() { return createdAt; }
    public Date getUpdatedAt() { return updatedAt; }
    public Date getLastLoginAt() { return lastLoginAt; }

    // Setters
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setEmail(String email) { this.email = email; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setGender(String gender) { this.gender = gender; }
    public void setPincode(String pincode) { this.pincode = pincode; }
    public void setAddress(String address) { this.address = address; }
    public void setOccupation(String occupation) { this.occupation = occupation; }
    public void setRole(String role) { this.role = role; }
    public void setIsBlocked(Boolean isBlocked) { this.isBlocked = isBlocked; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }
    public void setLastLoginAt(Date lastLoginAt) { this.lastLoginAt = lastLoginAt; }
}
