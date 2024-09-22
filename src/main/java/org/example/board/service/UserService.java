package org.example.board.service;

import org.example.board.exception.follow.FollowAlreadyExistsException;
import org.example.board.exception.follow.FollowNotFoundException;
import org.example.board.exception.follow.InvalidFollowException;
import org.example.board.exception.user.UserAlreadyExistsException;
import org.example.board.exception.user.UserNotAllowedException;
import org.example.board.exception.user.UserNotFoundException;
import org.example.board.model.entity.FollowEntity;
import org.example.board.model.entity.UserEntity;
import org.example.board.model.user.User;
import org.example.board.model.user.UserAuthenticationResponse;
import org.example.board.model.user.UserPatchRequestBody;
import org.example.board.repository.FollowEntityRepository;
import org.example.board.repository.UserEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService implements UserDetailsService {

    @Autowired private UserEntityRepository userEntityRepository;
    @Autowired private BCryptPasswordEncoder passwordEncoder;
    @Autowired private JwtService jwtService;
    @Autowired private FollowEntityRepository followEntityRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userEntityRepository
                .findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username)
        );
    }

    public User signUp(String username, String password) {
        userEntityRepository
                .findByUsername(username)
                .ifPresent(
                        user -> {
                            throw new UserAlreadyExistsException();
                        });
        var userEntity
                = userEntityRepository.save(UserEntity.of(username, passwordEncoder.encode(password)));

        return User.from(userEntity);
    }

    public UserAuthenticationResponse authenticate(String username, String password) {
        var userEntity = userEntityRepository
                .findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        if (passwordEncoder.matches(password, userEntity.getPassword())) {
            var accessToken = jwtService.generateAccessToken(userEntity);
            return new UserAuthenticationResponse(accessToken);
        }
        else {
            throw new UserNotFoundException();
        }
    }

    public List<User> getUsers(String query) {
        List<UserEntity> userEntities;

        if(query != null && !query.isBlank()){
            //TODO: query 검색어 기반, 해당 검색어가 username에 포함되어 있는 유저목록 가져오기
            userEntities = userEntityRepository.findByUsernameContaining(query);
        }
        else{
            userEntities = userEntityRepository.findAll();
        }
        return userEntities.stream().map(User::from).toList();
    }

    public User getUser(String username) {
        var userEntity = userEntityRepository
                .findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
        return User.from(userEntity);
    }

    public User updateUser(String username, UserPatchRequestBody userPatchRequestBody, UserEntity currentUser) {
        var userEntity = userEntityRepository
                .findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        if(!userEntity.equals(currentUser)){
            throw new UserNotAllowedException();
        }

        if(userPatchRequestBody.description() != null){
            userEntity.setDescription(userPatchRequestBody.description());
        }

        return User.from(userEntityRepository.save(userEntity));
    }

    @Transactional
    public User follow(String username, UserEntity currentUser) {
        var following = userEntityRepository
                .findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        if(following.equals(currentUser)){
            throw new InvalidFollowException("A user cannot follow themselves.");
        }

        followEntityRepository.
                findByFollowerAndFollowing(currentUser, following)
                .ifPresent(
                        follow -> {
                            throw new FollowAlreadyExistsException(currentUser, following);
                        });
        followEntityRepository.save(
                FollowEntity.of(currentUser, following)
        );

        following.setFollowersCount(following.getFollowersCount() + 1);
        currentUser.setFollowingsCount(currentUser.getFollowingsCount() + 1);

        userEntityRepository.saveAll(List.of(following, currentUser));

        return User.from(following);
    }

    @Transactional
    public User unFollow(String username, UserEntity currentUser) {
        var following = userEntityRepository
                .findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        if(following.equals(currentUser)){
            throw new InvalidFollowException("A user cannot unfollow themselves.");
        }

        var followEntity = followEntityRepository.
                findByFollowerAndFollowing(currentUser, following)
                .orElseThrow(
                        () -> new FollowNotFoundException(currentUser, following));

        followEntityRepository.delete(followEntity);

        following.setFollowersCount(Math.max(0, following.getFollowersCount() - 1));
        currentUser.setFollowingsCount(Math.max(0, currentUser.getFollowingsCount() + 1));

        userEntityRepository.saveAll(List.of(following, currentUser));

        return User.from(following);
    }

    public List<User> getFollowersByUsername(String username) {
        var following = userEntityRepository
                .findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        var followEntities = followEntityRepository.findByFollowing(following);

        return followEntities.stream().map(follow -> User.from(follow.getFollower())).toList();
    }

    public List<User> getFollowingsByUsername(String username) {
        var follower = userEntityRepository
                .findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        var followEntities = followEntityRepository.findByFollowing(follower);

        return followEntities.stream().map(follow -> User.from(follow.getFollowing())).toList();
    }

}
