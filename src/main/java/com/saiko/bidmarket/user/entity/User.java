package com.saiko.bidmarket.user.entity;

import static org.apache.commons.lang3.StringUtils.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;

import org.springframework.util.Assert;

import com.saiko.bidmarket.common.entity.BaseTime;

@Entity
public class User extends BaseTime {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank
  @Column(length = 20)
  private String username;

  @NotBlank
  @Column(length = 512)
  private String profileImage;

  @NotBlank
  @Column(length = 20)
  private String provider;

  @NotBlank
  @Column(length = 80)
  private String providerId;

  @ManyToOne(optional = false)
  @JoinColumn(name = "group_id")
  private Group group;

  protected User() {/*no-op*/}

  public User(String username, String profileImage, String provider, String providerId, Group group) {
    Assert.isTrue(isNotBlank(username), "Username must be provided");
    Assert.isTrue(isNotBlank(profileImage), "ProfileImage must be provided");
    Assert.isTrue(isNotBlank(provider), "ProfileImage must be provided");
    Assert.isTrue(isNotBlank(providerId), "ProviderId must be provided");
    Assert.notNull(group, "Group must be provided");

    this.username = username;
    this.profileImage = profileImage;
    this.provider = provider;
    this.providerId = providerId;
    this.group = group;
  }

  public String getUsername() {
    return username;
  }

  public Group getGroup() {
    return group;
  }
}
