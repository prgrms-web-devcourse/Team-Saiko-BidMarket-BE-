package com.saiko.bidmarket.user.service;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.saiko.bidmarket.common.exception.NotFoundException;
import com.saiko.bidmarket.user.entity.Group;
import com.saiko.bidmarket.user.entity.User;
import com.saiko.bidmarket.user.repository.UserRepository;

@Service
@Transactional
public class DefaultUserService implements UserService {

  private final Logger log = LoggerFactory.getLogger(getClass());

  private final UserRepository userRepository;
  private final GroupService groupService;

  public DefaultUserService(UserRepository userRepository, GroupService groupService) {
    this.userRepository = userRepository;
    this.groupService = groupService;
  }

  @Override
  @Transactional(readOnly = true)
  public User findByProviderAndProviderId(String provider, String providerId) {
    Assert.hasText(provider, "Provider must be provided");
    Assert.hasText(providerId, "ProviderId must be provided");

    return userRepository.findByProviderAndProviderId(provider, providerId)
                         .orElseThrow(() -> new NotFoundException("User does not exist"));
  }

  @Override
  @Transactional
  public User join(OAuth2User oAuth2User, String authorizedClientRegistrationId) {
    Assert.notNull(oAuth2User, "OAuth2User must be provided");
    Assert.hasText(authorizedClientRegistrationId,
                   "AuthorizedClientRegistrationId must be provided");

    String providerId = oAuth2User.getName();
    try {
      User user = findByProviderAndProviderId(authorizedClientRegistrationId, providerId);
      log.warn("Already exists: {} for (provider: {}, providerId: {})", user,
               authorizedClientRegistrationId, providerId);
      return user;
    } catch (NotFoundException e) {
      Map<String, Object> attributes = oAuth2User.getAttributes();
      @SuppressWarnings("unchecked")
      Map<String, Object> properties = (Map<String, Object>)attributes.get("properties");

      if (properties == null) {
        throw new IllegalArgumentException("OAuth2User properties is empty");
      }

      String username = (String)properties.get("name");
      String profileImage = (String)properties.get("picture");
      Group group = groupService.findByName("USER_GROUP");

      User user = new User(username, profileImage, authorizedClientRegistrationId, providerId,
                           group);
      return userRepository.save(user);
    }
  }

}